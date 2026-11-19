# BikeRadar-Kross

**Rozwijana wersja aplikacji Bike Radar Overlay z obsługą radaru rowerowego Kross Echo Pro przez Bluetooth Low Energy (BLE).**

Projekt jest prywatną, niezależną modyfikacją [Bike Radar Overlay autorstwa partymola](https://github.com/partymola/android-bike-radar-overlay). Celem jest dodanie obsługi Kross Echo Pro bez pogorszenia kompatybilności z radarami Garmin. Nie jest to oficjalna aplikacja Kross ani wydanie oryginalnego autora.

> **Stan projektu.** Wersja testowa `1.12.2.k1` odbiera dane Kross Echo Pro i pokazuje odległość oraz poziom zagrożenia. Odczyty prędkości, strony mijania i zachowanie podczas jazdy wymagają dalszych testów na sprzęcie.

## Cele

- Wykrywanie i wybór radaru Kross Echo Pro.
- Stabilne połączenie BLE/GATT, subskrypcja powiadomień i diagnostyka sesji.
- Czytelne, spójne statusy: łączenie, połączenie BLE, aktywna telemetria i rozłączenie.
- Rozpoznanie formatu ramek i bezpieczne dekodowanie rzeczywistych zdarzeń.
- Docelowo: wyświetlanie pojazdów na nakładce i alerty, **dopiero po weryfikacji na sprzęcie**.
- Zachowanie istniejącej obsługi Garmin V1/V2.

## Co już ustalono

Aplikacja wykrywa Kross Echo Pro, pokazuje stan połączenia i dekoduje odległość oraz zagrożenie z potwierdzonych ramek. Menu Diagnostyka zawiera próbny odczyt danych urządzenia. Szczegóły opisuje [dokumentacja BLE](docs/kross/BLE_PROTOCOL.md).

## Dokumentacja

- [Wersje Kross i lista zmian](CHANGELOG-KROSS.md)
- [Historia projektu i przekazanie kontekstu](docs/kross/PROJECT_HISTORY.md)
- [Obserwacje protokołu BLE](docs/kross/BLE_PROTOCOL.md)
- [Znane problemy i regresje](docs/kross/KNOWN_ISSUES.md)
- [Plan testów](docs/kross/TEST_PLAN.md)
- [Instrukcje dla Codexa i agentów](AGENTS.md)

## Organizacja prac

- `main` — punkt odniesienia oparty na oryginalnym projekcie.
- `feature/kross-echo-pro` — dokumentacja i rozwój obsługi Kross.

Kod Android jest rozwijany i testowany w Codexie. Kompilacja debug APK i testy jednostkowe są dostępne. Testy zrzutów ekranu wymagają uruchomienia JVM z `-Duser.timezone=UTC`, aby odpowiadały wzorcom.

## Pochodzenie i licencja

Oparty na [partymola/android-bike-radar-overlay](https://github.com/partymola/android-bike-radar-overlay). Prawa autorskie do oryginalnego kodu należą do jego autorów. Zachowujemy historię Git, informacje o autorach i [licencję GNU GPL v3 lub nowszą](LICENSE). Zmiany BikeRadar-Kross są niezależnym rozwinięciem projektu.

Sześć plików kontraktu IPC ma licencję [Apache-2.0](LICENSES/Apache-2.0.txt):
`IRadarService.aidl`, `IRadarListener.aidl`, `RadarStateParcel.aidl`,
`RadarContract.kt`, `RadarStateParcel.kt` i `RadarVehicleParcel.kt`.
Zasady dotyczące aplikacji korzystających wyłącznie z tego interfejsu
określa [dodatkowe zezwolenie GPL](additional-permission.txt).

Funkcje i deklaracje dotyczące radarów Garmin w oryginalnym projekcie nie oznaczają, że analogiczne funkcje są już dostępne dla Kross Echo Pro.
