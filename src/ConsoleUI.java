import java.io.BufferedReader;
import java.util.List;

public class ConsoleUI implements BudgetObserver {
//    -reader : BufferedReader
//-manager : BudgetManager
//-myGoals : List<SavingsGoal>
//+ConsoleUI()
//+start() : void
//-printMenu() : void
//-handleAddIncome() : void
//-handleAddExpense() : void
//-handleAddGoal() : void
//-handleShowReport() : void
//-printCategoryRecursive(cat: Category, level: int) : void
//-handleExport() : void
//+update(totalExpenses: double, totalIncome: double) : void
    private BufferedReader reader;
    private BudgetManager manager;
    private List<SavingsGoals> myGoals;

    public ConsoleUI(){
        this.manager = new BudgetManager();
    }
    public void start() {
    }

    private void printMenu() {
    }
    private void handleAddIncome() {
    }
    private void handleAddExpense() {
    }
    private void handleAddGoal() {
    }
    private void handleShowReport() {
    }
    private void printCategoryRecursive(Category cat, int level) {
    }
    private void handleExport(){
        Scanner scanner = new Scanner(System.in);
        String extension;
        boolean isValid = false;

        do {
            System.out.println("Select file format (CSV or JSON):");
            extension = scanner.nextLine().toUpperCase();

            if (extension.equals("CSV") || extension.equals("JSON")) {
                isValid = true;
            } else {
                System.out.println("Invalid format. Please enter 'CSV' or 'JSON'.");
            }
        } while (!isValid);

        scanner.close();

        ExporterCreator creator;
        if (extension.equals("JSON")) {
            creator = new JSONExporterCreator();
        } else {
            creator = new CSVExporterCreator();
        }

        manager.exportData(creator);
    }

    public void update(double totalExpenses, double totalIncome) {
    }
}
