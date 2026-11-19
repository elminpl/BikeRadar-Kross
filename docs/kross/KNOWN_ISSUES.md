# Kross Echo Pro — known issues

Status refers to test APK [1.12.2.k5](../../CHANGELOG-KROSS.md#1122k5).

| Area | Evidence and current status |
|---|---|
| BLE discovery and reconnect | S24+ captures show successful discovery and FFE2 telemetry, alongside earlier intermittent GATT status 133. k4 retries twice after a verified stream without waiting for a new sighting. Hardware timing after standby, loss of range and Bluetooth off/on remains unverified. |
| Connection labels | The earlier mismatch between the home screen and Settings is covered by automated UI tests. Check the transitions again on the S24+, including an Echo Pro without an Android bond. |
| Targets and alerts | The decoder was replayed against 1,447 frames from two stationary roadside captures. The overlay and audible alerts still need a physical riding test. |
| Target speed, direction and fifth slot | Speed units and left/right mapping are unverified. Both captures had side code zero for active targets and contained no type 0x31 frames. |
| Read-only diagnostics | One stationary S24+ session on k1 confirmed battery, model and the 53-byte FFF1 settings reply with firmware 1.0.2. Behavior across other firmware and settings remains to be checked. |
| Garmin compatibility | Selected automated BLE and UI regressions passed. A physical Garmin reconnect and alert check remains outstanding. |
| Screenshot baseline | An earlier broad run reported four screenshot discrepancies. The later selected UTC runs passed their screenshot checks. See the [test record](TEST_PLAN.md#recorded-test-results) for scope. |

Earlier Smali builds reported a verifier failure near `RadarLinkCoordinator.markConnected` and repeated `connectAndRun entered` messages apparently emitted when coroutines resumed. The Kotlin path uses its own telemetry timeout for Kross, while Garmin keeps its existing handshake and stream watchdog. See [project history](PROJECT_HISTORY.md) for the experimental builds.

## Capture follow-up

Seven captures from 19 November 2026, five from k2 and two from k1, contain 2,995 identical idle FFE2 frames and one FFF1 settings reply per file. The recorded battery was 100% and the firmware response was 1.0.2. The interval from first to last notification ranged from 27 seconds to about 159 seconds. Every file ended after the telemetry watchdog stopped receiving frames. The captures do not establish why the frames stopped or confirm behavior with moving targets.


## k4 hardware reconnect journal

The S24+ log from 19 November shows 390 FFE2 notifications before a reconnect scheduled after one second. That attempt reached GATT and began discovery before status 8, followed by the scheduled second retry and status 147 before discovery. The trace confirms the two retries and leaves the cause of the BLE disconnect unresolved.
