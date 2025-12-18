import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class ConsoleUI implements BudgetObserver {
    private BufferedReader reader;
    private BudgetManager manager;
    private List<SavingsGoals> myGoals;

    public ConsoleUI(){
        this.reader = new BufferedReader(new InputStreamReader(System.in));
        this.manager = BudgetManager.getInstance();
        // stachu dodaj my goals register observer
    }
    public void start() {
        System.out.println("Witaj w Budżecie Domowym!");

        while (true) {
            printMenu();
            try {
                int choice = Integer.parseInt(reader.readLine());

                switch (choice) {
                    case 1 -> handleAddIncome();
                    case 2 -> handleAddExpense();
                    case 3 -> handleAddGoal();
                    case 4 -> handleShowReport();
                    case 5 -> handleShow();
                    case 6 -> handleExport();
                    case 7 -> {
                        System.out.println("Żegnaj");
                        return;
                    }
                    default -> System.out.println("Nieznana opcja.");
                }
            } catch (Exception e) {
                System.out.println("Błąd wejścia: " + e.getMessage());
            }
        }
    }

    private void printMenu() {
        System.out.println("\n--- MENU ---");
        System.out.println("1. Dodaj przychód");
        System.out.println("2. Dodaj wydatek");
        System.out.println("3. Dodaj cel oszczędnościowy");
        System.out.println("4. Pokaż raport wydatków");
        System.out.println("5. Pokaż prognozę");
        System.out.println("6. Eksportuj dane");
        System.out.println("7. Wyjście");
        System.out.print("Wybór: ");
    }

    private void handleAddIncome() {}

    private void handleAddExpense() {}

    private void handleAddGoal() {}

    private void handleShowReport() {}

    private void handleShowForecast() {}

    private void handleShow() {}

    private void printCategoryRecursive(Category cat, int level) {}

    private void handleExport() throws IOException {
        String extension;
        boolean isValid = false;

        do {
            System.out.println("Select file format (CSV or JSON):");
            extension = reader.readLine().toUpperCase();

            if (extension.equals("CSV") || extension.equals("JSON")) {
                isValid = true;
            } else {
                System.out.println("Invalid format. Please enter 'CSV' or 'JSON'.");
            }
        } while (!isValid);

        ExporterCreator creator;
        if (extension.equals("JSON")) {
            creator = new JSONExporterCreator();
        } else {
            creator = new CSVExporterCreator();
        }

        manager.exportData(creator);
    }

    public void update(double totalExpenses, double totalIncome) {}
}
