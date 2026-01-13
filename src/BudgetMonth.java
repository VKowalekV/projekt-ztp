import java.time.LocalDate;

public class BudgetMonth {
    private int year;
    private int month;
    private Category rootCategory;
    private BudgetLifecycleState lifecycleState;
    private double totalIncome;

    public BudgetMonth(int year, int month) {
        this.year = year;
        this.month = month;
        this.rootCategory = new Category("Budzet", 0.0);
        this.lifecycleState = new DraftState();
        this.totalIncome = 0.0;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public void setLifecycleState(BudgetLifecycleState state) {
        this.lifecycleState = state;
    }

    public BudgetLifecycleState getLifecycleState() {
        return lifecycleState;
    }

    public void nextState() {
        lifecycleState.nextState(this);
    }

    public Category getRootCategory() {
        return rootCategory;
    }

    public void addIncome(double amount) {
        this.totalIncome += amount;
        this.rootCategory.setSpendingLimit(totalIncome);
    }

    public double getIncome() {
        return totalIncome;
    }

    public void addTransaction(String categoryName, double amount, String desc) {
        lifecycleState.addTransaction(this, categoryName, amount, desc);
    }

    public void addSubCategory(String parentName, String newCatName, double limit) {
        lifecycleState.addCategory(this, parentName, newCatName, limit);
    }

    public void performAddTransaction(String categoryName, double amount, String desc) {
        Category category = findCategoryRecursive(rootCategory, categoryName);
        if (category != null) {
            category.add(new Transaction(amount, desc, LocalDate.of(year, month, 1)));
        } else {
            System.out.println("Nie znaleziono kategorii o nazwie " + categoryName);
        }
    }

    public void performAddSubCategory(String parentName, String newCatName, double limit) {
        Category parent = findCategoryRecursive(rootCategory, parentName);
        if (parent != null) {
            double currentChildrenLimitSum = 0;
            for (BudgetComponent c : parent.getChildren()) {
                if (c instanceof Category) {
                    currentChildrenLimitSum += ((Category) c).getLimit();
                }
            }
            if (currentChildrenLimitSum + limit > parent.getLimit()) {
                System.out.println(
                        "Blad: Suma limitow kategorii przekracza dostepne srodki (limit kategorii nadrzednej '"
                                + parent.getName() + "': "
                                + parent.getLimit() + ")");
                return;
            }

            parent.add(new Category(newCatName, limit));
        } else {
            System.out.println("Nie znaleziono kategorii o nazwie " + parentName);
        }
    }

    private Category findCategoryRecursive(Category current, String name) {
        if (current.getName().equalsIgnoreCase(name)) {
            return current;
        }
        for (BudgetComponent child : current.getChildren()) {
            if (child instanceof Category) {
                Category result = findCategoryRecursive((Category) child, name);
                if (result != null)
                    return result;
            }
        }
        return null;
    }

    public double getTotalExpenses() {
        return rootCategory.getAmount();
    }
}
