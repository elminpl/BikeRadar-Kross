// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 JJ del Rio
package es.jjrh.bikeradar

import android.annotation.SuppressLint
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.withTimeoutOrNull

/** Read-only queries on the verified Echo Pro profile. */
internal object KrossDeviceProbe {
    private const val FIRMWARE_OFFSET = 23
    private val firmwareVersion = Regex("""(?:VER:\s*)?(\d{1,4}(?:\.\d{1,4}){1,3})(?:[^\w.]|$)""")

    fun isConfigReplyCandidate(bytes: ByteArray): Boolean = bytes.size >= 3 &&
        bytes[0].toInt() == 0x10 &&
        bytes[2].toUByte().toInt() == 0xba

    fun revision(bytes: ByteArray): String? {
        if (bytes.size <= FIRMWARE_OFFSET || bytes[0].toInt() != 0x10 || bytes[2].toUByte().toInt() != 0xba) return null
        return firmwareVersion.matchAt(bytes.decodeToString(startIndex = FIRMWARE_OFFSET), 0)?.groupValues?.get(1)
    }

    private fun describeReply(bytes: ByteArray): String {
        val code = (bytes[2].toInt() and 0xff).toString(16).padStart(2, '0')
        val summary = "FFF1=0x$code (${bytes.size} B)"
        if (bytes.size < FIRMWARE_OFFSET) return "$summary short-reply"

        val revision = revision(bytes)
        return "$summary mode=${bytes[3].toUByte()} brightness=${bytes[4].toUByte()} " +
            "charging=${bytes[5].toUByte()} firmware≈${revision ?: "?"}"
    }

    @SuppressLint("MissingPermission")
    suspend fun readBattery(gatt: BluetoothGatt, queue: BleOpQueue): Int? {
        val battery = gatt.getService(Uuids.SVC_BATTERY)?.getCharacteristic(Uuids.CHAR_BATTERY)
            ?.takeIf { it.properties and BluetoothGattCharacteristic.PROPERTY_READ != 0 } ?: return null
        return queue.read(gatt, battery)?.singleOrNull()?.toUByte()?.toInt()?.takeIf { it <= 100 }
    }

    @SuppressLint("MissingPermission")
    suspend fun read(
        gatt: BluetoothGatt,
        queue: BleOpQueue,
        commandSubscribed: Boolean,
        expectReply: (CompletableDeferred<ByteArray>) -> Unit,
        onBattery: (Int) -> Unit,
        onFirmware: (String) -> Unit,
    ): String {
        val pct = readBattery(gatt, queue)
        if (pct != null) onBattery(pct)
        val batteryStatus = pct?.let { "$it%" } ?: "no-valid-reading"
        val model = gatt.getService(Uuids.SVC_DIS)?.getCharacteristic(Uuids.DIS_MODEL_NUMBER)
            ?.takeIf { it.properties and BluetoothGattCharacteristic.PROPERTY_READ != 0 }
            ?.let { queue.read(gatt, it) }
            ?.takeIf { it.size in 1..32 }
            ?.decodeToString()?.trimEnd('\u0000')?.takeIf { it.matches(Regex("[a-zA-Z0-9 _.-]{1,32}")) }
        val identity = "2A19=$batteryStatus · 2A24=${model ?: "?"}"
        val input = KrossBleDiagnostics.commandWriteChannel(gatt).takeIf { commandSubscribed }
        if (input == null) return "$identity · FFF1=unavailable"

        // Firmware starts beyond the default ATT notification payload of 20 bytes.
        val mtu = queue.requestMtu(gatt, 64)
        val reply = CompletableDeferred<ByteArray>()
        expectReply(reply)
        val request = byteArrayOf(0x11, 0x02, 0xba.toByte(), 0xa9.toByte())
        val writeConfirmed = queue.write(gatt, input, request, noResponse = true)
        val bytes = withTimeoutOrNull(1_000L) { reply.await() }
        bytes?.let(::revision)?.let(onFirmware)
        val configStatus = when {
            bytes != null -> describeReply(bytes)
            writeConfirmed -> "FFF1=timeout"
            else -> "FFF2=write-unconfirmed"
        }
        return "$identity · MTU=$mtu · $configStatus"
    }
}
