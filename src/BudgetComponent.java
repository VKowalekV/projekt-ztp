public interface BudgetComponent {
    double getAmount();

    String getName();

    String getType();

    void add(BudgetComponent c);

    void remove(BudgetComponent c);

    BudgetComponent getChild(String name);

    void setParent(BudgetComponent parent);

    BudgetComponent getParent();
}
