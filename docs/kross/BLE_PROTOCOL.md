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

## Observed traffic
An earlier session reportedly captured 1,441 identical notifications on `FFE2` over ~217 seconds:
```text
30 00 55 00 00 00 FF F0
```
This may be a heartbeat/status packet. **Meaning is unknown**. No validated vehicle distance, speed, count, or lateral position decoding exists.

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
