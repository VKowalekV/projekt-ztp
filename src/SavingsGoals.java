public class SavingsGoals implements BudgetObserver {
    private String goalName;
    private double targetAmount;
    private boolean isMet;
    private double currentSavings;

    public SavingsGoals(String Name, double target) {
        goalName = Name;
        targetAmount = target;
        this.currentSavings = 0.0;
    }

    public void update(double totalExpenses, double totalIncome) {
        this.currentSavings = totalIncome - totalExpenses;
        isMet = currentSavings >= targetAmount;
        System.out.println(getStatus());
    }

    public String getStatus() {
        double percent = targetAmount > 0 ? Math.min(100.0, (currentSavings / targetAmount) * 100.0) : 100.0;
        return String.format("%s: %.2f/%.2f (%.0f%%) - %s", goalName, currentSavings, targetAmount, percent, isMet ? "Osiągnięty" : "Nieosiągnięty");
    }
}