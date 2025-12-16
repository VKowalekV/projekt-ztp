public interface BudgetComponent {
    double getAmout();
    String getName();
    String getType();

    void add(BudgetComponent c);
    void remove(BudgetComponent c);
    BudgetComponent getChild(String name);
}
