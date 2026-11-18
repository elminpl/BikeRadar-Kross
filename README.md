# BikeRadar-Kross

**Rozwijana wersja aplikacji Bike Radar Overlay z obsługą radaru rowerowego Kross Echo Pro przez Bluetooth Low Energy (BLE).**

Projekt jest prywatną, niezależną modyfikacją [Bike Radar Overlay autorstwa partymola](https://github.com/partymola/android-bike-radar-overlay). Celem jest dodanie obsługi Kross Echo Pro bez pogorszenia kompatybilności z radarami Garmin. Nie jest to oficjalna aplikacja Kross ani wydanie oryginalnego autora.

> **Stan projektu: prace rozwojowe.** W eksperymentalnym APK udało się nawiązać połączenie GATT, wykonać handshake i odbierać powiadomienia BLE. Format danych o pojazdach nie został jeszcze potwierdzony, dlatego **wykrywanie pojazdów i alerty Kross nie są obecnie zweryfikowane**. Eksperymentalne modyfikacje Smali nie zostały automatycznie przeniesione do kodu źródłowego.

## Cele

- Wykrywanie i wybór radaru Kross Echo Pro.
- Stabilne połączenie BLE/GATT, subskrypcja powiadomień i diagnostyka sesji.
- Czytelne, spójne statusy: łączenie, połączenie BLE, aktywna telemetria i rozłączenie.
- Rozpoznanie formatu ramek i bezpieczne dekodowanie rzeczywistych zdarzeń.
- Docelowo: wyświetlanie pojazdów na nakładce i alerty, **dopiero po weryfikacji na sprzęcie**.
- Zachowanie istniejącej obsługi Garmin V1/V2.

## Co już ustalono

W eksperymentalnych testach zaobserwowano usługi `FFE0` / `FFF0`, charakterystyki `FFE1`, `FFE2`, `FFF1`, `FFF2` oraz powtarzającą się ramkę `30 00 55 00 00 00 FF F0`. Znaczenie tej ramki jest nieznane. Pełne informacje i ograniczenia dowodowe opisano w [dokumentacji Kross](docs/kross/).

## Dokumentacja

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

Funkcje i deklaracje dotyczące radarów Garmin w oryginalnym projekcie nie oznaczają, że analogiczne funkcje są już dostępne dla Kross Echo Pro.
