# Kross Echo Pro — test plan and results

## Recorded test results

| Build checkpoint | Verification |
|---|---|
| Targets on the overlay | 261 selected tests passed, including 43 screenshot tests. The Kotlin decoder replayed 1,447 type 0x30 frames from two S24+ captures, with 301 frames containing active targets. |
| Connection status in Settings | 190 selected tests passed, including 113 screenshot tests and a dedicated check of Quick Status and Connections for an unbonded Kross. |
| Read-only Kross diagnostics | 252 selected tests passed, including 165 BLE controller tests and 9 screenshot tests. The harness checked that the query is opt-in and that telemetry continues while a simulated response is processed. |
| 1.12.2.k1 packaging | Release build and DEX keep verification passed. Package `es.jjrh.bikeradar.kross`, version code 45 and the existing test certificate were verified. The previous selected tests cover its app behavior. |
| Hardware-response and roadside replay follow-up | 177 selected tests passed, including 9 Debug screenshots. The BLE harness uses the captured 53-byte FFF1 reply. The decoder replays 22 FFE2 frames with relative timestamps from the two roadside sessions, including transitions to empty frames. |
| 1.12.2.k2 | 299 selected tests passed, including 113 screenshot tests. Test coverage includes Kross battery and firmware in System and Quick Status, and session cleanup on disconnect. |
| 1.12.2.k3 | 311 selected tests passed, including 90 screenshots in UTC. Tests cover capture reuse, expiry and link targets in About. Six references changed for the requested UI updates. Debug APK, ktlint, release build and DEX keep verification passed. The signed APK has package `es.jjrh.bikeradar.kross`, version code 47 and the same certificate as k2. |
| 1.12.2.k4 | 237 selected tests passed, including 16 screenshots in UTC. The BLE harness verifies the bounded retry sequence after decoded telemetry. A renderer test confirms that a decoded Kross target is drawn and cleared by the idle frame. Debug, ktlint, Release and DEX keep verification passed. Signed package `es.jjrh.bikeradar.kross` has version code 48 and the k3 certificate. |

An earlier broad test run reported four Roborazzi discrepancies. The selected runs above used a UTC JVM and kept the existing reference images. The table describes the recorded checkpoints. A full clean test suite on k1 has yet to be recorded.

## Next device checks

1. **Connection and status on S24+.** Open the Radar screen and select the Echo Pro. Check its status in Home, System, Settings → Quick Status and Settings → Connections. Expect connecting, Bluetooth connected, and limited coverage once decoded frames arrive. Confirm that Paired reflects the Android bond. Repeat after turning Bluetooth off and on, moving out of range and restarting the app.
2. **Read-only diagnostics.** The first stationary S24+ read on k1 passed. Repeat after reconnecting or with another firmware and compare the result. Enable capture logging before connecting to capture a complete FFF1 response. Check captures for identifiers before sharing them.
3. **Targets on the overlay.** Compare displayed range, threat color, audible alerts and clearing of targets against observed traffic. Record independent timestamps in a controlled setting. The existing stationary captures cover passes on both sides and support range and threat decoding.
4. **Unknown fields.** Collect a capture containing type 0x31, a controlled speed reference and a side code that varies with the setup. Record which firmware produced each capture.
5. **Garmin regression.** With a Garmin V1 or V2 unit, verify selecting the radar, reconnect, telemetry and audible alerts. Rerun relevant JVM and Roborazzi tests with `-Duser.timezone=UTC` on BLE or UI changes.

For k3, exercise the Polish diagnostics screen at a narrow width and large font. Check that the sound preview expands, capture sharing and bulk deletion remain usable, and the radar-state log shows fields for the active model. Reconnect the same radar before and after 15 seconds, then switch to a second device. Confirm the capture is reused only across the short gap for the same radar. Check that Garmin options become available again after the Kross stream ends.

Keep the original reference images for environment-only differences. Track the test results and hardware limitations for each named APK in the [Kross changelog](../../CHANGELOG-KROSS.md).
