import java.util.ArrayList;
import java.util.List;

public class Category implements BudgetComponent {
    private String name;
    private List<BudgetComponent> children;
    private double spendingLimit;
    private BudgetState state;

    private BudgetComponent parent;

    public Category(String name, double spendingLimit) {
        this.name = name;
        this.spendingLimit = spendingLimit;
        this.children = new ArrayList<>();
        this.state = new NormalState();
    }

    @Override
    public void add(BudgetComponent c) {
        this.children.add(c);
        c.setParent(this);
        checkLimitState();
    }

    @Override
    public void remove(BudgetComponent c) {
        this.children.remove(c);
        c.setParent(null);
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
    public double getAmount() {
        double total = 0;
        for (BudgetComponent c : children) {
            total += c.getAmount();
        }
        return total;
    }

    @Override
    public BudgetComponent getChild(String name) {
        for (BudgetComponent c : children) {
            if (c.getName().equals(name)) {
                return c;
            }
        }
        return null;
    }

    public List<BudgetComponent> getChildren() {
        return children;
    }

    public double getLimit() {
        return spendingLimit;
    }

    public void setSpendingLimit(double spendingLimit) {
        this.spendingLimit = spendingLimit;
        checkLimitState();
    }

    public void setState(BudgetState state) {
        this.state = state;
    }

    public BudgetState getState() {
        return state;
    }

    @Override
    public void setParent(BudgetComponent parent) {
        this.parent = parent;
    }

    @Override
    public BudgetComponent getParent() {
        return parent;
    }

    public void checkLimitState() {
        if (state != null)
            state.handleState(this);

        if (parent != null && parent instanceof Category) {
            ((Category) parent).checkLimitState();
        }
    }
}
