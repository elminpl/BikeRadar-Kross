# Kross Echo Pro — BLE observations (not a decoded protocol specification)

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

These property assignments need confirmation against actual characteristic property flags in capture or Android logs. UUIDs are abbreviated 16-bit forms.

The S24+ capture on 2026-11-18 confirmed the full UUIDs for the first channel.
The service is `aa86ffe0-3884-465c-a034-c242988b0000`, its write characteristic
is `aa86ffe1-3884-465c-a034-c242988b0000` (properties `0x0c`), and its notification
characteristic is `aa86ffe2-3884-465c-a034-c242988b0000` (properties `0x10`).
The `FFF0` service and its two characteristics use the Bluetooth base UUID.
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

## Open questions / data needed
1. Exact full UUIDs and characteristic properties, CCCD writes and subscription results.
2. Complete timestamped captures including start/stop and a real approaching vehicle.
3. Whether payload varies when vehicles approach; check differences without assuming frame format.
4. Disconnect/reconnect and watchdog behavior under loss of signal.
5. Whether `FFF0` is an alternate data path or a distinct function.

## Safety
Do not present `handshake-ok` as `vehicle detection working`. Until frames are decoded and field-validated, the app must not imply reliable traffic warnings.

## Echo Pro capture and vendor application follow-up

The ARM64 library from Radar 1.0.10 and two S24+ captures confirm that
`FFE2` carries the target stream. Type `0x30` packs four 6-bit ranges with
a scale of 3.125 m and four 2-bit threat levels. BikeRadar uses those fields.
The vendor also decodes a fifth slot from a separate `0x31` frame. The two
captures contained only `0x30`; the fifth slot's lifetime, the speed field's
units, and the direction field's mapping still need a hardware capture.

Debug → Kross Echo Pro enables one read-only probe per connection, including
an already open connection when first enabled. The probe reads Battery Level
(`180F/2A19`) and Model Number (`180A/2A24`) if readable. On the verified
command profile it sends `11 02 BA A9` to `FFF2` after subscribing to
`FFF1`. This is the vendor's configuration query. A `0x10` response with
at least 23 bytes is summarized using the vendor's field offsets. The
response layout and reported firmware revision still need confirmation on
the physical unit. The complete BLE response can be recorded with capture
logging. The Garmin settings probe remains a separate tool.
