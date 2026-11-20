# Lista do następnej wersji BikeRadar-Kross

Propozycje dla wersji 1.12.2.k6. Implementację i kompilację zaczniemy po sygnale użytkownika.

## Priorytet

- [ ] **Ponowne połączenie Krossa na S24+.** Zmierzyć czas od wybudzenia do pierwszej poprawnej ramki FFE2. W logu odróżnić moment wykrycia radaru, połączenie GATT, odkrycie usług i powrót telemetrii. Dobrać ewentualną poprawkę do etapu, który powoduje zwłokę.
- [ ] **Nakładka w ruchu.** Sprawdzić pozycje celów, kolory zagrożenia, dźwięk oraz znikanie pojazdów. Porównać podgląd szerokości, wysokości i przezroczystości z rzeczywistą nakładką w pionie i poziomie.
- [ ] **Grupowanie logów.** Sprawdzić, czy szybkie ponowne połączenia faktycznie zmniejszają liczbę plików. Zmienić próg pięciu minut dopiero, jeśli nowe zapisy pokażą potrzebę.

## Dalsze sprawdzenia i poprawki

- [ ] **Garmin V2 i Home Assistant.** Sprawdzić licznik potwierdzonych wyprzedzeń, historię jazd i MQTT oraz działanie istniejących bliskich wyprzedzeń.
- [ ] **Audyt prywatności.** Poprawić obsługę zmiennej manifestu `${radarPermission}` w skrypcie weryfikacji.
- [ ] **Dane Krossa.** Jeśli pojawią się nowe przejazdy z oznaczoną stroną i niezależnym pomiarem prędkości, wrócić do interpretacji tych pól. Potwierdzenie przejazdu przed rower jest warunkiem licznika potwierdzonych wyprzedzeń dla Krossa.

## Wydanie

- [ ] Po zatwierdzeniu zakresu wykonać testy, przygotować podpisane APK, zaktualizować dokumentację i dodać zrzuty widocznych nowości.
- [ ] Dla tagu Krossa uzupełnić GitHub Releases o zmiany, wyniki, ograniczenia i link do APK.
