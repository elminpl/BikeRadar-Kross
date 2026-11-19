# Kross Echo Pro — historia projektu

Projekt startował od prób z APK oryginału 1.11.0. Obsługa rozwijana w Kotlinie na gałęzi `feature/kross-echo-pro` bazuje na Bike Radar Overlay 1.12.2.

## Etapy rozwoju

| Etap | Zmiana |
|---:|---|
| 1 | Próby Smali z połączeniem, śladem GATT i stanem interfejsu na bazie 1.11.0. UIState był kandydatem do powrotu. |
| 2 | Pierwsze kompilacje Kotlin 1.12.2. Oddzielny pakiet testowy na S24+, skan BLE i diagnostyka GATT. |
| 3 | Wybór Krossa z istniejącej listy i pierwsze statusy po rozpoznaniu jego profilu. |
| 4 | Subskrypcja FFE2 oraz FFF1 i rozróżnienie kanałów w logach. |
| 5 | Polska wersja językowa i poprawki zapisu sesji do analizy ramek. |
| 6 | Odległość i zagrożenie z typu 0x30, cele na dotychczasowej nakładce i podstawowy dźwięk. |
| 7 | Spójny status w Szybkim stanie i Połączeniach. Etykieta sparowania odpowiada powiązaniu urządzenia w Androidzie. |
| 8 | Próbny odczyt baterii, modelu, ustawień i firmware w Diagnostyce. Ten stan funkcji trafił do pierwszego numerowanego APK `1.12.2.k1`. |

Przy liczeniu kolejnych zmian funkcjonalnych wychodzi **8 etapów**, w tym **7 na bazie 1.12.2**. Retrospektywnie odpowiadałoby to `1.12.2.k7`, gdyby numerację uruchomiono na początku prac w Kotlinie. Oficjalna nazwa udostępnionego APK to `1.12.2.k1`. Kompilacje rozwijające tę samą funkcję i nadanie numeru istniejącemu zestawowi funkcji liczą się w ramach tego samego etapu.

## Pliki APK

Daty oznaczają zapis na Dysku Google w strefie Europe/Warsaw. W katalogu projektu było **19 plików BikeRadar-Kross**. Manifesty 18 z nich sprawdzono bezpośrednio. W przypadku dużego pliku debug dostępne były metadane katalogu. Osobne pliki aplikacji producenta nie wchodzą w ten wykaz.

| Data | Plik | Wersja i kod z manifestu | Etap |
|---|---|---|---:|
| 2026-11-17 | `BikeRadar_Kross_JobTrace.apk` | `1.11.0` (41) | 1 |
| 2026-11-17 | `BikeRadar_Kross_ConnectTrace.apk` | `1.11.0` (41) | 1 |
| 2026-11-17 | `BikeRadar_Kross_GattReturn.apk` | `1.11.0` (41) | 1 |
| 2026-11-17 | `BikeRadar_Kross_NoResumeFlood.apk` | `1.11.0` (41) | 1 |
| 2026-11-17 | `BikeRadar_Kross_UIState.apk` | `1.11.0` (41) | 1 |
| 2026-11-18 | `BikeRadar-Kross-debug-2026-11-18.apk` | Manifest niezweryfikowany | 2 |
| 2026-11-18 | `BikeRadar-Kross-1.12.2-test-S24.apk` | `1.12.2-kross-test` (44) | 2 |
| 2026-11-18 | `BikeRadar-Kross-1.12.2-S24-skan-BLE.apk` | `1.12.2-kross-test` (44) | 2 |
| 2026-11-18 | `BikeRadar-Kross-1.12.2-S24-diagnostyka-GATT.apk` | `1.12.2-kross-test` (44) | 2 |
| 2026-11-18 | `BikeRadar-Kross-1.12.2-S24-wybor-radaru.apk` | `1.12.2-kross-test-chooser` (44) | 3 |
| 2026-11-18 | `BikeRadar-Kross-1.12.2-S24-profil-i-wybor.apk` | `1.12.2-kross-test-profile` (44) | 3 |
| 2026-11-18 | `BikeRadar-Kross-1.12.2-S24-status-i-lista.apk` | `1.12.2-kross-test-status` (44) | 3 |
| 2026-11-18 | `BikeRadar-Kross-1.12.2-S24-FFE2-FFF1.apk` | `1.12.2-kross-test-ffe2-fff1` (44) | 4 |
| 2026-11-18 | `BikeRadar-Kross-1.12.2-S24-FFE2-FFF1-PL.apk` | `1.12.2-kross-test-ffe2-fff1-pl` (44) | 5 |
| 2026-11-19 | `BikeRadar-Kross-1.12.2-S24-logi-PL.apk` | `1.12.2-kross-test-log-pl` (44) | 5 |
| 2026-11-19 | `BikeRadar-Kross-1.12.2-S24-cele-PL.apk` | `1.12.2-kross-test-targets-pl` (44) | 6 |
| 2026-11-19 | `BikeRadar-Kross-1.12.2-S24-status-polaczenia-PL.apk` | `1.12.2-kross-test-link-ui-pl` (44) | 7 |
| 2026-11-19 | `BikeRadar-Kross-1.12.2-S24-diagnostyka-Kross-PL.apk` | `1.12.2-kross-test-diagnostics-pl` (44) | 8 |
| 2026-11-19 | `BikeRadar-Kross-1.12.2.k1.apk` | `1.12.2.k1` (45) | 8 |


Wczesne pliki Smali miały pakiet `es.jjrh.bikeradar`. Sprawdzone kompilacje Kotlin używały osobnego pakietu `es.jjrh.bikeradar.kross`. Trzy pierwsze testowe APK na S24+ zgłaszały tę samą wersję aplikacji, ale miały różne sumy SHA-256.

Najnowszy znaleziony plik testowy to `1.12.2.k1`, zbudowany z commita `7435a067` i oznaczony tagiem `kross-1.12.2.k1`. Późniejsze zmiany na gałęzi rozwojowej dotyczyły automatyzacji GitHub Actions, konfiguracji numeracji i instrukcji projektu. Po tym tagu nie znaleziono nowszego udostępnionego APK w sprawdzonym katalogu ani następnego tagu Krossa w repozytorium.

## Wnioski z prób

W eksperymentalnych wersjach powtarzający się wpis `connectAndRun entered` był powiązany prawdopodobnie ze wznowieniami korutyny. Do oceny ponownych połączeń używamy rzeczywistych zdarzeń GATT. W interfejsie rozróżniamy połączenie Bluetooth, świeże dane i wykrycie celu. Szczegóły oraz otwarte pytania są w [opisie BLE](BLE_PROTOCOL.md), [liście problemów](KNOWN_ISSUES.md) i [planie testów](TEST_PLAN.md).
