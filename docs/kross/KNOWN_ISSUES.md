# Known issues and hypotheses

| Issue | Evidence | Status / handling |
|---|---|---|
| No vehicle detection | BLE notification frames observed, no verified vehicle parser | Open; decoder research needed |
| Conflicting UI statuses | Screenshot: `BLE połączone` vs `Rear radar — Brak sygnału` | Open; separate connection vs streaming states |
| `Connecting` persisted | Earlier experimental APK UI | Reproduce on clean rollback baseline |
| Repeated `connectAndRun entered` journal entries | 40 entries in ~6 seconds on 2026-11-17 19:57 | **Not** evidence of reconnect storm; logging before coroutine resume handling likely explains repetitions |
| GATT connection uncertain in later capture | Some journals showed `about to invoke openGatt` without subsequent callback in last 40 entries | Inconclusive; inspect full capture |
| Garmin-specific watchdog | Earlier patch bypassed watchdog for Kross | Review design; do not disable all liveness checks |
| UI state Smali verifier regression | Previous experimental changes to `RadarLinkCoordinator.markConnected` caused VerifyError | Avoid direct Smali register surgery; implement Kotlin and test |

## Prior code areas named in APK analysis
These are *decompiled artifact names*, not verified source paths:
- `RadarLinkController$connectAndRun$cb$1.smali` — GATT callbacks.
- `RadarLinkController$connectAndRun$3.smali` — watchdog coroutine.
- `RadarLinkCoordinator.markConnected()` — link state.
- `RadarLinkStatus.isConnecting()` — UI state.
- `RadarStateBus.radarStreamIsLive()` — freshness of decoded radar data.
- `BikeRadarService.followRadarPin()` — connection scheduling.

Find corresponding Kotlin definitions in repository before proposing changes.
