import java.util.ArrayList;
import java.util.List;

public class Category implements BudgetComponent{
    private String name;
    private List<BudgetComponent> children;
    private double spendingLimit;
//    private BudgetState state;

    public Category(String name, double spendingLimit) {
        this.name = name;
        this.spendingLimit = spendingLimit;
        this.children = new ArrayList<>();

        // dodać state jak będzie gotowy
    }

    @Override
    public void add(BudgetComponent c) {
        this.children.add(c);
        checkLimitState();
    }

    @Override
    public void remove(BudgetComponent c) {
        this.children.remove(c);
        checkLimitState();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getType() {
        return "CATEGORY";
    }

    @Override
    public double getAmount() { // zrobić
        double total = 0;
        return total;
    }

    @Override
    public BudgetComponent getChild(String name) {
        return null;
    }

    public List<BudgetComponent> getChildren() {
        return children;
    }

    public double getLimit() {
        return spendingLimit;
    }

// zrobić get i set dla state

    public void checkLimitState() { // zrobić
    }
}
