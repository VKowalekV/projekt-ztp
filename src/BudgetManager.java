public class BudgetManager {

    private static BudgetManager instance;
//    private Category rootCategory;
//    private List<BudgetObserver> observers;
    private double totalIncome;

    private BudgetManager() {
    }

    public static BudgetManager getInstance() {
        return instance;
    }

//    public Category getRootCategory() {
//    }

    public void addIncome(double amount) {
    }

    public double getIncome() {
        return totalIncome;
    }

    public void addTransaction(String categoryName, double amount, String desc) {}

//    private Category findCategoryRecursive(Category current, String name) {
//
//    }

    public void addSubCategory(String parentName, String newCatName, double limit) {

    }

    public double getTotalExpenses() {
        return 0;
    }

    public double getCurrentSavings() {
        return 0;
    }

    public double getForecast() {
        return 0;
    }

//    public void registerObserver(BudgetObserver o) {
//    }

//    public void removeObserver(BudgetObserver o) {
//    }

    public void notifyObservers() {
    }

    public void exportData(String format) {
    }

}