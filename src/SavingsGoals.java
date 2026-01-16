public class SavingsGoals implements BudgetObserver {
    private String goalName;
    private double targetAmount;
    private boolean isMet;
    private double allocatedAmount;

    public SavingsGoals(String Name, double target) {
        goalName = Name;
        targetAmount = target;
        this.allocatedAmount = 0.0;
    }

    public void update(double totalExpenses, double totalIncome) {
        isMet = allocatedAmount >= targetAmount;
        System.out.println(getStatus());
    }

    public String getStatus() {
        double percent = targetAmount > 0 ? Math.min(100.0, (allocatedAmount / targetAmount) * 100.0) : 100.0;
        return String.format("%s: %.2f/%.2f (%.0f%%) - %s", goalName, allocatedAmount, targetAmount, percent, isMet ? "Osiągnięty" : "Nieosiągnięty");
    }

    public String getGoalName() { return goalName; }
    public double getAllocated() { return allocatedAmount; }
    public double getRemainingNeed() { return Math.max(0.0, targetAmount - allocatedAmount); }

    public double allocate(double amount) {
        double toAlloc = Math.min(getRemainingNeed(), Math.max(0.0, amount));
        this.allocatedAmount += toAlloc;
        return toAlloc;
    }
}