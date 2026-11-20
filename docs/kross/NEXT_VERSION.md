# Lista do następnej wersji BikeRadar-Kross

Propozycje dla wersji 1.12.2.k6. Implementacja k6 rozpoczęła się po sygnale użytkownika. Pomiary na sprzęcie pozostają otwarte.

## Priorytet

- [x] **Najnowsze poprawki partymola.** Przed rozpoczęciem k6 sprawdzić, które poprawki z oryginalnego repozytorium są nowsze od aktualnej bazy forka. Wdrożyć brakujące poprawki, zapisać ich źródło w opisie wydania i sprawdzić w testach, czy Kross Echo Pro oraz Garmin V1/V2 nadal działają poprawnie.
- [ ] **Ponowne połączenie Krossa na S24+.** Zmierzyć czas od wybudzenia do pierwszej poprawnej ramki FFE2. W logu odróżnić moment wykrycia radaru, połączenie GATT, odkrycie usług i powrót telemetrii. Dobrać poprawkę do etapu, na którym połączenie się zatrzymuje. Uwzględnić [obserwację z k6](TEST_PLAN.md#obserwacje-z-testów-k6) dotyczącą szybkiego uśpienia bez pojazdów i porównać ponowne łączenie z aplikacją producenta.
- [ ] **Stabilność połączenia Krossa.** Zbadać częste rozłączenia zgłoszone w k6 na postoju i sprawdzić, czy występują także podczas jazdy. Porównać ostatnie ramki FFE2, kody GATT i ponowne łączenie z zachowaniem aplikacji producenta. [Scenariusz testowy](TEST_PLAN.md#obserwacje-z-testów-k6).
- [ ] **Nakładka w ruchu.** Sprawdzić pozycje celów, kolory zagrożenia, dźwięk oraz znikanie pojazdów. Porównać podgląd szerokości, wysokości i przezroczystości z rzeczywistą nakładką w pionie i poziomie.
- [x] **Podgląd oryginalnej nakładki w ustawieniach.** Pokazać ten sam widok nakładki, który aplikacja wyświetla podczas jazdy, z zachowaniem jego wyglądu i położenia przy krawędzi ekranu. Zmiany szerokości, wysokości, przezroczystości i zasięgu mają być widoczne od razu.
- [x] **Delikatniejsze suwaki w całej aplikacji.** Skrócić widoczny tor i zwiększyć marginesy po obu stronach. Zastosować cieńszy tor i mniejszy uchwyt, zachowując wygodny obszar dotyku i dotychczasowe zakresy wartości. Sprawdzić wygląd na wąskich ekranach.
- [ ] **Szacowany licznik wyprzedzeń dla Krossa.** Śledzić malejącą odległość celu w kolejnych ramkach. Jeśli cel zniknie z bliskiego zasięgu przy nadal działającej telemetrii, potraktować to jako możliwe wyprzedzenie. Sprawdzić na nagraniach, jak odróżnić je od chwilowej utraty celu i uniknąć podwójnego zliczania. Wynik oznaczać jako szacowany.
- [ ] **Grupowanie logów.** Sprawdzić, czy szybkie ponowne połączenia faktycznie zmniejszają liczbę plików. Zmienić próg pięciu minut dopiero, jeśli nowe zapisy pokażą potrzebę.

## Dalsze sprawdzenia i poprawki

- [ ] **Garmin V2 i Home Assistant.** Sprawdzić licznik potwierdzonych wyprzedzeń, historię jazd i MQTT oraz działanie istniejących bliskich wyprzedzeń.
- [x] **Audyt prywatności.** Poprawić obsługę zmiennej manifestu `${radarPermission}` w skrypcie weryfikacji.
- [ ] **Dane Krossa.** Jeśli pojawią się nowe przejazdy z oznaczoną stroną i niezależnym pomiarem prędkości, wrócić do interpretacji tych pól. Dane o przejeździe przed rower mogą w przyszłości pozwolić na potwierdzanie zdarzeń.

## Badania funkcji Echo Pro

- [ ] **Szybko zbliżający się pojazd.** Porównać czerwony wskaźnik aplikacji producenta z poziomem zagrożenia w tych samych ramkach FFE2. Sprawdzić, czy istniejące czerwone oznaczenie celu w BikeRadar odpowiada temu zdarzeniu i czy jest dostatecznie czytelne. Na potrzeby szacowania wyprzedzeń nadal analizować przebieg odległości.
- [ ] **Jedna oś celów.** Porównać zachowanie nakładki z instrukcją i nagraniami przejazdów z lewej i prawej. Dla Krossa rysować cele na osi, dopóki żaden wiarygodny pomiar nie potwierdzi strony.
- [ ] **Lampka, Auto ON/OFF i STOP.** Sprawdzić na postoju odczyt ustawień przed i po zmianie trybu przyciskiem oraz przed i po przełączeniu Auto ON/OFF. Osobno sprawdzić światło STOP i odczyt stanu. Instrukcja podaje, że Echo Pro nie obsługuje menu Device Setting aplikacji producenta. Ewentualne sterowanie udostępnić po potwierdzeniu polecenia i skutku na tym modelu.
- [ ] **Firmware.** Ustalić u KROSS, czy istnieje oficjalny plik lub procedura aktualizacji dla tego modelu. W bibliotece aplikacji są ślady obsługi OTA, ale bez ustalonego źródła obrazu Echo Pro. Weryfikować zgodność z modelem i rewizją urządzenia przed próbą aktualizacji.

[Ustalenia z instrukcji i APK](BLE_PROTOCOL.md#echo-pro-manual-and-vendor-app)

## Wydanie

- [ ] Po zatwierdzeniu zakresu wykonać testy, przygotować podpisane APK, zaktualizować dokumentację i dodać zrzuty widocznych nowości.
- [ ] Dla tagu Krossa uzupełnić GitHub Releases o zmiany, wyniki, ograniczenia i link do APK.

## Wyniki prac k6

- Wdrożono eksperymentalny licznik lokalny. Odtworzenie reguły na zapisach FFE2 wskazało 5 i 2 zdarzenia w dwóch zapisach drogowych oraz 0 zdarzeń w zapisie bez celów zawierającym 1032 ramki. Dokładność w ruchu pozostaje nieznana.
- Dodano czasy zestawienia GATT i pierwszej ramki liczone od rozpoczęcia próby. Moment fizycznego wybudzenia radaru nadal trzeba odnotować na sprzęcie.

## Ocena wykonalności z 20 listopada 2026

| Punkt | Wniosek przed implementacją |
|---|---|
| Poprawki partymola | Sprawdzono aktualne `main`. Do przeniesienia są [c34efc67](https://github.com/partymola/android-bike-radar-overlay/commit/c34efc670560c2396a7f0d2bec5a81253a817d5b), który liczy początek jazdy od pierwszej ramki, oraz [a27ae3ea](https://github.com/partymola/android-bike-radar-overlay/commit/a27ae3eac65ebca28974da5838ee22ae73c52245), który usuwa powtórny alert w trakcie tego samego wyprzedzenia. Podgląd scalenia nie pokazał konfliktów tekstowych. Pierwsza poprawka dotyka statystyk używanych przez licznik k5, a druga ścieżki alertów Garmin, więc obie wymagają testów zachowania. Ostatni tag oryginału to `v1.12.2`. |
| Podgląd i suwaki | Wykonalne. Podgląd już używa `RadarOverlayView`, jak ekran jazdy. K5 skaluje jego szerokość do 60% i ogranicza wysokość do panelu 210 dp. Do wiernego podglądu potrzeba wspólnych reguł wymiaru i położenia. Suwaki Compose korzystają ze wspólnego komponentu `BrSlider`, co umożliwia zmianę wyglądu w jednym miejscu z zachowaniem obszaru dotyku. |
| Szacowanie wyprzedzeń | Wykonalne jako funkcja eksperymentalna. Próbna analiza 1447 ramek FFE2 znalazła 6 kandydatów po lewej i 2 po prawej przy co najmniej czterech próbkach, spadku odległości o co najmniej 9,375 m i zniknięciu celu w promieniu 10 m. To liczba sekwencji, których jeszcze nie dopasowano do niezależnej listy pojazdów. Sprawdzić chwilowe zaniki, kilka pojazdów naraz i ponowne użycie tego samego slotu. |
| Ponowne połączenie | Wykonalne jest rozdzielenie pomiarów skanu, połączenia GATT, odkrycia usług i pierwszej poprawnej ramki. Istniejący dziennik wskazuje jedną próbę przerwaną w trakcie odkrywania usług i drugą przed odkrywaniem. Powód opóźnienia po wybudzeniu wymaga powtarzalnych prób z S24+. |
| Nakładka w ruchu i grupowanie logów | Do sprawdzenia na sprzęcie. Kod grupuje ponowne połączenia tego samego radaru w oknie pięciu minut i otwiera nowy plik przy kolejnym połączeniu po godzinie od rozpoczęcia zapisu. Dziennik k4 potwierdza przebieg ponownych prób. Skuteczność grupowania k5 trzeba ocenić z nowych plików. |
| Garmin V2, historia i Home Assistant | Ścieżki są zaimplementowane w k5, a testy sprawdzają logikę i format MQTT. Odbiór zdarzeń przez rzeczywisty Home Assistant oraz zachowanie z fizycznym Garminem wymagają próby. |
| Audyt prywatności | Wykonalne. Skrypt czyta dosłowne `${radarPermission}` z manifestu źródłowego. Gradle przypisuje różne nazwy dla pakietu oryginalnego i Kross. Kontrola powinna rozwiązać zmienną dla sprawdzanego wariantu i sprawdzić faktyczną nazwę w tekście prywatności. |
| Kierunek, prędkość i szybkie zbliżanie | Jedna oś i czerwony stan zagrożenia są już wykonalne na potwierdzonych danych. Porównanie z aplikacją producenta wymaga wspólnego nagrania lub porównywalnych prób. Do potwierdzenia lewej/prawej strony i jednostek prędkości potrzeba niezależnych pomiarów. |
| Tryby lampki, Auto ON/OFF i STOP | Wykonalne jest porównanie odczytów przed i po zmianie ustawienia przyciskiem. Komendy BLE pozostają niepotwierdzone dla Echo Pro. STOP jest automatyczny. Instrukcja opisuje brak obsługi Device Setting przez ten model. |
| Firmware | Wersja `1.0.2` została odczytana z urządzenia. W aplikacji są ślady OTA, a oficjalnego obrazu ani procedury dla tej rewizji nie udało się ustalić. Dostępność wymaga odpowiedzi KROSS. |
| APK i GitHub Releases | Podpis, tag, wysyłka APK na Dysk oraz galeria zostały sprawdzone przy k5. Odczyt repozytorium przez REST GitHub CLI zwrócił HTTP 403 w tej sesji. Dostęp do publikacji w GitHub Releases trzeba sprawdzić przed wydaniem. |
