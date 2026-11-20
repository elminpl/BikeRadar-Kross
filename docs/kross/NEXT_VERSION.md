# Lista do następnej wersji BikeRadar-Kross

Propozycje dla wersji 1.12.2.k6. Implementację i kompilację zaczniemy po sygnale użytkownika.

## Priorytet

- [ ] **Najnowsze poprawki partymola.** Przed rozpoczęciem k6 sprawdzić, które poprawki z oryginalnego repozytorium są nowsze od aktualnej bazy forka. Wdrożyć brakujące poprawki, zapisać ich źródło w opisie wydania i sprawdzić w testach, czy Kross Echo Pro oraz Garmin V1/V2 nadal działają poprawnie.
- [ ] **Ponowne połączenie Krossa na S24+.** Zmierzyć czas od wybudzenia do pierwszej poprawnej ramki FFE2. W logu odróżnić moment wykrycia radaru, połączenie GATT, odkrycie usług i powrót telemetrii. Dobrać ewentualną poprawkę do etapu, który powoduje zwłokę.
- [ ] **Nakładka w ruchu.** Sprawdzić pozycje celów, kolory zagrożenia, dźwięk oraz znikanie pojazdów. Porównać podgląd szerokości, wysokości i przezroczystości z rzeczywistą nakładką w pionie i poziomie.
- [ ] **Podgląd oryginalnej nakładki w ustawieniach.** Pokazać ten sam widok nakładki, który aplikacja wyświetla podczas jazdy, z zachowaniem jego wyglądu i położenia przy krawędzi ekranu. Zmiany szerokości, wysokości, przezroczystości i zasięgu mają być widoczne od razu.
- [ ] **Delikatniejsze suwaki w całej aplikacji.** Skrócić widoczny tor i zwiększyć marginesy po obu stronach. Zastosować cieńszy tor i mniejszy uchwyt, zachowując wygodny obszar dotyku i dotychczasowe zakresy wartości. Sprawdzić wygląd na wąskich ekranach.
- [ ] **Szacowany licznik wyprzedzeń dla Krossa.** Śledzić malejącą odległość celu w kolejnych ramkach. Jeśli cel zniknie z bliskiego zasięgu przy nadal działającej telemetrii, potraktować to jako możliwe wyprzedzenie. Sprawdzić na nagraniach, jak odróżnić je od chwilowej utraty celu i uniknąć podwójnego zliczania. Wynik oznaczać jako szacowany.
- [ ] **Grupowanie logów.** Sprawdzić, czy szybkie ponowne połączenia faktycznie zmniejszają liczbę plików. Zmienić próg pięciu minut dopiero, jeśli nowe zapisy pokażą potrzebę.

## Dalsze sprawdzenia i poprawki

- [ ] **Garmin V2 i Home Assistant.** Sprawdzić licznik potwierdzonych wyprzedzeń, historię jazd i MQTT oraz działanie istniejących bliskich wyprzedzeń.
- [ ] **Audyt prywatności.** Poprawić obsługę zmiennej manifestu `${radarPermission}` w skrypcie weryfikacji.
- [ ] **Dane Krossa.** Jeśli pojawią się nowe przejazdy z oznaczoną stroną i niezależnym pomiarem prędkości, wrócić do interpretacji tych pól. Dane o przejeździe przed rower mogą w przyszłości pozwolić na potwierdzanie zdarzeń.

## Wydanie

- [ ] Po zatwierdzeniu zakresu wykonać testy, przygotować podpisane APK, zaktualizować dokumentację i dodać zrzuty widocznych nowości.
- [ ] Dla tagu Krossa uzupełnić GitHub Releases o zmiany, wyniki, ograniczenia i link do APK.
