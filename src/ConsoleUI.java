import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class ConsoleUI implements BudgetObserver {
    private BufferedReader reader;
    private BudgetManager manager;

    private Map<String, String> userBudgets = new HashMap<>();
    private Map<String, String> userPasswords = new HashMap<>();

    public ConsoleUI() {
        this.reader = new BufferedReader(new InputStreamReader(System.in));
        // this.manager = BudgetManager.getInstance();//Na razie to zakomentowałem bo
        // nie wiem kto sie loguje

        userBudgets.put("jan", "Dom");
        userPasswords.put("jan", "123");
        userBudgets.put("anna", "Firma");
        userPasswords.put("anna", "123");
        userBudgets.put("marcin", "dom");
        userPasswords.put("marcin", "123");
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

            this.manager = BudgetManager.getInstance(loggedBudgetId);
            this.manager.registerObserver(this);
            boolean isUserLoggedIn = true;

            java.time.LocalDate now = java.time.LocalDate.now();
            manager.changeMonth(now.getYear(), now.getMonthValue());

            while (isUserLoggedIn) {
                printMenu();
                try {
                    String line = reader.readLine();
                    if (line == null || line.trim().isEmpty())
                        continue;
                    int choice = Integer.parseInt(line);

                    switch (choice) {
                        case 1 -> handleAddIncome();
                        case 2 -> handleAddExpense();
                        case 3 -> handleAddCategory();
                        case 4 -> handleNextState();
                        case 5 -> handleChangeMonth();
                        case 6 -> handleShowReport();
                        case 7 -> handleExport();
                        case 0 -> {
                            System.out.println("Zamykanie...");
                            isUserLoggedIn = false;
                            return;
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
        BudgetMonth current = manager.getCurrentMonth();
        String monthStr = (current != null) ? (current.getYear() + "-" + current.getMonth()) : "Brak";

        String stateName = "N/A";
        String nextStateAction = "Zmień status budżetu";

        if (current != null) {
            BudgetLifecycleState state = current.getLifecycleState();
            stateName = state.getStateName();

            if (state instanceof DraftState) {
                nextStateAction = "Zatwierdź plan (-> Aktywuj budżet)";
            } else if (state instanceof ActiveState) {
                nextStateAction = "Zamknij miesiąc (-> Zablokuj edycję)";
            } else if (state instanceof ClosedState) {
                nextStateAction = "(Miesiąc zamknięty - brak akcji)";
            }
        }

        System.out.println("\n--- MENU [" + monthStr + "] Status: " + stateName + " ---");
        System.out.println("1. Dodaj przychód");
        System.out.println("2. Dodaj wydatek (Transakcja)");
        System.out.println("3. Dodaj kategorię (Struktura)");
        System.out.println("4. " + nextStateAction);
        System.out.println("5. Przełącz miesiąc");
        System.out.println("6. Pokaż raport");
        System.out.println("7. Eksportuj dane");
        System.out.println("0. Wyjście");
        System.out.print("Wybór: ");
    }

    private void handleChangeMonth() {
        try {
            System.out.print("Podaj rok (np. 2024): ");
            int year = Integer.parseInt(reader.readLine());
            System.out.print("Podaj miesiąc (1-12): ");
            int month = Integer.parseInt(reader.readLine());
            manager.changeMonth(year, month);
            System.out.println("Przełączono na miesiąc: " + year + "-" + month);
        } catch (Exception e) {
            System.out.println("Błąd danych.");
        }
    }

    private void handleNextState() {
        manager.nextState();
    }

    private void handleAddCategory() {
        try {
            System.out.println("\n--- OBECNA STRUKTURA KATEGORII ---");
            if (manager.getRootCategory() != null) {
                printCategoryRecursive(manager.getRootCategory(), 0);
            }

            System.out.println("\n--- DODAWANIE KATEGORII ---");
            System.out.print("Nazwa kategorii nadrzędnej (lub '" + manager.getRootCategory().getName() + "'): ");
            String parent = reader.readLine();
            System.out.print("Nazwa nowej kategorii: ");
            String name = reader.readLine();
            System.out.print("Limit wydatków: ");
            double limit = Double.parseDouble(reader.readLine());

            manager.addSubCategory(parent, name, limit);
        } catch (Exception e) {
            System.out.println("Błąd wejścia.");
        }
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

    private void handleAddExpense() {
        try {
            System.out.print("Kategoria: ");
            String cat = reader.readLine();
            System.out.print("Kwota: ");
            double amount = Double.parseDouble(reader.readLine());
            System.out.print("Opis: ");
            String desc = reader.readLine();

            manager.addTransaction(cat, amount, desc);
        } catch (Exception e) {
            System.out.println("Błąd danych.");
        }
    }

    private void handleShowReport() {
        System.out.println("\n--- RAPORT FINANSOWY ---");
        if (manager.getCurrentMonth() == null) {
            System.out.println("Brak wybranego miesiąca.");
            return;
        }

        double income = manager.getIncome();
        double expenses = manager.getTotalExpenses();
        double savings = manager.getCurrentSavings();

        System.out.println("Całkowite Przychody: " + income);
        System.out.println("Całkowite Wydatki:   " + expenses);
        System.out.println("Bilans: " + savings);
        System.out.println("\nSzczegóły kategorii:");
        printCategoryRecursive(manager.getRootCategory(), 0);
    }

    public void printCategoryRecursive(Category cat, int level) {
        String indent = "\t".repeat(level);
        String color = cat.getState().getColorCode();
        String reset = "\u001B[0m";
        String status = cat.getState().getStatusName();

        System.out.println(indent + color + "+ " + cat.getName() + " (" + cat.getAmount() + " / " + cat.getLimit()
                + ") [" + status + "]" + reset);

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

    public void update(double totalExpenses, double totalIncome) {
    }
}
