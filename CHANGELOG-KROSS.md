# Wersje BikeRadar-Kross

Nazwa wersji składa się z wersji oryginału i numeru zmian Krossa. Po `1.12.2.k1` nastąpi `1.12.2.k2`. Przy przejściu na nowszy oryginał numer `k` zaczyna się od 1, a Androidowy `versionCode` nadal rośnie, żeby telefon przyjmował aktualizacje.

- Kod rozwijamy na gałęzi `feature/kross-echo-pro`. Commit zapisuje konkretną zmianę, a tag `kross-<wersja>` wskazuje dokładny stan kodu dla danego APK.
- Przy następnym wydaniu testowym zaktualizuj końcówkę nazwy i zwiększ kod APK Krossa w `app/build.gradle.kts`. Dodaj u góry tego pliku opis zmian i ograniczenia, a po testach oznacz commit tagiem Krossa.
- APK testowe budujemy z `-Pbikeradar.krossSideBySide` i nazywamy `BikeRadar-Kross-<wersja>.apk`. Identyfikator pakietu i klucz podpisu muszą być takie same jak w poprzednim APK Krossa.
- Tagi `kross-*` służą do oznaczania stanów kodu. Automatyczne wydania oryginału uruchamiają się po tagach `v*`.

## 1.12.2.k1

Pierwsza numerowana wersja testowa modyfikacji Krossa. Zbiera dotychczasowe zmiany z eksperymentalnych APK, które używały opisowych nazw wersji i kodu 44.

- Wykrywanie Kross Echo Pro przez BLE i wybór radaru na istniejącej liście urządzeń.
- Status połączenia Krossa na ekranie głównym, w Szybkim stanie i w Połączeniach.
- Cele, odległość i poziom zagrożenia z ramek `0x30` oraz podstawowe sygnały dźwiękowe na istniejącej nakładce.
- Opcjonalne odczyty baterii, modelu i ustawień Krossa w Diagnostyce. Narzędzia Garmina są nadal dostępne.
- Polska wersja językowa.

Kod APK Krossa to **45**. Na fizycznym radarze nadal trzeba sprawdzić odczyt ustawień i działanie alertów podczas jazdy. Prędkość, strona mijania i piąty cel wymagają dodatkowych pomiarów.
