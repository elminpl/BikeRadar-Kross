# Kross Echo Pro — project handover

## Scope and provenance
This is a handover of observations from prior Bike Radar APK/Smali experiments on a Samsung Galaxy S24+. It is **not** source code already merged into this repository. Observations are from user-supplied diagnostic bundles and previous development notes; original capture files and APKs must be inspected independently before trusting details.

Upstream: https://github.com/partymola/android-bike-radar-overlay
Working branch: `feature/kross-echo-pro`. Preserve upstream license and attribution.

## Development chronology
1. Started from Bike Radar 1.11.0 (code 41). Modified an APK through apktool/Smali; multiple experimental builds were produced.
2. Kross-specific BLE service discovery and handshake were added experimentally. One session at 2026-11-17 19:25:52 reported `newState=2`, `radar handshake complete`, and a Kross-specific bypass of a Garmin V2 watchdog.
3. Other captures reported recurring notifications from characteristic `FFE2` but no decoded vehicle events.
4. UI/state patches attempted to replace persistent `Connecting` with `BLE połączone`. Subsequent screens showed contradictory top and bottom status cards, e.g. `BLE połączone` and `Rear radar — Brak sygnału`.
5. A diagnostic journal at 19:57 repeated `connectAndRun entered` many times. Later analysis identified that the log was inserted ahead of Kotlin coroutine resume handling: repeated lines do **not** prove repeated new GATT connections.
6. User chose to roll back to the earlier `BikeRadar_Kross_UIState.apk` checkpoint, before later StatusFix/BLEStatus changes. This checkpoint is **not yet verified** by a new physical test.

## Previous artifact names (external, not checked into Git)
- `BikeRadar_Kross_UIState.apk` — rollback candidate.
- `BikeRadar_Kross_JobTrace.apk`, `BikeRadar_Kross_ConnectTrace.apk`, `BikeRadar_Kross_GattReturn.apk`, `BikeRadar_Kross_NoResumeFlood.apk` — successive diagnostics.
- Earlier capture files: `bike-radar-capture-20261117-195438.log` and `bike-radar-capture-20261117-192552.log.gz`.
- Some artifacts were stored outside Git under Google Drive folder `PROJEKTY/BikeRadarKross`. Availability and hashes have not been validated here.

## Important lessons
- Do not port Smali patches wholesale. Reimplement intentionally in Kotlin with tests.
- Separate physical GATT connectivity, handshake, fresh decoded radar stream, and vehicle detection.
- Avoid logging on coroutine resume paths as if they were new connections.
- Do not infer protocol semantics from a single repeated frame.
- Preserve baseline `main`; implement on feature branch with small, reversible commits.

## First Codex task
Inspect the actual repository implementation, identify BLE connection/state/decoder paths, and propose a minimal Kross adapter and test harness. Do not change behavior until baseline tests and device evidence are reviewed.
