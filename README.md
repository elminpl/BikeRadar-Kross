# BikeRadar-Kross

[![CI](https://github.com/elminpl/BikeRadar-Kross/actions/workflows/ci.yml/badge.svg)](https://github.com/elminpl/BikeRadar-Kross/actions/workflows/ci.yml)
[![Test release](https://img.shields.io/badge/pre--release-1.12.2.k7-blue)](CHANGELOG-KROSS.md#1122k7)
[![License](https://img.shields.io/badge/license-GPL--3.0--or--later-blue)](https://github.com/elminpl/BikeRadar-Kross/blob/feature/kross-echo-pro/LICENSE)
![Android 12+](https://img.shields.io/badge/android-12%2B-brightgreen)

[English](#english) · [Polski](#polski)

## English

**An independent fork of [Bike Radar Overlay by partymola](https://github.com/partymola/android-bike-radar-overlay) with experimental Bluetooth Low Energy (BLE) support for the Kross Echo Pro.** The project aims to retain existing Garmin V1/V2 support.

> **Kross Echo Pro setup.** Disable **Auto ON/OFF** on the radar and use manual mode. With Auto ON/OFF disabled, telemetry continued until the radar was manually turned off in both Galaxy S24+ tests. Automatic reconnection after power-on is ready for hardware testing in k7. [Kross manual](https://kross.pl/media/akeneo_connector/asset_files/e/c/echo_pro_instrukcja_obs__ugi_858f.pdf) · [Test results](docs/kross/TEST_PLAN.md)

### Project status

Test release `1.12.2.k7` discovers the Echo Pro, shows its connection status, and displays target distance and threat level. System and Quick Status also show battery level and radar firmware. Stationary tests confirmed configuration and firmware readback. Speed, passing side, and alerts while riding still need hardware validation.

The Debug screen includes a device readout. Capture logging groups quick reconnects to the same radar into one file. The development goals include reliable BLE/GATT connections and notification subscriptions, clear connection and telemetry states, validated frame decoding, and on-road verification of the overlay and alerts.

### Documentation

- [Kross versions and changelog](CHANGELOG-KROSS.md)
- [Next version plan](docs/kross/NEXT_VERSION.md)
- [Feature screenshots](docs/kross/SCREENSHOTS.md)
- [Project history and APK archive](docs/kross/PROJECT_HISTORY.md)
- [BLE protocol findings](docs/kross/BLE_PROTOCOL.md)
- [Known issues and regressions](docs/kross/KNOWN_ISSUES.md)
- [Test plan](docs/kross/TEST_PLAN.md)
- [Contributor and agent instructions](AGENTS.md)

### Development

The [Kross upstream update workflow](.github/workflows/kross-upstream.yml) checks for new stable upstream versions and prepares updates for review. The `main` branch tracks the upstream baseline. Documentation and Kross development are on `feature/kross-echo-pro`.

Android development and testing take place in Codex. Debug APK builds and unit tests are available. Screenshot tests use `-Duser.timezone=UTC` to match the reference images.

### Attribution and license

The original authors retain copyright in their code. This independent fork preserves the upstream Git history, attribution, and [GNU GPL v3 or later license](LICENSE). The six IPC contract files use [Apache-2.0](LICENSES/Apache-2.0.txt). The [additional GPL permission](additional-permission.txt) covers apps that use only that interface. Consult the project status and known issues above for which Garmin features are also supported on the Echo Pro.

## Polski

**Rozwijana wersja aplikacji Bike Radar Overlay z obsługą radaru rowerowego Kross Echo Pro przez Bluetooth Low Energy (BLE).**

Projekt jest prywatną, niezależną modyfikacją [Bike Radar Overlay autorstwa partymola](https://github.com/partymola/android-bike-radar-overlay). Celem jest dodanie obsługi Kross Echo Pro bez pogorszenia kompatybilności z radarami Garmin. Nie jest to oficjalna aplikacja Kross ani wydanie oryginalnego autora.

> **Konfiguracja Kross Echo Pro.** Wyłącz **Auto ON/OFF** w radarze i używaj trybu ręcznego. Przy wyłączonej funkcji w obu próbach na Galaxy S24+ dane napływały aż do ręcznego wyłączenia radaru. Automatyczne połączenie po ponownym włączeniu radaru w k7 wymaga próby na sprzęcie. [Instrukcja Kross](https://kross.pl/media/akeneo_connector/asset_files/e/c/echo_pro_instrukcja_obs__ugi_858f.pdf) · [Wyniki testów](docs/kross/TEST_PLAN.md)

> **Stan projektu.** Wersja testowa `1.12.2.k7` odbiera dane Kross Echo Pro i pokazuje odległość oraz poziom zagrożenia. Karta System i Szybki stan pokazują także baterię oraz wersję oprogramowania radaru. Dwa nagrania z S24+ potwierdzają dekodowanie odległości i zagrożenia. Na postoju potwierdzono też odczyt ustawień i firmware. Prędkość, strona mijania i działanie alertów podczas jazdy wymagają dalszych prób na sprzęcie.

### Cele

- Wykrywanie i wybór radaru Kross Echo Pro.
- Stabilne połączenie BLE/GATT, subskrypcja powiadomień i diagnostyka sesji.
- Czytelne, spójne statusy: łączenie, połączenie BLE, aktywna telemetria i rozłączenie.
- Rozpoznanie formatu ramek i bezpieczne dekodowanie rzeczywistych zdarzeń.
- Weryfikacja nakładki i alertów podczas jazdy.
- Zachowanie istniejącej obsługi Garmin V1/V2.

### Co już ustalono

Aplikacja wykrywa Kross Echo Pro, pokazuje stan połączenia i dekoduje odległość oraz zagrożenie z potwierdzonych ramek. Menu Diagnostyka zawiera próbny odczyt danych urządzenia. Dziennik przechwytywania łączy szybkie ponowne połączenia tego samego radaru w jeden plik. Szczegóły opisuje [dokumentacja BLE](docs/kross/BLE_PROTOCOL.md).

### Dokumentacja

- [Wersje Kross i lista zmian](CHANGELOG-KROSS.md)
- [Plan następnej wersji](docs/kross/NEXT_VERSION.md)
- [Zrzuty nowych funkcji](docs/kross/SCREENSHOTS.md)
- [Historia projektu i archiwum APK](docs/kross/PROJECT_HISTORY.md)
- [Obserwacje protokołu BLE](docs/kross/BLE_PROTOCOL.md)
- [Znane problemy i regresje](docs/kross/KNOWN_ISSUES.md)
- [Plan testów](docs/kross/TEST_PLAN.md)
- [Instrukcje dla Codexa i agentów](AGENTS.md)

### Organizacja prac

Workflow [Kross upstream update](.github/workflows/kross-upstream.yml) sprawdza nowe stabilne wersje oryginału i przygotowuje aktualizację do przeglądu.

- `main` — punkt odniesienia oparty na oryginalnym projekcie.
- `feature/kross-echo-pro` — dokumentacja i rozwój obsługi Kross.

Kod Android jest rozwijany i testowany w Codexie. Kompilacja debug APK i testy jednostkowe są dostępne. Testy zrzutów ekranu wymagają uruchomienia JVM z `-Duser.timezone=UTC`, aby odpowiadały wzorcom.

### Pochodzenie i licencja

Oparty na [partymola/android-bike-radar-overlay](https://github.com/partymola/android-bike-radar-overlay). Prawa autorskie do oryginalnego kodu należą do jego autorów. Zachowujemy historię Git, informacje o autorach i [licencję GNU GPL v3 lub nowszą](LICENSE). Zmiany BikeRadar-Kross są niezależnym rozwinięciem projektu.

Sześć plików kontraktu IPC ma licencję [Apache-2.0](LICENSES/Apache-2.0.txt):
`IRadarService.aidl`, `IRadarListener.aidl`, `RadarStateParcel.aidl`,
`RadarContract.kt`, `RadarStateParcel.kt` i `RadarVehicleParcel.kt`.
Zasady dotyczące aplikacji korzystających wyłącznie z tego interfejsu
określa [dodatkowe zezwolenie GPL](additional-permission.txt).

Funkcje i deklaracje dotyczące radarów Garmin w oryginalnym projekcie nie oznaczają, że analogiczne funkcje są już dostępne dla Kross Echo Pro.
