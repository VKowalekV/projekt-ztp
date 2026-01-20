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

### Logowanie i Start

Po starcie aplikacji wyświetlany jest ekran logowania. Użytkownik ma możliwość zalogowania się lub zakończenia programu. Aplikacja nie umożliwia rejestracji nowych kont.

Po wybraniu opcji **Zaloguj** należy podać login oraz hasło. Identyfikator budżetu (np. "Dom") jest przypisany do konta i nie wpisujemy go ręcznie.

**Dostępne konta testowe:**

- login: `jan`, hasło: `123` (Budżet "Dom")
- login: `anna`, hasło: `123` (Budżet "Firma")
- login: `marcin`, hasło: `123` (Budżet "Dom")
- login: `kacper`, hasło: `123` (Budżet "Firma")

Po poprawnym logowaniu użytkownik zostaje przeniesiony do głównego **MENU** aplikacji.

### Cykl Życia Budżetu

Budżet może znajdować się w trzech stanach, które determinują dostępne opcje:

#### 1. Stan Planowania

Domyślny stan po zalogowaniu (dla nowego miesiąca).
**Dostępne opcje:**

- Dodanie przychodu (1)
- Dodanie kategorii (3)
- Dodanie celów oszczędnościowych (4)
- Zatwierdzenie planu (5) - _przełącza na stan Aktywny_
- Przełączenie miesiąca (6)
- Wyświetlanie raportu (7)
- Wyświetlanie prognozy (9)
- Wyjście z programu (0)

_Uwaga: W tym stanie nie można dodawać wydatków (transakcji)._

#### 2. Stan Aktywny

Stan po zatwierdzeniu planu.
**Dostępne opcje:**

- Dodanie przychodu (1)
- Dodanie wydatku (2) - _przypisanie transakcji do kategorii_
- Dodanie celów oszczędnościowych (4)
- Zamknięcie miesiąca (5) - _przełącza na stan Zamknięty_
- Przełączenie miesiąca (6)
- Wyświetlanie raportu (7)
- Wyświetlanie prognozy (9)
- Wyjście z programu (0)

#### 3. Stan Zamknięty

Stan po zakończeniu miesiąca. Brak możliwości edycji przychodów, wydatków ani kategorii.
**Dostępne opcje:**

- Dodanie celów oszczędnościowych (4)
- Przełączenie miesiąca (6)
- Wyświetlanie raportu (7)
- Eksport danych (8)
- Wyświetlanie prognozy (9)
- Wyjście z programu (0)

### Opis Funkcjonalności (Menu)

1.  **Dodaj przychód (Opcja 1):**
    Należy podać kwotę przychodu. Po zatwierdzeniu zwiększa się pula dostępnych środków.

2.  **Dodaj wydatek (Opcja 2):**
    Dostępne tylko w stanie aktywnym. Należy wskazać istniejącą kategorię, podać kwotę oraz opis. System przypisuje wydatek do kategorii i sprawdza limity.

3.  **Dodaj kategorię (Opcja 3):**
    Dostępne tylko w stanie planowania. Należy podać nazwę kategorii nadrzędnej (domyślna to "Budzet"), nazwę nowej podkategorii oraz limit wydatków.

4.  **Dodaj cel oszczędnościowy (Opcja 4):**
    Należy podać nazwę celu oraz kwotę docelową. System automatycznie przydziela wolne środki (Przychód - Wydatki - Inne cele) na realizację celu.

5.  **Zmiana stanu budżetu (Opcja 5):**
    Służy do przełączania faz miesiąca: `Planowanie -> Aktywny -> Zamknięty`.

6.  **Zmień miesiąc (Opcja 6):**
    Pozwala podać rok i miesiąc, dla którego chcemy prowadzić budżet. Umożliwia pracę na budżetach historycznych lub przyszłych.

7.  **Pokaż raport (Opcja 7):**
    Wyświetla podsumowanie: całkowite przychody, wydatki, bilans oraz szczegółowe drzewo kategorii z informacją o wykorzystaniu limitów.

8.  **Eksport danych (Opcja 8):**
    Dostępne tylko w stanie zamkniętym. Pozwala na wybór formatu (**JSON** lub **CSV**). Pliki zapisywane są w folderze `exports/` i zawierają: nazwę, typ, kwotę, limit oraz ścieżkę kategorii.

9.  **Prognoza finansowa (Opcja 9):**
    Wyświetla symulację stanu konta na koniec miesiąca, bazując na obecnym tempie wydawania pieniędzy.

10. **Wyjście (Opcja 0):**
    Kończy działanie programu.

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
