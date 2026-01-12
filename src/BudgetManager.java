import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BudgetManager {
    private static Map<String, BudgetManager> instances = new HashMap<>();
    private Map<String, BudgetMonth> months;
    private BudgetMonth currentMonth;
    private List<BudgetObserver> observers;

    private BudgetManager() {
        this.observers = new ArrayList<>();
        this.months = new HashMap<>();

        changeMonth(LocalDate.now().getYear(), LocalDate.now().getMonthValue());
    }

    public static BudgetManager getInstance(String key) {
        if (!instances.containsKey(key)) {
            instances.put(key, new BudgetManager());
        }
        return instances.get(key);
    }

    public void changeMonth(int year, int month) {
        String key = year + "-" + month;
        if (!months.containsKey(key)) {
            months.put(key, new BudgetMonth(year, month));
        }
        this.currentMonth = months.get(key);
        notifyObservers();
    }

    public BudgetMonth getCurrentMonth() {
        return currentMonth;
    }

    public void nextState() {
        if (currentMonth != null) {
            currentMonth.nextState();
        }
    }

    public Category getRootCategory() {
        return currentMonth != null ? currentMonth.getRootCategory() : null;
    }

    public void addIncome(double amount) {
        if (currentMonth != null) {
            currentMonth.addIncome(amount);
            notifyObservers();
        }
    }

    public double getIncome() {
        return currentMonth != null ? currentMonth.getIncome() : 0.0;
    }

    public void addTransaction(String categoryName, double amount, String desc) {
        if (currentMonth != null) {
            currentMonth.addTransaction(categoryName, amount, desc);
            notifyObservers();
        }
    }

    public void addSubCategory(String parentName, String newCatName, double limit) {
        if (currentMonth != null) {
            currentMonth.addSubCategory(parentName, newCatName, limit);
            notifyObservers();
        }
    }

    public double getTotalExpenses() {
        return currentMonth != null ? currentMonth.getTotalExpenses() : 0.0;
    }

    public double getCurrentSavings() {
        return getIncome() - getTotalExpenses();
    }

    public double getForecast() {
        return getCurrentSavings();
    }

    public void registerObserver(BudgetObserver o) {
        observers.add(o);
    }

    public void removeObserver(BudgetObserver o) {
        observers.remove(o);
    }

    public void notifyObservers() {
        for (BudgetObserver o : observers) {
            o.update(getTotalExpenses(), getIncome());
        }
    }

    public void exportData(ExporterCreator creator) {
        if (currentMonth != null) {
            creator.performExport(currentMonth.getRootCategory(), currentMonth);
        }
    }
}