public interface BudgetLifecycleState {
    void addIncome(BudgetMonth context, double amount);
    void addCategory(BudgetMonth context, String parentName, String name, double limit);
    void addTransaction(BudgetMonth context, String categoryName, double amount, String desc);
    void exportData(BudgetMonth context, ExporterCreator creator);
    void nextState(BudgetMonth context);
    String getStateName();
}
