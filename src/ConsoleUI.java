import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

public class ConsoleUI implements BudgetObserver {
    private BufferedReader reader;
    private BudgetManager manager;
    private List<SavingsGoals> myGoals;

    private Map<String, String> userBudgets = new HashMap<>();
    private Map<String, String> userPasswords = new HashMap<>();

    public ConsoleUI(){
        this.reader = new BufferedReader(new InputStreamReader(System.in));
        this.myGoals = new ArrayList<>();
        //this.manager = BudgetManager.getInstance();//Na razie to zakomentowałem bo nie wiem kto sie loguje
        // stachu dodaj my goals register observer
        userBudgets.put("jan", "Dom");
        userPasswords.put("jan", "123");
        userBudgets.put("anna", "Dom");
        userPasswords.put("anna", "123");
    }
    public void start() {
        System.out.println("Witaj w Budżecie Domowym!");
        while (true) {
            String loggedBudgetId = null;
            try {
                while (loggedBudgetId == null) {
                    System.out.println("\n--- EKRAN LOGOWANIA ---");
                    System.out.println("1. Zaloguj");
                    System.out.println("0. Zamknij program");
                    System.out.print("Wybór: ");
                    String startChoice = reader.readLine();

                    if ("0".equals(startChoice)) {
                        System.out.println("Do widzenia!");
                        return;
                    }

                    if ("1".equals(startChoice)) {
                        System.out.print("Login: ");
                        String login = reader.readLine();
                        System.out.print("Hasło: ");
                        String pass = reader.readLine();

                        if (userBudgets.containsKey(login) && userPasswords.get(login).equals(pass)) {
                            loggedBudgetId = userBudgets.get(login);
                        } else {
                            System.out.println("Błąd logowania.");
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

                // Inicjalizacja Managera dopiero tutaj, gdy mamy ID!
                this.manager = BudgetManager.getInstance(loggedBudgetId);
                this.manager.registerObserver(this); // Rejestracja obserwatora
            boolean isUserLoggedIn = true;

            while (isUserLoggedIn) {
                printMenu();
                try {
                    String line = reader.readLine();
                    if (line == null || line.trim().isEmpty()) continue;
                    int choice = Integer.parseInt(line);

                    switch (choice) {
                        case 1 -> handleAddIncome();
                        case 2 -> handleAddExpense();
                        case 3 -> handleAddGoal();
                        case 4 -> handleShowReport();
                        case 5 -> handleShow();
                        case 6 -> handleExport();
                        case 7 -> {
                            System.out.println("Wylogowywanie...");
                            //this.manager.removeObserver(this);
                            this.manager = null;
                            isUserLoggedIn = false;
                        }
                        default -> System.out.println("Nieznana opcja.");
                    }
                } catch (Exception e) {
                    System.out.println("Błąd wejścia: " + e.getMessage());
                }
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
        System.out.println("7. Wyloguj / Zmień konto");
        System.out.print("Wybór: ");
    }

    private void handleAddIncome() {
        System.out.println("\n--- DODAWANIE PRZYCHODU ---\n");
        System.out.print("Podaj kwotę przychodu: ");
        try {
            String input = reader.readLine();
            double amount = Double.parseDouble(input);

            manager.addIncome(amount);

            System.out.println("Pomyślnie dodano przychód: " + amount);
        } catch (NumberFormatException e) {
            System.out.println("Błąd: To nie jest poprawna liczba.");
        } catch (IOException e) {
            System.out.println("Błąd wejścia.");
        }
    }

    private void handleAddExpense() {}

    private void handleAddGoal() {}

    private void handleShowReport() {
        System.out.println("\n--- RAPORT FINANSOWY ---");

        double income = manager.getIncome();
        double expenses = manager.getTotalExpenses();
        double savings = manager.getCurrentSavings();

        System.out.println("Całkowite Przychody: " + income);
        System.out.println("Całkowite Wydatki:   " + expenses);
        System.out.println("Bilans (Oszczędności): " + savings);
        System.out.println("\nSzczegóły kategorii:");
        prtntCategoryRecursive(manager.getRootCategory(), 0);
    }

    private void handleShowForecast() {

    }

    private void handleShow() {
        System.out.println("\n--- PROGNOZA OSZCZĘDNOŚCI ---");

        double forecast = manager.getForecast();

        System.out.println("Twoje obecne oszczędności (Przychody - Wydatki): " + forecast + " zł");

        if (forecast > 0) {
            System.out.println("Super! Jesteś na plusie.");
        } else if (forecast < 0) {
            System.out.println("Uwaga! Jesteś na minusie.");
        } else {
            System.out.println("Wychodzisz na zero.");
        }
    }

    public void printCategoryRecursive(Category cat, int level) {
        String indent = "\t".repeat(level);
        String color = cat.getState().getColorCode();
        String reset = "\u001B[0m";
        String status = cat.getState().getStatusName();

        System.out.println(indent + color + "+ " + cat.getName() + " (" + cat.getAmount() + " / " + cat.getLimit() + ") [" + status + "]" + reset);

        for (BudgetComponent child : cat.getChildren()) {
            if (child instanceof Category) {
                printCategoryRecursive((Category) child, level + 1);
            } else {
                System.out.println(indent + "\t- " + child.getName() + " (" + child.getAmount() + ")");
            }
        }
    }

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
