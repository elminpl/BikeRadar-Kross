// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 JJ del Rio
package es.jjrh.bikeradar

import android.annotation.SuppressLint
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import java.util.UUID

enum class RadarDiagnosticStage { CONNECTING, GATT_CONNECTED, SUBSCRIBED }

/** Profile recognition and characteristic validation for the observed Echo Pro GATT table. */
internal object KrossBleDiagnostics {
    private fun bluetoothUuid(short: String): UUID = UUID.fromString("0000$short-0000-1000-8000-00805f9b34fb")

    private val service = UUID.fromString("aa86ffe0-3884-465c-a034-c242988b0000")
    private val write = UUID.fromString("aa86ffe1-3884-465c-a034-c242988b0000")
    private val commandService = bluetoothUuid("fff0")
    private val commandWrite = bluetoothUuid("fff2")
    private val notifications: UUID = UUID.fromString("aa86ffe2-3884-465c-a034-c242988b0000")

    val advertisedServices = setOf(bluetoothUuid("1816"), Uuids.SVC_BATTERY)

    private fun matchesName(name: String): Boolean = name.startsWith("Radar", ignoreCase = true) ||
        name.equals("Kross Echo Pro", ignoreCase = true)

    fun matchesAdvertisement(name: String, services: List<UUID>): Boolean = matchesName(name) &&
        services.any { it in advertisedServices }

    fun isSelected(name: String, mac: String, pin: String?): Boolean = mac.equals(pin, ignoreCase = true) && matchesName(name)

    @SuppressLint("MissingPermission")
    fun notificationChannel(gatt: BluetoothGatt): BluetoothGattCharacteristic? {
        // A table with the existing radar service always belongs to the existing path.
        if (gatt.getService(Uuids.SVC_RADAR) != null) return null
        val profile = gatt.getService(service) ?: return null
        val output = profile.getCharacteristic(notifications) ?: return null
        val input = profile.getCharacteristic(write) ?: return null
        val writeFlags = BluetoothGattCharacteristic.PROPERTY_WRITE or BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE
        return output.takeIf {
            input.properties and writeFlags != 0 &&
                it.properties and BluetoothGattCharacteristic.PROPERTY_NOTIFY != 0 &&
                it.getDescriptor(Uuids.CCCD) != null
        }
    }

    @SuppressLint("MissingPermission")
    fun commandNotificationChannel(gatt: BluetoothGatt): BluetoothGattCharacteristic? {
        val profile = gatt.getService(commandService) ?: return null
        commandWriteChannel(gatt) ?: return null
        return profile.getCharacteristic(bluetoothUuid("fff1"))?.takeIf {
            it.properties and BluetoothGattCharacteristic.PROPERTY_NOTIFY != 0 &&
                it.getDescriptor(Uuids.CCCD) != null
        }
    }

    @SuppressLint("MissingPermission")
    fun commandWriteChannel(gatt: BluetoothGatt): BluetoothGattCharacteristic? {
        val input = gatt.getService(commandService)?.getCharacteristic(commandWrite) ?: return null
        return input.takeIf { it.properties and BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE != 0 }
    }

    @SuppressLint("MissingPermission")
    fun describe(gatt: BluetoothGatt): String = gatt.services.joinToString(" ") { service ->
        val characteristics = service.characteristics.joinToString(",") { characteristic ->
            "${characteristic.uuid}/0x${characteristic.properties.toString(16)}"
        }
        "${service.uuid}[$characteristics]"
    }
}
