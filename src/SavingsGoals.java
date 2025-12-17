public class SavingsGoals implements BudgetObserver {
    private String goalName;
    private double targetAmount;
    private boolean isMet;

    public SavingsGoals(String Name, double target) {
        goalName = Name;
        targetAmount = target;
    }

    public void update(double totalExpenses, double totalIncome) {
        isMet = totalIncome - totalExpenses > targetAmount;
    }

    public boolean getStatus() {
        return isMet;
    }
}
/*
Klasa SavingsGoal

Obserwuje budżet, by sprawdzić, czy użytkownika stać na cel.

Pola:

goalName : String – Nazwa celu (np. "Nowy Laptop").

targetAmount : double – Wymagana kwota.

isMet : boolean – Flaga, czy cel został osiągnięty.

Metody:

+ update(...) – Oblicza oszczędności (Income - Expenses). Jeśli są >= targetAmount, zmienia isMet na true.

+ getStatus() – Zwraca sformatowany tekst o postępie realizacji celu
*/