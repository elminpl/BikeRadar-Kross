# Kross Echo Pro — test plan and results

## Recorded test results

The Dependabot updates in [PR #1](https://github.com/elminpl/BikeRadar-Kross/pull/1) and [PR #2](https://github.com/elminpl/BikeRadar-Kross/pull/2) were merged after review. The debug APK built with Gradle 9.9.1. Roborazzi 1.79.0 and Robolectric 4.18 passed a separate UTC verification run of 214 selected tests with the existing screenshot references. The full run reported 3,994 tests, with four failures and one skipped. A targeted run before the Gradle and test dependency update reproduced all four failures in `JournalScopeIsDisclosedTest`, `OverlayHideDoesNotReachTheAlertPathTest`, `RadarContractTest`, and `RadarStateProjectionTest`.

| Build checkpoint | Verification |
|---|---|
| Targets on the overlay | 261 selected tests passed, including 43 screenshot tests. The Kotlin decoder replayed 1,447 type 0x30 frames from two S24+ captures, with 301 frames containing active targets. |
| Connection status in Settings | 190 selected tests passed, including 113 screenshot tests and a dedicated check of Quick Status and Connections for an unbonded Kross. |
| Read-only Kross diagnostics | 252 selected tests passed, including 165 BLE controller tests and 9 screenshot tests. The harness checked that the query is opt-in and that telemetry continues while a simulated response is processed. |
| 1.12.2.k1 packaging | Release build and DEX keep verification passed. Package `es.jjrh.bikeradar.kross`, version code 45 and the existing test certificate were verified. The previous selected tests cover its app behavior. |
| Hardware-response and roadside replay follow-up | 177 selected tests passed, including 9 Debug screenshots. The BLE harness uses the captured 53-byte FFF1 reply. The decoder replays 22 FFE2 frames with relative timestamps from the two roadside sessions, including transitions to empty frames. |
| 1.12.2.k2 | 299 selected tests passed, including 113 screenshot tests. Test coverage includes Kross battery and firmware in System and Quick Status, and session cleanup on disconnect. |
| 1.12.2.k3 | 311 selected tests passed, including 90 screenshots in UTC. Tests cover capture reuse, expiry and link targets in About. Six references changed for the requested UI updates. Debug APK, ktlint, release build and DEX keep verification passed. The signed APK has package `es.jjrh.bikeradar.kross`, version code 47 and the same certificate as k2. |
| 1.12.2.k4 | 237 selected tests passed, including 16 screenshots in UTC. The BLE harness verifies the bounded retry sequence after decoded telemetry. A renderer test confirms that a decoded Kross target is drawn and cleared by the idle frame. Debug, ktlint, Release and DEX keep verification passed. Signed package `es.jjrh.bikeradar.kross` has version code 48 and the k3 certificate. |
| 1.12.2.k5 | 418 selected tests passed, including 92 screenshot tests in UTC. Ten Settings references changed for the new menu item. Tests cover capture reuse and expiry, confirmed-pass tracking, history, and the HA payload. Debug, ktlint, Release and DEX checks passed. Version code 49 and the k4 certificate were verified. |
| 1.12.2.k6 | 879 selected tests passed, including 186 screenshots in UTC. Twenty-eight references changed for the overlay and slider UI. The Kross estimate tests cover a completed estimate, a temporary missing target, and a link gap. Debug, ktlint, Release, DEX verification and the privacy script passed. Version code 50 and the k5 signing certificate were verified. |

An earlier broad test run reported four Roborazzi discrepancies. The selected runs above used a UTC JVM and kept the existing reference images. The table describes the recorded checkpoints. A full clean test suite on k1 has yet to be recorded.

## Obserwacje z testów k6

- Użytkownik zaobserwował, że bez pojazdów w zasięgu Echo Pro dość szybko zachowuje się jak uśpiony, a BikeRadar nie odzyskuje połączenia. Brak celów i uśpienie wystąpiły w tej samej próbie. Przyczyna i dokładny czas pozostają nieustalone.
- Powtórzyć próbę z BikeRadar i osobno z Cycling Radar. Zachować to samo ustawienie Auto ON/OFF i ten sam sposób wybudzenia. Odnotować ostatnią ramkę FFE2, rozłączenie, fizyczne wybudzenie, ponowne wykrycie w skanie BLE, połączenie GATT i pierwszą ramkę FFE2. Sprawdzić, czy aplikacja producenta sama wznawia skanowanie lub ponawia połączenie. Porównanie wskaże, czy szukać poprawki w wykrywaniu wybudzonego radaru, zestawianiu połączenia czy przywracaniu telemetrii.
- Użytkownik zgłosił częste rozłączenia Echo Pro podczas testów k6 na postoju. Stabilność podczas jazdy pozostaje niezweryfikowana. Porównać sesje na postoju i w ruchu przy tym samym ustawieniu Auto ON/OFF i z telefonem blisko radaru. W każdej próbie odnotować, czy radar wykrywał pojazdy. Zanotować czas i liczbę rozłączeń, ostatnią ramkę FFE2, kod GATT, przebieg ponownego łączenia oraz stan ekranu telefonu. Porównać z próbą w Cycling Radar przy tych samych warunkach. Przyczyna częstych rozłączeń pozostaje nieustalona.
- **Auto ON/OFF wyłączone.** Użytkownik zgłosił, że po wyłączeniu tej funkcji w Echo Pro BikeRadar działał bez przerw. To zalecane ustawienie do kolejnej próby stabilności na postoju. W dwóch dostarczonych logach k6 z 20 listopada 2026 r. telemetria FFE2 płynęła bez przerw w trakcie sesji: 12:10:27–12:19:04 (8 min 37 s, 3386 ramek, maks. odstęp 327 ms) oraz 14:28:53–14:33:54 (5 min 1 s, 1991 ramek, maks. odstęp 329 ms), czasy polskie. Użytkownik potwierdził, że obie sesje zakończył ręcznym wyłączeniem radaru. Po wyłączeniu każdorazowo zarejestrowano dwie nieudane próby wznowienia telemetrii (GATT 8 i 147). Stan Auto ON/OFF pochodzi z obserwacji użytkownika. Automatyczne łączenie po ręcznym włączeniu radaru sprawdzić osobno.
- **Punkt odniesienia do logów z 20 listopada 2026.** Około 11:49 czasu polskiego (10:49 UTC) użytkownik zaczął ręcznie poruszać radarem, symulując jazdę. Porównać okres postoju sprzed tej godziny z okresem symulowanego ruchu pod kątem ciągłości ramek FFE2, rozłączeń GATT i prób ponownego połączenia.
- Użytkownik widział liczbę szacowanych wyprzedzeń Krossa na ekranie głównym k6 i zgłosił brak wpisu w historii. Po ponownym połączeniu licznik pokazał poprzedni wynik 21. W k6 wartość była trzymana w pamięci procesu i nie była zapisywana w historii. Sprawdzić zapis szacunków po zakończeniu sesji i ich widoczność po ponownym uruchomieniu aplikacji.

- Użytkownik zgłosił, że po włączeniu Echo Pro przy już uruchomionej aplikacji k6 wymagane jest ręczne połączenie. Sprawdzić, czy Android zgłasza wynik skanowania BLE po włączeniu radaru, czy aplikacja go akceptuje i czy rozpoczyna próbę GATT. Zanotować, czy wcześniej użyto przycisku Rozłącz. W k6 automatyczna ścieżka czeka na wynik skanowania i respektuje ręczne rozłączenie, podczas gdy przycisk Połącz może użyć zapamiętanego radaru.

### Pakiet diagnostyczny z 20 listopada, 11:54

- Na telefonie powstał jeden plik przechwytywania (231 KB) mimo wielu prób połączenia. To potwierdza grupowanie plików w tej próbie k6.
- Po rozpoczęciu symulowanego ruchu sesja od 11:50:41 do 11:51:37 odebrała 350 powiadomień FFE2. Sesja od 11:52:02 do 11:52:48 odebrała 282. Telemetria wracała w trakcie symulowanego ruchu, a połączenie nadal się przerywało.
- Próby wznowienia po sekundzie zestawiły GATT w 26 i 33 ms, lecz po rozpoczęciu odkrywania usług zakończyły się statusem 8. Jedna kolejna próba zakończyła się statusem 147 przed zestawieniem GATT. W udanej próbie o 11:51:53 GATT zestawiono po 7912 ms, a pierwszą ramkę odebrano po 9038 ms.
- Do ustalenia pozostaje, czy strumień zamilkł przed zerwaniem połączenia i ile trwały przerwy przed 11:49. Sam pakiet diagnostyczny nie zawiera pełnych ramek. Przy analizie pliku przechwytywania sprawdzić przerwy między ramkami i wpisy o przekroczeniu limitu oczekiwania na telemetrię.

### Analiza pełnego przechwycenia k6 z 20 listopada

- Przed około 11:49 zapisano 2630 powiadomień FFE2 w pięciu sesjach. Po rozpoczęciu poruszania radarem zapisano 1033 w czterech sesjach, z których jedna odebrała pojedyncze powiadomienie. Przerwy nadal występowały. Ostatnie ramki dłuższych sesji po 11:49 zapisano o 11:50:17, 11:51:34 i 11:52:45 czasu polskiego.
- W ośmiu z dziewięciu sesji z FFE2 aplikacja zanotowała ciszę w kanale diagnostycznym. W k6 ten wpis powstaje po 3 sekundach bez poprawnej ramki już po uruchomieniu strumienia, jeśli aplikacja nie odnotowała jeszcze rozłączenia BLE. Następnie zamyka połączenie i próbuje połączyć się ponownie. Przerwy dotyczyły także odcinków z wykrytymi pojazdami. Log nie rozstrzyga, dlaczego radar przestał nadawać.
- Wszystkie 28 wznowień zapisu trafiły do tego samego pliku. Przy kolejnej próbie sprawdzić oddzielny stan braku świeżej telemetrii z krótkim dalszym oczekiwaniem na tym samym GATT oraz porównać wynik z Cycling Radar. Sprawdzić, czy strumień wraca sam i czy liczba rozłączeń spada.

## Next device checks

1. **Connection and status on S24+.** Open the Radar screen and select the Echo Pro. Check its status in Home, System, Settings → Quick Status and Settings → Connections. Expect connecting, Bluetooth connected, and limited coverage once decoded frames arrive. Confirm that Paired reflects the Android bond. Repeat after turning Bluetooth off and on, moving out of range and restarting the app.
2. **Read-only diagnostics.** The first stationary S24+ read on k1 passed. Repeat after reconnecting or with another firmware and compare the result. Enable capture logging before connecting to capture a complete FFF1 response. Check captures for identifiers before sharing them.
3. **Targets on the overlay.** Compare displayed range, threat color, audible alerts and clearing of targets against observed traffic. Record independent timestamps in a controlled setting. The existing stationary captures cover passes on both sides and support range and threat decoding.
4. **Unknown fields.** Collect a capture containing type 0x31, a controlled speed reference and a side code that varies with the setup. Record which firmware produced each capture.
5. **Garmin regression.** With a Garmin V1 or V2 unit, verify selecting the radar, reconnect, telemetry and audible alerts. Rerun relevant JVM and Roborazzi tests with `-Duser.timezone=UTC` on BLE or UI changes.

For k3, exercise the Polish diagnostics screen at a narrow width and large font. Check that the sound preview expands, capture sharing and bulk deletion remain usable, and the radar-state log shows fields for the active model. Reconnect the same radar before and after 15 seconds, then switch to a second device. Confirm the capture is reused only across the short gap for the same radar. Check that Garmin options become available again after the Kross stream ends.

Keep the original reference images for environment-only differences. Track the test results and hardware limitations for each named APK in the [Kross changelog](../../CHANGELOG-KROSS.md).

The standalone privacy shell check reports the unresolved manifest placeholder `${radarPermission}` on both k4 and k5. The MQTT disclosure contract test passes in k5.

### Weryfikacja zapisu sesji Krossa

Test automatyczny potwierdził zapis jednego wpisu po rozłączeniu, odczyt szacunku z historii i wyzerowanie licznika przy pierwszej ramce następnej sesji. Sesja bez wyprzedzeń nie tworzy wpisu. W wybranym zestawie przeszło 71 testów jednostkowych i 7 testów zrzutów. Weryfikacja na fizycznym Echo Pro pozostaje do wykonania.

### Automatyczne łączenie zapamiętanego Krossa przed k7

Skan BLE uwzględnia zapamiętany adres Krossa, więc kolejne ogłoszenie może uruchomić próbę także wtedy, gdy nie zawiera UUID usługi. Pierwsze wykrycie nadal korzysta z istniejącego rozpoznawania nazwy i usług. Ręczne rozłączenie pozostaje respektowane, a zapamiętany Kross nie zastępuje innego przypiętego radaru.

Na S24+ wyłączyć Auto ON/OFF i połączyć radar raz. Zostawić BikeRadar uruchomiony, wyłączyć radar przyciskiem, odczekać na zakończenie prób połączenia i ponownie go włączyć bez naciskania Połącz. Sprawdzić wpisy `Kross advertisement received`, `Kross GATT connected` i `Kross first decoded FFE2 frame`. Powtórzyć po ręcznym Rozłącz w aplikacji i sprawdzić, że połączenie pozostaje wstrzymane do naciśnięcia Połącz. Jeśli po włączeniu nie ma wpisu o ogłoszeniu, sprawdzić wynik skanu systemowego i adres BLE. Po zapisaniu sesji z dodatnim szacunkiem sprawdzić pojedynczy wpis w historii i zero przy pierwszej ramce kolejnego połączenia.

Cztery wcześniejsze niepowodzenia wynikały z założeń testów sprzed zmian Krossa. Audyt dziennika rozpoznaje wpisy techniczne Krossa. Test alertów uwzględnia osobną gałąź Krossa, która obsługuje dźwięk przed wyjściem z kolektora. Test IPC sprawdza warunkowy sufiks pakietu w instalacji obok oryginału i zachowanie oryginalnego identyfikatora w wydaniu domyślnym. Test telemetrii uwzględnia Krossa jako źródło bez opcjonalnych flag pomiarowych IPC.

Kompilacja debug i ktlint przeszły. Pierwszy przebieg objął 441 testów, w tym 214 testów zestawu zrzutów i nakładki w UTC. Osobny przebieg objął 171 testów kontrolera BLE, dekodera i estymatora. Oba przebiegi zakończyły się bez błędów, a obrazy wzorcowe pozostały bez zmian.

Podpisane APK k7 z commita `de2536b8` przeszło kompilację Release, kontrolę DEX (31 stałych w 9 typach enum) oraz kontrolę pakietu, kodu 51, obsługi ARM64 i zgodności certyfikatu z k6. Kopia na Dysku Google ma ten sam rozmiar co lokalne APK. Próby urządzenia pozostają otwarte.

### Wybudzanie k7 z włączonym Auto ON/OFF

Według obserwacji użytkownika Echo Pro we wszystkich trzech próbach połączył się automatycznie praktycznie od razu po wybudzeniu przy włączonym Auto ON/OFF. Przechwycenie k7 na S24+ z 22 listopada 2026 r., rozpoczęte o 17:04:18 czasu polskiego, obejmuje cztery sesje z telemetrią i trzy powroty strumienia. Pierwsze ramki po przerwach pojawiły się o 17:05:53, 17:07:04 i 17:08:30. Odstępy od ostatniej ramki poprzedniej sesji wyniosły odpowiednio 9,461 s, 11,589 s i 28,791 s. W czterech sesjach zapisano łącznie 1377 powiadomień FFE2, a maksymalny odstęp w ramach nieprzerwanej sesji wyniósł 326 ms.

Przed udanymi powrotami wystąpiły rozłączenia GATT ze statusem 8, a przed trzecim powrotem również status 147. Po ostatniej sesji zapis kończy się dwiema nieudanymi próbami. W pliku nie ma czasu fizycznego wybudzenia ani wpisu o odebraniu ogłoszenia BLE, ponieważ ten trafia do dziennika połączeń. Z samego przechwycenia nie można więc zmierzyć czasu od wybudzenia ani odróżnić udanego wznowienia z automatycznej próby GATT od połączenia wywołanego przez skan.
