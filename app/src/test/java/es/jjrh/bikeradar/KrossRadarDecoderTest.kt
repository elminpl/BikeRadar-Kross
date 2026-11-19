// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 JJ del Rio
package es.jjrh.bikeradar

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class KrossRadarDecoderTest {
    @Test fun decodesFourPackedRangesAndTheirThreatLevels() {
        val packed = 8 or (16 shl 6) or (24 shl 12) or (32 shl 18)
        val payload = byteArrayOf(0x30, 0x99.toByte(), 0, packed.toByte(), (packed shr 8).toByte(), (packed shr 16).toByte(), 0, 0)
        val state = requireNotNull(KrossRadarDecoder.decode(payload, 42))
        assertEquals(DataSource.KROSS, state.source)
        assertEquals(42L, state.timestamp)
        assertEquals(listOf(25, 50, 75, 100), state.vehicles.map { it.distanceM })
        assertEquals(listOf(1, 2, 1, 2), state.vehicles.map { it.reportedThreatLevel })
        assertTrue(state.vehicles.all { it.lateralUnknown })
        assertFalse(state.source.hasClosingSpeed)
    }

    @Test fun replaysRoadsideCapturesThroughToEmptyFrames() {
        for (side in listOf("left", "right")) {
            val fixture = requireNotNull(javaClass.getResourceAsStream("/kross-replay-$side.txt"))
            val frames = fixture.bufferedReader().useLines { lines -> lines.filterNot { it.startsWith("#") }.toList() }
            assertTrue(frames.first().substringAfterLast(' ') != "-")
            assertEquals("-", frames.last().substringAfterLast(' '))
            for (frame in frames) {
                val (elapsed, hex, expected) = frame.split(' ')
                val state = requireNotNull(KrossRadarDecoder.decode(hex.hexToBytes(), elapsed.toLong()))
                assertEquals(DataSource.KROSS, state.source)
                assertEquals(elapsed.toLong(), state.timestamp)
                val actual = state.vehicles.joinToString(",") { "${it.id}:${it.distanceM}:${it.reportedThreatLevel}" }
                assertEquals("$side at ${elapsed}ms", expected, actual.ifEmpty { "-" })
            }
        }
    }

    @Test fun idleFrameClearsVehiclesAndUnsupportedPacketsAreIgnored() {
        val idle = byteArrayOf(0x30, 0, 0x55, 0, 0, 0, 0xff.toByte(), 0xf0.toByte())
        assertTrue(requireNotNull(KrossRadarDecoder.decode(idle, 42)).vehicles.isEmpty())
        assertNull(KrossRadarDecoder.decode(idle.copyOf(7), 42))
        idle[0] = 0x31
        assertNull(KrossRadarDecoder.decode(idle, 42))
    }
}
