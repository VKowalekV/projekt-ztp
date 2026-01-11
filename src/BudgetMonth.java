import java.time.LocalDate;

public class BudgetMonth {
    private int year;
    private int month;
    private RootCategory rootCategory;
    private BudgetLifecycleState lifecycleState;
    private double totalIncome;

    public BudgetMonth(int year, int month) {
        this.year = year;
        this.month = month;
        this.rootCategory = new RootCategory();
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

    }

    public void performAddSubCategory(String parentName, String newCatName, double limit) {

    }

    private Category findCategoryRecursive(Category current, String name) {

        return null;
    }

    public double getTotalExpenses() {
        return rootCategory.getAmount();
    }
}
