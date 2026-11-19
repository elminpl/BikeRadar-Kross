# Wersje BikeRadar-Kross

Nazwa wersji składa się z wersji oryginału i numeru zmian Krossa. Po `1.12.2.k1` nastąpi `1.12.2.k2`. Przy przejściu na nowszy oryginał numer `k` zaczyna się od 1, a Androidowy `versionCode` nadal rośnie, żeby telefon przyjmował aktualizacje.

- Kod rozwijamy na gałęzi `feature/kross-echo-pro`. Commit zapisuje konkretną zmianę, a tag `kross-<wersja>` wskazuje dokładny stan kodu dla danego APK.
- Przy następnym wydaniu testowym zwiększ `revision` oraz `androidVersionCode` w `gradle/kross-version.properties`. Dodaj u góry tego pliku opis zmian i ograniczenia, a po testach oznacz commit tagiem Krossa.
- APK testowe budujemy z `-Pbikeradar.krossSideBySide` i nazywamy `BikeRadar-Kross-<wersja>.apk`. Identyfikator pakietu i klucz podpisu muszą być takie same jak w poprzednim APK Krossa.
- Tagi `kross-*` służą do oznaczania stanów kodu. Automatyczne wydania oryginału uruchamiają się po tagach `v*`.

## Aktualizacje oryginału

Workflow [Kross upstream update](.github/workflows/kross-upstream.yml) codziennie sprawdza stabilne tagi oryginału. Można go też uruchomić z zakładki **Actions**. Opcja **Test the current Kross branch or an existing update proposal** uruchamia testy także wtedy, gdy brakuje nowej wersji.

Dla nowszego tagu workflow przygotowuje osobną gałąź, ustawia `k1` i kolejny kod APK oraz otwiera roboczy pull request do gałęzi Krossa. Testy i raporty są w uruchomieniu podlinkowanym w PR. Konflikt zatrzymuje aktualizację i trafia do podsumowania zadania. Po przeglądzie i próbie na radarze można uzupełnić listę zmian, scalić PR i oznaczyć wersję tagiem Krossa.

GitHub wymaga uprawnienia **Allow GitHub Actions to create and approve pull requests** do otwarcia PR przez `GITHUB_TOKEN`. Podsumowanie wskazuje też porównanie gałęzi, z którego można otworzyć PR ręcznie.

## Historia przed numeracją

Archiwum obejmuje 19 plików APK i 8 etapów funkcjonalnych, z czego 7 przypada na bazę 1.12.2. Pierwsza numerowana wersja `1.12.2.k1` obejmuje stan z ostatniego etapu. [Pełny wykaz APK i wersji z manifestów](docs/kross/PROJECT_HISTORY.md#pliki-apk)

Po `k1` dodano automatyzację aktualizacji oryginału i zapisano procedurę aktualizowania GitHuba po kompilacji. Kolejna wersja aplikacji na bazie 1.12.2 otrzymała numer `k2`.

## 1.12.2.k5

- Dziennik przechwytywania dopisuje powroty tego samego radaru w ciągu pięciu minut. Po godzinie kolejne połączenie zaczyna plik od nowa. Dłuższa ciągła sesja zachowuje jeden plik.
- Dziennik stanu w Diagnostyce trzyma 100 najnowszych wpisów w pamięci ekranu.
- Ustawienia jazdy mają podmenu nakładki z podglądem szerokości, wysokości, przezroczystości i zasięgu.
- Opcjonalny licznik potwierdzonych wyprzedzeń Garmina V2 trafia do historii jazd, podsumowania i zdarzeń MQTT. Kross zgłasza odległość i zagrożenie. Jego ramki nie potwierdzają przejścia pojazdu przed rower.
- Kod APK to **49**.

## 1.12.2.k4

- Nagłówek podglądu sygnałów ma duże litery i odstęp zgodny z pozostałymi sekcjami Diagnostyki. Tekst nadal pochodzi z zasobów językowych.
- Po utracie działającej transmisji Krossa wykrytego przez skan BLE aplikacja robi maksymalnie dwie dodatkowe próby połączenia. Pierwsza rusza po sekundzie, druga trzy sekundy po nieudanej próbie. Potem wykrywanie znów zależy od skanowania BLE.
- Z potwierdzonych ramek Krossa nakładka pokazuje odległość i zagrożenie. Cele o nieznanej stronie rysuje na osi. Prognoza ruchu jest wyszarzona przy Krossie, ponieważ wymaga prędkości celu. Licznik bliskich minięć nadal wymaga dodatkowo zmierzonego odstępu bocznego i prędkości rowerzysty.
- Przeszło 237 wybranych testów, w tym 16 testów zrzutów ekranu w UTC. Podpis zweryfikowany. Kod APK to **48**. Czas ponownego połączenia i prezentacja na telefonie czekają na próbę z fizycznym radarem.

## 1.12.2.k3

- Informacje rozróżniają źródło i wydania partymola od repozytorium i listy zmian BikeRadar-Kross.
- Diagnostyka ma zwijany podgląd sygnałów i szersze przyciski dzienników. Dziennik stanu pokazuje odległość i zagrożenie dla Krossa, odległość dla Garmina V1 oraz odległość i prędkość względną dla Garmina V2. Licznik nieprawidłowych restartów usługi ma osobne objaśnienie.
- Dziennik przechwytywania pozostaje otwarty przez 15 sekund po zerwaniu łącza. Szybki powrót tego samego radaru dopisuje do pliku kolejną próbę.
- Podczas odbioru danych z Krossa niedostępne są automatyczne tryby tylnej lampy, korekta montażu, próba ustawień Garmina, liczenie bliskich minięć i opcje alertów zależne od algorytmu Garmina. Zapisane preferencje są dostępne po powrocie do Garmina.
- Siedem nagrań z 19 listopada potwierdziło odczyt baterii i firmware. W 2995 ramkach FFE2 brak aktywnych celów. Prędkość i strona mijania pozostają do potwierdzenia na sprzęcie.
- Przeszło 311 wybranych testów, w tym 90 testów zrzutów w UTC. Kompilacja Debug i ktlint przeszły. Kod APK to **47**.

## 1.12.2.k2

- Kross Echo Pro pokazuje procent baterii z istniejącą ikoną i wersję oprogramowania w karcie System, Szybkim stanie oraz szczegółach radaru.
- Bateria jest odczytywana po połączeniu i odświeżana co 20 sekund. Aplikacja odczytuje firmware bez włączania Diagnostyki.
- Przeszło 299 wybranych testów, w tym 113 testów zrzutów ekranu z JVM w UTC.
- Kod APK to **46**. Odczyt był potwierdzony na S24+ w k1. Ekrany i odświeżanie w k2 czekają na próbę na urządzeniu.

## 1.12.2.k1

Pierwsza numerowana wersja testowa modyfikacji Krossa. Zbiera dotychczasowe zmiany z eksperymentalnych APK, które używały opisowych nazw wersji i kodu 44.

- Wykrywanie Kross Echo Pro przez BLE i wybór radaru na istniejącej liście urządzeń.
- Status połączenia Krossa na ekranie głównym, w Szybkim stanie i w Połączeniach.
- Cele, odległość i poziom zagrożenia z ramek `0x30` oraz podstawowe sygnały dźwiękowe na istniejącej nakładce.
- Opcjonalne odczyty baterii, modelu i ustawień Krossa w Diagnostyce. Narzędzia Garmina są nadal dostępne.
- Polska wersja językowa.

Kod APK Krossa to **45**. Na fizycznym radarze nadal trzeba sprawdzić działanie alertów podczas jazdy. Prędkość, strona mijania i piąty cel wymagają dodatkowych pomiarów.


### Próba na sprzęcie po udostępnieniu k1

Na S24+ potwierdzono odczyt baterii (100%), modelu `pan1080xb1` oraz odpowiedzi FFF1 zawierającej firmware `1.0.2`. Strumień FFE2 działał podczas odczytu. Szczegóły są w [opisie BLE](docs/kross/BLE_PROTOCOL.md#physical-configuration-read-on-s24).
