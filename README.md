# Gra Balony 🎈

> Gra podobna do "Bubble shooter" oparta na własnej, nieco innej mechanice. Projekt stworzony oryginalnie na studiach jako część przedmiotu "Programowanie zdarzeniowe" na Wydziale Elektroniki i Technik Informacyjnych Politechniki Warszawskiej (WUT).

Prosta i wciągająca gra zręcznościowa napisana na interfejsie graficznym Java (Swing). Gra polega na odpowiednim dobieraniu i zbijaniu kolorowych balonów z planszy, zanim te dotkną dolnej krawędzi ekranu!

## 📜 Zasady Gry

Twoim zadaniem jest strzelanie balonami (pociskami) z dolnej platformy w grupy innych balonów znajdujących się powyżej.

- Aby usunąć balony z planszy, musisz stworzyć grupę co najmniej **3 balonów tego samego koloru**.
- Jeśli pudłujesz, bądź Twój strzał nie spowoduje zbicia balonów, otrzymujesz punkty karne.
- Po zebraniu określonej liczby punktów karnych (w zależności od trudności), balony na planszy obniżają się o jeden rząd.
- **Gra kończy się**, gdy którykolwiek z balonów dotknie samego dołu ekranu. Im dłużej przetrwasz, tym wyższy Twój wynik!

W grze przygotowane są również **bonusy i przeszkody**:

- **Tęczowy balon** (różowy) – automatycznie przybiera kolor balonu, w który trafi, działając niczym kameleon.
- **Pechowy czarny balon** – nie można go zniszczyć w klasyczny sposób. Wymaga sprytu lub użycia materiałów wybuchowych!
- **Bomba** (ikona bomby) – specjalny rodzaj pocisku, który pojawia się przy udanych kombo. Niszczy natychmiastowo duży obszar balonów wokół miejsca, w którym wyląduje!

## ⚙️ Wymagania

- Zainstalowane środowisko uruchomieniowe Java (JRE) lub narzędzia deweloperskie (JDK 8+).
- System operacyjny Windows, macOS lub Linux.

## 🚀 Kompilacja i uruchomienie

Jeżeli pobrałeś kody źródłowe gry, postępuj zgodnie z tą instrukcją instalacji:

1. Otwórz wiersz poleceń (terminal) w głównym folderze ze spakowanym repozytorium.
2. Skompiluj najpierw źródła komendą `javac` (wymagane kodowanie UTF-8), kierując gotowe pliki do folderu `bin`:

   ```bash
   javac -encoding UTF-8 -d bin src\*.java
   ```

3. Po prawidłowej kompilacji rozpocznij grę główną metodą startową zawartą w `MenuGlowne`:

   ```bash
   java -cp bin MenuGlowne
   ```

## 🏆 Tablica Wyników i Poziomy

Z poziomu menu głównego gry możesz wczytać predefiniowane układy balonów dla **Trzech Plansz** (`Pierwszy.txt`, `Drugi.txt`, `Trzeci.txt`). Rozgrywka umożliwia również dynamiczną modyfikację poziomu trudności dla każdego z poziomów:

- **Łatwy** (wolny spadek, standardowa punktacja)
- **Średni** (szybszy spadek, mnożnik pkt 1.25x)
- **Trudny** (bardzo szybki spadek, mnożnik pkt 1.50x)

Dla każdej z 3 głównych map zdefiniowana jest oddzielna Tablica Najlepszych Wyników (High Scores), która zapisuje historyczne wyniki graczy lokalnie w plikach tekstowych. Rywalizuj sam ze sobą i stań się mistrzem Balonów!

---
*Zrealizowany projekt gry Java w celach edukacyjnych.*
