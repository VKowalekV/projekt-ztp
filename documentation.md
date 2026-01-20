# Dokumentacja Projektu Budżet Domowy

## 1. Wzorce Projektowe

W projekcie zastosowano 5 wzorców projektowych, które organizują logikę biznesową, zarządzanie stanem oraz interakcję z użytkownikiem.

---

### 1.1 Multiton Pattern

**Cel użycia:**
Zarządzanie instancjami budżetów dla różnych użytkowników (np. budżet domowy, firmowy). Gwarantuje, że dany identyfikator budżetu (np. "Dom") ma tylko jedną, globalną instancję w systemie, ale pozwala na istnienie wielu niezależnych instancji dla różnych kluczy.

**Role klas:**

- **Multiton (Registry):** `BudgetManager` (przechowuje mapę instancji).

**Lokalizacja:**

- **Definicja:** `src/BudgetManager.java` (metoda `getInstance(String key)`).
- **Użycie:** `src/ConsoleUI.java` (linia `this.manager = BudgetManager.getInstance(loggedBudgetId);`).

**Wektor zmian:**
Pozwala na łatwe dodawanie nowych, niezależnych budżetów bez modyfikacji kodu klienta. W przyszłości można łatwo dodać mechanizm persistencji (zapisu do bazy danych) dla każdego klucza niezależnie.

---

### 1.2 Composite Pattern

**Cel użycia:**
Traktowanie pojedynczych transakcji i grup transakcji (kategorii) w jednolity sposób. Pozwala to na budowanie hierarchicznej struktury wydatków (drzewo kategorii), gdzie kategoria może zawierać podkategorie lub transakcje.

**Role klas:**

- **Component:** `BudgetComponent` (interfejs definiujący wspólne operacje).
- **Composite:** `Category` (kontener, może mieć dzieci).
- **Leaf:** `Transaction` (liść, nie ma dzieci).

**Lokalizacja:**

- **Interfejs:** `src/BudgetComponent.java`
- **Klasy:** `src/Category.java`, `src/Transaction.java`
- **Użycie:** `src/BudgetMonth.java` (pole `rootCategory`), `src/ConsoleUI.java` (metoda `printCategoryRecursive`).

**Wektor zmian:**
Ułatwia dodawanie nowych typów składników budżetu (np. "Zlecenie stałe") bez zmiany logiki obliczania sum czy wyświetlania.

---

### 1.3 State Pattern

W projekcie wyróżniono dwa zastosowania wzorca State:

#### A. Budget Lifecycle State (Cykl życia budżetu)

**Cel użycia:**
Kontrola dostępnych operacji w zależności od etapu miesiąca budżetowego (np. planowanie, realizacja, zamknięcie).

**Role klas:**

- **State Interface:** `BudgetLifecycleState`
- **Concrete States:** `DraftState` (Planowanie), `ActiveState` (Aktywny), `ClosedState` (Zamknięty).
- **Context:** `BudgetMonth`.

**Lokalizacja:**

- **Pakiet:** `src/` (pliki `BudgetLifecycleState.java`, `DraftState.java` itd.)
- **Użycie:** `src/BudgetMonth.java` (metody delegujące do `lifecycleState`).

**Wektor zmian:**
Pozwala na dodanie nowych etapów (np. "Audyt") lub zmianę reguł przejść między stanami bez ingerencji w klasę `BudgetMonth`.

#### B. Budget Limit State (Stan limitu kategorii)

**Cel użycia:**
Dynamiczne określanie statusu kategorii w oparciu o stopień wykorzystania limitu (np. ostrzeżenie, przekroczenie).

**Role klas:**

- **State Interface:** `BudgetState`
- **Concrete States:** `NormalState`, `WarningState`, `LimitReachedState`, `ExceededState`.
- **Context:** `Category`.

**Lokalizacja:**

- **Pakiet:** `src/` (pliki `BudgetState.java`, `NormalState.java` itd.)
- **Użycie:** `src/Category.java` (metoda `checkLimitState`).

**Wektor zmian:**
Możliwość łatwego dodania nowych reguł walidacji (np. "Krytyczny poziom") lub zmiany progów procentowych dla ostrzeżeń.

---

### 1.4 Observer Pattern

**Cel użycia:**
Automatyczne powiadamianie elementów systemu o zmianach w finansach (np. aktualizacja celów oszczędnościowych, logowanie operacji).

**Role klas:**

- **Subject:** `BudgetManager` (zarządza listą obserwatorów).
- **Observer:** `BudgetObserver` (interfejs).
- **Concrete Observers:** `SavingsGoals`, `SystemLogger`, `ConsoleUI`.

**Lokalizacja:**

- **Interfejs:** `src/BudgetObserver.java`
- **Subject:** `src/BudgetManager.java` (metody `registerObserver`, `notifyObservers`).
- **Użycie:** `src/BudgetManager.java` (wywołanie `notifyObservers` przy zmianie przychodów/wydatków).

**Wektor zmian:**
Umożliwia dodawanie nowych reakcji na zmiany w budżecie (np. wysyłanie e-maili, aktualizacja wykresów w GUI) bez modyfikacji logiki biznesowej `BudgetManager`.

---

### 1.5 Factory Method Pattern

**Cel użycia:**
Separacja logiki tworzenia obiektów eksportu od logiki ich użycia. Pozwala na wybór formatu eksportu (CSV/JSON) w czasie działania programu.

**Role klas:**

- **Creator (Abstract):** `ExporterCreator`.
- **Concrete Creators:** `CSVExporterCreator`, `JSONExporterCreator`.
- **Product (Interface):** `FileExporter`.
- **Concrete Products:** `CSVExporter`, `JSONExporter`.

**Lokalizacja:**

- **Pakiet:** `src/` (pliki `ExporterCreator.java`, `FileExporter.java` itd.)
- **Użycie:** `src/ConsoleUI.java` (wybór kreatora w `handleExport`).

**Wektor zmian:**
Bardzo łatwe dodanie nowego formatu eksportu (np. PDF, XML). Wystarczy dodać nową klasę eksportera i kreatora.

---

## 2. Podział pracy w zespole

| Osoba                 | Zakres odpowiedzialności                                                                                                                                                                                                                                                                                                                                                                |
| :-------------------- | :-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Maciej Lachowicz**  | **Wzorzec Factory Method:**<br>- Klasy `ExporterCreator` (abstrakcja) i implementacje `CSVExporterCreator`, `JSONExporterCreator`.<br>- Interfejs `FileExporter` i implementacje `CSVExporter`, `JSONExporter`.<br>- Implementacja obsługi eksportu w `ConsoleUI` (`handleExport`).                                                                                                     |
| **Julia Stankiewicz** | **Wzorzec State (Lifecycle & Limits):**<br>- Interfejsy `BudgetState`, `BudgetLifecycleState`.<br>- Stany limitów: `ExceededState`, `LimitReachedState`, `NormalState`, `WarningState`.<br>- Stany cyklu życia: `ActiveState`, `ClosedState`, `DraftState`.<br>- Implementacja delegacji stanów w `BudgetMonth` (pole i metody lifecycle).<br>- Metoda `handleNextState` w `ConsoleUI`. |
| **Stanisław Tomczak** | **Wzorzec Observer:**<br>- Interfejs `BudgetObserver`.<br>- Klasy obserwatorów: `SystemLogger`, `SavingsGoals`.<br>- Logika powiadomień i celów oszczędnościowych w `ConsoleUI` (`handleAddSavingsGoal`, `update`, `checkExceededRecursive`).                                                                                                                                           |
| **Damian Kowalski**   | **Wzorzec Composite & UI Core:**<br>- Interfejs `BudgetComponent`.<br>- Klasy struktury danych: `Transaction`, `Category`.<br>- Logika drzewiasta w `BudgetMonth` (zarządzanie `rootCategory`, rekurencja).<br>- `ConsoleUI`: Główne menu, obsługa wejścia, `handleAddCategory`, `handleAddExpense`, wyświetlanie drzewa (`printCategoryRecursive`).                                    |
| **Hubert Urbański**   | **Wzorzec Multiton & Core Logic:**<br>- Klasa `BudgetManager` (singleton/multiton logic).<br>- `BudgetMonth`: logika podstawowa (konstruktor, getters, zarządzanie dochodami).<br>- `ConsoleUI`: Inicjalizacja, logowanie, zarządzanie sesją (`start`, `handleChangeMonth`, `handleAddIncome`, `handleShowReport`, `handleShowForecast`).                                               |

---

## 3. Instrukcja Użytkownika

### Uruchamianie i Logowanie

Po starcie aplikacji wyświetlany jest ekran logowania. Użytkownik ma możliwość zalogowania się lub zakończenia programu. Aplikacja nie umożliwia rejestracji nowych kont.

Po wybraniu opcji **Zaloguj** należy podać login oraz hasło (identyfikator budżetu oznacza do jakiego budżetu należy nasz klient nie wpisujemy go podczas logowania).

**Dostępne konta testowe:**

- login: `jan` hasło: `123` (identyfikator budżetu: "Dom")
- login: `anna` hasło: `123` (identyfikator budżetu: "Firma")
- login: `marcin` hasło: `123` (identyfikator budżetu: "Dom")
- login: `kacper` hasło: `123` (identyfikator budżetu: "Firma")

Po poprawnym logowaniu użytkownik zostaje przeniesiony do głównego MENU aplikacji.

### Główne Funkcjonalności

Budżet może znajdować się w trzech stanach: planowania, aktywnym oraz zamkniętym.

#### Stan Planowania

Po zalogowaniu budżet domyślnie znajduje się w stanie planowania. W tym trybie dostępne są następujące opcje:

- dodanie przychodu
- dodanie kategorii
- dodanie celów oszczędnościowych
- zatwierdzenie planu
- przełączenie miesiąca
- wyświetlanie raportu
- wyświetlanie prognozy
- wyjście z programu

Po dodaniu kategorii i przychodów należy zatwierdzić plan, co zmienia stan budżetu na aktywny.

#### Stan Aktywny

W stanie aktywnym użytkownik może:

- dodać przychód
- dodać wydatek
- dodać cele oszczędnościowe
- zamknąć miesiąc
- przełączyć miesiąc
- wyświetlić raport
- wyświetlić prognozę
- wyjść z programu

Po zakończeniu miesiąca i wybraniu opcji zamknij miesiąc budżet przechodzi w stan zamknięty. W tym stanie nie ma możliwości edycji przychodów, wydatków ani kategorii.

#### Stan Zamknięty

W stanie zamkniętym użytkownik może:

- dodać cele oszczędnościowe
- przełączyć miesiąc
- wyświetlić raport
- eksportować dane
- wyświetlić prognozę
- wyjść z programu

Po zamknięciu miesiąca użytkownik może przejść do kolejnego miesiąca i ponownie korzystać z aplikacji.

### Opis działania poszczególnych funkcji

1.  **Dodanie przychodu (Opcja 1):** Aby dodać przychód należy w MENU wybrać opcję 1, a następnie podać jego kwotę. Po zatwierdzeniu wyświetlany jest komunikat o pomyślnym dodaniu przychodu.

2.  **Dodanie wydatku (Opcja 2):** Dodanie wydatku odbywa się poprzez wybranie opcji 2 w MENU. Następnie należy wskazać istniejącą kategorię, podaj kwotę wydatku oraz jego opis. Po zatwierdzeniu wyświetlany jest komunikat potwierdzający dodaniu wydatku.

3.  **Dodanie kategorii (Opcja 3):** W celu dodania nowej kategorii należy w MENU wybrać opcję 3. Najpierw podaje się nazwę kategori nadrzędnej (domyślnie istnieje kategoria Budzet), następnie nazwę nowej kategorii oraz limit wydatków. Po poprawnym dodaniu wyświetlany jest stosowny komunikat.

4.  **Cele oszczędnościowe (Opcja 4):** Opcja 4 w MENU umożliwia dodawanie nowego celu oszczędnościowego. W tym celu należy podać nazwę celu oraz kwotę, która użytkownik chce zgromadzić. Po zatwierdzeniu pojawia się komunikat o pomyślnym dodaniu celu oszczędnościowego.

5.  **Zmiana stanu budżetu (Opcja 5):** Zmiana stanu budżetu odbywa się poprzez wybór opcji 5 w MENU. Pozwala to przejść ze stanu planowania do stanu aktywnego, a następnie zamknąć miesiąc. Po każdej zmianie wyświetlany jest komunikat potwierdzający operację.

6.  **Przełączenie miesiąca (Opcja 6):** Aby zmienić miesiąc należy wybrać opcję 6, a następnie podać rok oraz miesiąc, dla którego ma być prowadzony budżet. Po zatwierdzeniu aplikacja przełącza się na wybrany okres.

7.  **Wyświetlanie raportu (Opcja 7):** Opcja 7 pozwala wyświetlić raport finansowy. Zawiera on informację o całkowitych przychodach, wydatkach, bilansie oraz szczegółowe zestawienie wydatków i limitów dla każdej kategorii.

8.  **Eksport danych (Opcja 8):** Aby wyeksportować dane w MENU należy wybrać opcję 8. Najpierw wybieramy rozszerzenia pliku, w którym mają zostać zapisane (JSON lub CSV). Po wyborze rozszerzenia wykonywany jest eksport do pliku zawierającego następujące informacje: nazwę kategorii, typ obiektu (kategoria lub transakcja przypisana do danej kategorii), kwotę wydaną w danej kategorii lub transakcji, limit wydatków dla kategorii oraz ścieżkę do danej kategorii. Pliki pojawią się w folderze exports w folderze w zależności od rozszerzenia, CSVExports albo JSONExports.

9.  **Prognoza finansowa (Opcja 9):** Po wybraniu opcji 9 aplikacja wyświetla prognozę finansową, która pokazuje szacowaną ilość środków pozostałych na koniec miesiąca.

## 4. Instrukcja Instalacji

### Wymagania

- **Zalecane JDK:** Java 25 (projekt tworzony w tej wersji).
- **Minimum:** Java 17 lub nowsza (ze względu na użycie _Switch Expressions_).

### Instalacja i Uruchomienie

**Pobierz kod źródłowy:**
Sklonuj repozytorium lub wypakuj archiwum z kodem.

#### IntelliJ IDEA lub inne IDE

1. **Otwórz projekt:**
   Wybierz opcję _File -> Open_ i wskaż folder z projektem. IntelliJ powinien automatycznie wykryć projekt.

2. **Skonfiguruj SDK:**
   Upewnij się, że w _Project Structure_ ustawione jest **JDK 25** (lub min. 17).

3. **Weryfikacja Bibliotek (Ważne!):**
   Projekt zawiera biblioteki w folderze `lib/`. Jeśli IDE ich nie widzi (błędy w kodzie przy importach `com.google.gson` lub `org.apache.commons.csv`):

   - Kliknij prawym przyciskiem myszy na folder `lib` w drzewie projektu.
   - Wybierz opcję **"Add as Library..."**.
   - Zatwierdź nazwę (np. "lib") i kliknij OK.
   - To doda pliki `.jar` do ścieżki projektu (Classpath).

4. **Uruchom:**
   Odszukaj klasę `Main.java` w folderze `src/`. Kliknij prawym przyciskiem myszy na tę klasę i wybierz opcję **Run 'Main.main()'** (zielona strzałka).

### Uwagi

- Pliki eksportu (JSON/CSV) pojawią się automatycznie w folderze `exports/` w katalogu roboczym programu.
