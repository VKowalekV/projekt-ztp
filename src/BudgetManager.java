import java.time.LocalDate;
import java.util.List;

public class BudgetManager {
    private static BudgetManager instance;
    private Category rootCategory;
    private List<BudgetObserver> observers;
    private ExporterCreator exporterFactory;
    private double totalIncome;

    private BudgetManager() {
    }

    public static BudgetManager getInstance() {
        return instance;
    }

   public Category getRootCategory() {
       return rootCategory;
   }

    public void addIncome(double amount) {
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
                if (result != null) return result;
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

    public void exportData(ExporterCreator creator){
        creator.performExport(rootCategory);
    }

}