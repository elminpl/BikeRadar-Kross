# Kross Echo Pro — staged test plan

## A. Repository baseline (no Kross changes)
1. Confirm build toolchain, build debug APK, run existing unit and screenshot tests.
2. Record failing tests/dependency problems separately from Kross work.
3. Keep upstream `main` untouched; work on `feature/kross-echo-pro`.

## B. Physical BLE connection (radar charged)
1. Fresh app install; grant Bluetooth permissions; select Kross Echo Pro.
2. Capture full timestamped BLE discovery, connection callback, service discovery, characteristic properties, CCCD subscription, and handshake.
3. Confirm one logical connection attempt is not misreported repeatedly by coroutine resume logs.
4. Verify reconnect after moving out of range and after toggling Bluetooth.
5. Check foreground/background behavior and app restart.

## C. Protocol capture
1. Capture idle frames for several minutes.
2. Capture controlled real-world approach/departure events in a safe environment, with independent event timestamps.
3. Compare bytes and frequencies; build parser fixtures from real captures.
4. Validate parsing against multiple events before enabling vehicle UI or audio alerts.

## D. UI semantics
- **Disconnected**: no active GATT.
- **Connecting**: attempt in progress.
- **BLE connected / awaiting data**: GATT/handshake active but no validated radar telemetry.
- **Radar data active**: recent *decoded* frames.
- **Vehicle detected**: only when a verified decoder reports a vehicle.
- Keep top status and SYSTEM card consistent about which layer they describe.

## E. Regression checks
- Existing Garmin radar support must continue to work.
- No duplicate GATT jobs, busy loops, or unbounded log floods.
- No misleading `ready` status when only heartbeat frames arrive.
- No new Android verifier/runtime errors.
- Roll back to last known-good commit on failure.

## Release gate
Do not describe Kross vehicle alerts as functional until physical device tests confirm decoded vehicle events. APK smoke test alone is insufficient.
