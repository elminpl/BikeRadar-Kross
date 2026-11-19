# Kross Echo Pro — known issues

Status refers to test APK [1.12.2.k1](../../CHANGELOG-KROSS.md#1122k1).

| Area | Evidence and current status |
|---|---|
| BLE discovery and reconnect | S24+ captures show successful discovery and FFE2 telemetry, alongside earlier intermittent GATT status 133. Check reconnect after loss of range, Bluetooth off/on and app restart. |
| Connection labels | The earlier mismatch between the home screen and Settings is covered by automated UI tests. Check the transitions again on the S24+ with k1, including an Echo Pro without an Android bond. |
| Targets and alerts | The decoder was replayed against 1,447 frames from two stationary roadside captures. The k1 overlay and audible alerts still need a physical riding test. |
| Target speed, direction and fifth slot | Speed units and left/right mapping are unverified. Both captures had side code zero for active targets and contained no type 0x31 frames. |
| Read-only diagnostics | A simulated FFF1 reply passed the integration test. Battery, model, settings and firmware responses still need to be checked on the physical Echo Pro. |
| Garmin compatibility | Selected automated BLE and UI regressions passed. A physical Garmin reconnect and alert check remains outstanding. |
| Screenshot baseline | An earlier broad run reported four screenshot discrepancies. The later selected UTC runs passed their screenshot checks. See the [test record](TEST_PLAN.md#recorded-test-results) for scope. |

Earlier Smali builds reported a verifier failure near `RadarLinkCoordinator.markConnected` and repeated `connectAndRun entered` messages apparently emitted when coroutines resumed. The Kotlin path uses its own telemetry timeout for Kross, while Garmin keeps its existing handshake and stream watchdog. See [project history](PROJECT_HISTORY.md) for the experimental builds.
