// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 JJ del Rio
package es.jjrh.bikeradar

import kotlin.math.roundToInt

/** Decodes the 0x30 payload observed on the Echo Pro target channel. */
internal object KrossRadarDecoder {
    fun decode(payload: ByteArray, timestamp: Long): RadarState? {
        if (payload.size != 8 || payload[0].toInt() != 0x30) return null

        val levels = payload[1].toInt() and 0xff
        val packedRanges = (payload[3].toInt() and 0xff) or
            ((payload[4].toInt() and 0xff) shl 8) or
            ((payload[5].toInt() and 0xff) shl 16)
        val targets = (0 until 4).mapNotNull { slot ->
            val level = (levels shr (2 * slot)) and 3
            val range = (packedRanges shr (6 * slot)) and 63
            if (level !in 1..2 || range == 0) {
                null
            } else {
                Vehicle(
                    id = slot + 1,
                    distanceM = (range * 3.125).roundToInt(),
                    speedMs = 0f,
                    lateralUnknown = true,
                    reportedThreatLevel = level,
                )
            }
        }
        return RadarState(vehicles = targets, timestamp = timestamp, source = DataSource.KROSS)
    }
}
