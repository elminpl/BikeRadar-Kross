// SPDX-License-Identifier: GPL-3.0-or-later
package es.jjrh.bikeradar.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import es.jjrh.bikeradar.DataSource
import es.jjrh.bikeradar.R
import es.jjrh.bikeradar.RadarOverlayView
import es.jjrh.bikeradar.RadarState
import es.jjrh.bikeradar.Vehicle
import es.jjrh.bikeradar.data.Prefs
import es.jjrh.bikeradar.data.PrefsSnapshot
import kotlin.math.roundToInt

private val previewState = RadarState(
    source = DataSource.KROSS,
    vehicles = listOf(
        Vehicle(id = 1, distanceM = 12, speedMs = 0f, reportedThreatLevel = 2),
        Vehicle(id = 2, distanceM = 35, speedMs = 0f, reportedThreatLevel = 1),
    ),
    timestamp = 1L,
)

@Composable
internal fun SettingsOverlay(navController: NavController, prefs: Prefs) {
    val settings by prefs.flow.collectAsState(initial = prefs.snapshot())
    var fullSize by rememberSaveable { mutableStateOf(false) }
    UiTheme {
        val br = LocalBrColors.current
        Column(
            Modifier.fillMaxSize().background(br.bg).systemBarsPadding().verticalScroll(rememberScrollState()),
        ) {
            SettingsHeader(stringResource(R.string.settings_radar_section_overlay), onBack = { navController.popBackStack() })
            OverlayPreview(settings, Modifier.fillMaxWidth().height(210.dp))
            androidx.compose.material3.TextButton(
                modifier = Modifier.padding(horizontal = 16.dp),
                onClick = { fullSize = true },
            ) {
                androidx.compose.material3.Text(stringResource(R.string.settings_overlay_full_size))
            }
            if (fullSize) {
                Dialog(onDismissRequest = { fullSize = false }, properties = DialogProperties(usePlatformDefaultWidth = false)) {
                    androidx.compose.foundation.layout.Box(Modifier.fillMaxSize()) {
                        OverlayPreview(settings, Modifier.fillMaxSize())
                        androidx.compose.material3.TextButton(
                            modifier = Modifier.align(Alignment.BottomStart).systemBarsPadding().padding(16.dp),
                            onClick = { fullSize = false },
                        ) {
                            androidx.compose.material3.Text(stringResource(R.string.settings_overlay_close_preview))
                        }
                    }
                }
            }
            SettingsSliderRow(
                title = stringResource(R.string.settings_overlay_width),
                valueDisplay = stringResource(R.string.settings_overlay_dp, settings.overlayWidthDp),
                helper = null,
                value = settings.overlayWidthDp.toFloat(),
                valueRange = 80f..220f,
                onValueChange = { prefs.overlayWidthDp = it.roundToInt() },
            )
            SettingsSliderRow(
                title = stringResource(R.string.settings_overlay_height),
                valueDisplay = stringResource(R.string.settings_radar_percent_value, settings.overlayHeightPct),
                helper = null,
                value = settings.overlayHeightPct.toFloat(),
                valueRange = 40f..100f,
                onValueChange = { prefs.overlayHeightPct = it.roundToInt() },
            )
            SettingsSliderRow(
                title = stringResource(R.string.settings_radar_overlay_dimmer_title),
                valueDisplay = stringResource(R.string.settings_radar_percent_value, (100 * settings.overlayOpacity).roundToInt()),
                helper = null,
                value = settings.overlayOpacity,
                valueRange = 0.5f..1f,
                onValueChange = { prefs.overlayOpacity = it },
            )
            SettingsSliderRow(
                title = stringResource(R.string.settings_radar_visual_distance_title),
                valueDisplay = stringResource(R.string.settings_radar_meters_value, settings.visualMaxDistanceM),
                helper = null,
                value = settings.visualMaxDistanceM.toFloat(),
                valueRange = 10f..80f,
                onValueChange = { prefs.visualMaxDistanceM = it.roundToInt() },
            )
        }
    }
}

@Composable
private fun OverlayPreview(settings: PrefsSnapshot, modifier: Modifier) {
    val br = LocalBrColors.current
    BoxWithConstraints(modifier.background(br.bgElev1)) {
        val roadColor = br.hairline2
        Canvas(Modifier.fillMaxSize()) {
            for (fraction in listOf(0.15f, 0.45f, 0.75f)) {
                drawLine(roadColor, Offset(0f, size.height * fraction), Offset(size.width, size.height * (fraction + 0.1f)), 8.dp.toPx())
            }
            drawLine(roadColor, Offset(size.width * 0.2f, 0f), Offset(size.width * 0.54f, size.height), 12.dp.toPx())
        }
        AndroidView(
            modifier = Modifier.align(Alignment.TopEnd).size(
                width = settings.overlayWidthDp.dp.coerceAtMost(maxWidth),
                height = maxHeight * (settings.overlayHeightPct / 100f),
            ),
            factory = { RadarOverlayView(it) },
            update = { view ->
                view.setState(previewState)
                view.setVisualMaxM(settings.visualMaxDistanceM)
                view.setAlertMaxM(settings.alertMaxDistanceM)
                view.setAdaptiveAlerts(settings.adaptiveAlertsEnabled)
                view.alpha = settings.overlayOpacity
            },
        )
    }
}
