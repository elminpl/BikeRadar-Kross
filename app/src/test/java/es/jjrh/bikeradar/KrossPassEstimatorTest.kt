// SPDX-License-Identifier: GPL-3.0-or-later
package es.jjrh.bikeradar

import org.junit.Assert.assertEquals
import org.junit.Test

class KrossPassEstimatorTest {
    private fun KrossPassEstimator.approach() {
        listOf(28, 21, 15, 9).forEachIndexed { index, distance ->
            assertEquals(0, observe(listOf(Vehicle(1, distance, 0f)), index * 150L))
        }
    }

    @Test
    fun countsOneApproachWithContinuedTelemetry() {
        val estimator = KrossPassEstimator()
        estimator.approach()
        assertEquals(0, estimator.observe(emptyList(), 600))
        assertEquals(1, estimator.observe(emptyList(), 900))
        assertEquals(0, estimator.observe(emptyList(), 1_050))
    }

    @Test
    fun aMissingFrameOrLinkLossDoesNotCount() {
        val estimator = KrossPassEstimator()
        estimator.approach()
        assertEquals(0, estimator.observe(emptyList(), 600))
        assertEquals(0, estimator.observe(listOf(Vehicle(1, 9, 0f)), 750))
        assertEquals(0, estimator.observe(emptyList(), 3_000))
    }
}
