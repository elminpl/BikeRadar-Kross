# Kross Echo Pro — BLE observations and decoding

## Observed GATT
A user diagnostic preference `radar_link_probe` reported:
```text
svc=1801[2a05,2b29,2b2a] 1800[2a00,2a01,2a04] 180f[2a19]
ffe0[ffe1,ffe2] 180a[2a24,2a29] fff0[fff2,fff1] dc1d[7828]
out=handshake-ok
```
Previous experimental implementation treated:
- `FFE0`: `FFE1` as WRITE, `FFE2` as NOTIFY.
- `FFF0`: `FFF2` as WRITE, `FFF1` as NOTIFY.

UUIDs above use abbreviated 16-bit forms. The S24+ GATT capture confirmed the properties listed below.

The S24+ capture on 2026-11-18 confirmed the full UUIDs for the first channel.
The service is `aa86ffe0-3884-465c-a034-c242988b0000`, its write characteristic
is `aa86ffe1-3884-465c-a034-c242988b0000` (properties `0x0c`), and its notification
characteristic is `aa86ffe2-3884-465c-a034-c242988b0000` (properties `0x10`).
The `FFF0` service and its two characteristics use the Bluetooth base UUID. The S24+ reported `FFF2` as write without response (`0x04`) and `FFF1` as notify (`0x10`).
Later S24+ captures confirmed service discovery and a working FFE2 subscription.

## Observed traffic
An earlier session reportedly captured 1,441 identical notifications on `FFE2` over ~217 seconds:
```text
30 00 55 00 00 00 FF F0
```
The vendor parser and later S24+ captures identify this as an empty target frame. The later captures also support range and threat decoding (see below).

## Connection evidence
2026-11-17 19:25:52 journal included:
```text
radar conn state status=0 newState=2
radar connected, running handshake
radar handshake complete
KROSS UI TRACE: markConnected after successful handshake
KROSS JOB TRACE: Garmin V2 watchdog bypassed for Kross GATT
```
A later session showed `connectAndRun entered` repeatedly; this was likely inflated by coroutine resume logging, not proof of multiple connections.

## Echo Pro capture and vendor application follow-up

The ARM64 library from Radar 1.0.10 and two S24+ captures confirm that
`FFE2` carries the target stream. Type `0x30` packs four 6-bit ranges with
a scale of 3.125 m and four 2-bit threat levels. BikeRadar uses those fields.
The vendor also decodes a fifth slot from a separate `0x31` frame. The two
captures contained only `0x30`. The fifth slot's lifetime, the speed field's
units, and the direction field's mapping still need a hardware capture.

In k1, Debug → Kross Echo Pro enables one read-only probe per connection.
In k2, the probe runs automatically after the Kross subscriptions, and the app
refreshes the battery reading every 20 seconds while connected. Debug records
the result and requests an extra reading if enabled during the connection. The probe reads Battery Level
(`180F/2A19`) and Model Number (`180A/2A24`) if readable. On the verified
command profile it sends `11 02 BA A9` to `FFF2` after subscribing to
`FFF1`. This is the vendor's configuration query. A `0x10` response with
at least 23 bytes is summarized using the vendor's field offsets. The
Echo Pro response was confirmed on a physical unit with version 1.12.2.k1.
Capture logging can record the full response. The Garmin settings probe remains a separate tool.

## Evidence from stationary roadside captures

| Side the vehicles passed on | Type 0x30 frames | Frames with active targets | Active target entries |
|---|---:|---:|---:|
| Left | 987 | 214 | 257 |
| Right | 460 | 87 | 87 |

Every active entry had `threatSide = 0`. The manufacturer stores the field,
while its target view draws distance without using that field. The binary
stores `speed` as the packed 4-bit value times 3.04. In one right-side
sequence the change in decoded range corresponded to about 41.4 km/h,
while the speed code stayed at 3. Assuming m/s would give 32.8 km/h,
so the scale alone is insufficient to assign a unit. BikeRadar uses decoded
distance and the reported threat level for this profile.

The application expects a verified Kross GATT profile before using its
diagnostic query. The response parser is tested with a reply captured from
the physical unit. See the [test plan](TEST_PLAN.md) for the remaining hardware checks.


## Physical configuration read on S24+

In one stationary session on 1.12.2.k1, the device reported 100% via
`180F/2A19` and model `pan1080xb1` via `180A/2A24`. The negotiated ATT
MTU was 252. The reply on FFF1 was 53 bytes with header `10 33 BA`,
followed by mode 1, brightness 70 and charging setting 0. At offset 23
it reported `VER:1.0.2 Apr 10 2023 10:55:40`.

The capture held 351 empty FFE2 frames of type 0x30 and one FFF1 reply. FFE2
frames occurred 93 ms before and 66 ms after the reply. This confirms
the read and concurrent telemetry for this session. The meaning of each
mode and the effect of the charging setting still require device checks.


## Checking lamp brightness

Radar 1.0.10 writes decimal 70 for the first brightness choice and 99
for the second. Its readback classifies values above 89 as the second
choice. The physical k1 capture confirms 70. To verify 99, change only
brightness in the manufacturer's app on a stationary radar, close that app,
then reconnect BikeRadar with the Kross diagnostic reading enabled. Compare
the two readings and restore the original setting. Treat the numbers as
configuration codes until light output is independently measured.

The settings reply also exposes the selected operating mode and whether
operation while charging is enabled. Neither field measures the current
physical state of the lamp or charger. Other bytes in the captured reply
remain undocumented.


## Echo Pro manual and vendor app

The [KROSS manual](https://kross.pl/media/akeneo_connector/asset_files/e/c/echo_pro_instrukcja_obs__ugi_858f.pdf#page=2) describes the green status as no approaching target, amber as an approaching vehicle and red as a fast-approaching vehicle. Its application illustration shows the vehicle marker moving along one axis. This fits the app's distance-only target placement and our roadside captures with zero side codes. BikeRadar already renders threat level 2 in red and other decoded targets as a warning. A comparison against the vendor display is needed to verify the mapping on the same traffic event. The manual gives no numeric speed threshold.

The manual describes switching the lighting mode with the lamp button and toggling manual/Auto ON/OFF with a double press. It expressly excludes Device Setting support for this model in the vendor app. The light's own braking sensor triggers STOP for about three seconds at 50 lm. Radar 1.0.10 contains settings-write and lighting-mode code, and Echo Pro responds to the read-only configuration query. Settings-write support for this model, a separate Auto ON/OFF write, and STOP control or reporting over BLE remain unverified.

The vendor ARM64 library contains OTA header, transfer and verification messages. The inspected ARM64 APK split contains the library and no separate firmware asset. The app strings and official product downloads examined did not establish a firmware download URL or an official Echo Pro image. The observed device firmware is 1.0.2. Ask [KROSS support](https://kross.pl/kontakt) for the supported update procedure and exact hardware compatibility.
