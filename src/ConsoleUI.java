import java.io.BufferedReader;
import java.util.List;

public class ConsoleUI implements BudgetObserver {
//    -reader : BufferedReader
//-manager : BudgetManager
//-myGoals : List<SavingsGoal>
//+ConsoleUI()
//+start() : void
//-printMenu() : void
//-handleAddIncome() : void
//-handleAddExpense() : void
//-handleAddGoal() : void
//-handleShowReport() : void
//-printCategoryRecursive(cat: Category, level: int) : void
//-handleExport() : void
//+update(totalExpenses: double, totalIncome: double) : void
    private BufferedReader reader;
    private BudgetManager manager;
    private List<SavingsGoals> myGoals;

    public ConsoleUI() {
    }
    public void start() {
    }

    private void printMenu() {
    }
    private void handleAddIncome() {
    }
    private void handleAddExpense() {
    }
    private void handleAddGoal() {
    }
    private void handleShowReport() {
    }
    private void printCategoryRecursive(Category cat, int level) {
    }
    private void handleExport() {
    }

    public void update(double totalExpenses, double totalIncome) {
    }
}
/*
Klasa ConsoleUI

Główny interfejs tekstowy. Pełni rolę widoku oraz kontrolera. Implementuje interfejs BudgetObserver, aby automatycznie odświeżać widok po zmianie danych.

Pola:

reader : BufferedReader – Strumień wejściowy do odczytu danych z klawiatury.

manager : BudgetManager – Referencja do Singletona (logiki biznesowej).

myGoals : List<SavingsGoal> – Lista celów oszczędnościowych utworzonych w tej sesji UI.

Metody:

+ ConsoleUI() – Konstruktor. Inicjalizuje reader i pobiera instancję Managera.

+ start() : void – Uruchamia nieskończoną pętlę (while true) obsługującą menu główne.

printMenu() : void – Wypisuje dostępne opcje (1. Dodaj, 2. Raport, 3. Eksport...).

handleAddIncome() : void – Pyta użytkownika o kwotę i wywołuje manager.addIncome().

handleAddExpense() : void – Pyta o kategorię, kwotę, opis i datę, a następnie wywołuje manager.addTransaction().

handleAddGoal() : void – Tworzy nowy obiekt SavingsGoal i rejestruje go w Managerze.

handleShowReport() : void – Inicjuje wyświetlanie struktury budżetu (wywołuje printCategoryRecursive).

handleShowForecast() : void – Pobiera wynik manager.getForecast() i wyświetla estymację wydatków na koniec miesiąca.

printCategoryRecursive(cat: Category, level: int) : void – Rekurencyjnie przechodzi przez drzewo kategorii. Używa parametru level do robienia wcięć. Pobiera kolor ze stanu kategorii (cat.getState().getColorCode()), aby wyróżnić przekroczenia budżetu.

handleExport() : void – Pyta o format (CSV/JSON) i zleca manager.exportData().

+ update(totalExpenses, totalIncome) : void – Metoda z wzorca Observer. Wyświetla komunikat o zmianie salda (reakcja na dodanie transakcji).*/