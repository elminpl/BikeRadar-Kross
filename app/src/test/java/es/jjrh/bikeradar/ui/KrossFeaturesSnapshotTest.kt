// SPDX-License-Identifier: GPL-3.0-or-later
package es.jjrh.bikeradar.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.captureRoboImage
import es.jjrh.bikeradar.ClosePassStateBus
import es.jjrh.bikeradar.DataSource
import es.jjrh.bikeradar.RadarState
import es.jjrh.bikeradar.RadarStateBus
import es.jjrh.bikeradar.RideHistoryRecord
import es.jjrh.bikeradar.data.Prefs
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = "pl-rPL-w448dp-h997dp-xxhdpi")
class KrossFeaturesSnapshotTest {
    @Test
    fun overlaySettings() {
        val prefs = Prefs(ApplicationProvider.getApplicationContext()).apply {
            overlayWidthDp = 170
            overlayHeightPct = 80
            overlayOpacity = 0.8f
        }
        captureRoboImage { SettingsOverlay(rememberNavController(), prefs) }
    }

    @Test
    fun passingCount() {
        ClosePassStateBus.reset()
        ClosePassStateBus.increment(2)
        ClosePassStateBus.setPassingCount(12)
        try {
            captureRoboImage {
                UiTheme {
                    Column(Modifier.fillMaxWidth().background(LocalBrColors.current.bg).padding(16.dp)) {
                        ClosePassStatsCard(loggingEnabled = true, rideStored = true, rangeOnly = false)
                        PassingStatsCard(onClick = {})
                    }
                }
            }
        } finally {
            ClosePassStateBus.reset()
            ClosePassStateBus.resetPassing()
        }
    }

    @Test
    fun estimatedKrossPasses() {
        ClosePassStateBus.resetPassing()
        ClosePassStateBus.addEstimatedPasses(7)
        RadarStateBus.publish(RadarState(vehicles = emptyList(), timestamp = 1L, source = DataSource.KROSS))
        try {
            captureRoboImage {
                UiTheme {
                    Column(Modifier.fillMaxWidth().background(LocalBrColors.current.bg).padding(16.dp)) {
                        PassingStatsCard(onClick = {})
                    }
                }
            }
        } finally {
            RadarStateBus.clear()
            ClosePassStateBus.resetPassing()
        }
    }

    @Test
    fun passingHistory() {
        captureRoboImage {
            RideHistoryContent(
                records = listOf(
                    RideHistoryRecord(
                        startedAtMs = 1_780_733_520_000L,
                        endedAtMs = 1_780_736_040_000L,
                        overtakes = 15,
                        closePasses = 2,
                        grazingPasses = 0,
                        hgvClosePasses = 0,
                        peakClosingKmh = 52,
                        closingSpeedP90Kmh = 38,
                        minLateralClearanceM = null,
                        distanceKm = 12.4f,
                        exposureSeconds = 950L,
                        alertsPerKm = null,
                        tightestPassClearanceM = null,
                        tightestPassClosingKmh = null,
                        closePassesCounted = true,
                        passingCount = 12,
                    ),
                ),
                onBack = {},
            )
        }
    }
}
