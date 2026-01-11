import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BudgetManager {
    private static Map<String, BudgetManager> instances = new HashMap<>();
    private Category rootCategory;
    private List<BudgetObserver> observers;
    private double totalIncome;
    private ExporterCreator exporterFactory;

    private BudgetManager() {
        this.observers = new ArrayList<>();
        this.totalIncome = 0.0;
        this.rootCategory = new Category("Root", 0.0);
    }

    public static BudgetManager getInstance(String key) {
        if (!instances.containsKey(key)) {
            instances.put(key, new BudgetManager());
        }
        return instances.get(key);
    }

    public Category getRootCategory() {
        return rootCategory;
    }

    public void addIncome(double amount) {
        this.totalIncome += amount;
        notifyObservers();
    }

    public double getIncome() {
        return totalIncome;
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

    public void addTransaction(String categoryName, double amount, String desc) {
        Category category = findCategoryRecursive(rootCategory, categoryName);
        if (category != null) {
            category.add(new Transaction(amount, desc, LocalDate.now()));
            notifyObservers();
        } else {
            System.out.println("Nie znaleziono kategorii o nazwie " + categoryName);
        }
    }

    public void addSubCategory(String parentName, String newCatName, double limit) {
        Category parent = findCategoryRecursive(rootCategory, parentName);
        if (parent != null) {
            parent.add(new Category(newCatName, limit));
            notifyObservers();
        } else {
            System.out.println("Nie znaleziono kategorii o nazwie " + parentName);
        }
    }

    public double getTotalExpenses() {
        return rootCategory.getAmount();
    }

    public double getCurrentSavings() {
        return totalIncome - getTotalExpenses();
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
            o.update(getTotalExpenses(), totalIncome);
        }
    }

    public void exportData(ExporterCreator creator) {
        creator.performExport(rootCategory);
    }

}