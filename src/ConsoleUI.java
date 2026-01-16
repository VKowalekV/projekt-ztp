import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class ConsoleUI implements BudgetObserver {
    private BufferedReader reader;
    private BudgetManager manager;

    private Map<String, String> userBudgets = new HashMap<>();
    private Map<String, String> userPasswords = new HashMap<>();

    private List<SavingsGoals> savingsGoals = new ArrayList<>();

    public ConsoleUI() {
        this.reader = new BufferedReader(new InputStreamReader(System.in));

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
                        case 4 -> handleAddSavingsGoal();
                        case 5 -> handleNextState();
                        case 6 -> handleChangeMonth();
                        case 7 -> handleShowReport();
                        case 8 -> handleExport();
                        case 9 -> handleShowForecast();
                        case 0 -> {
                            System.out.println("Zamykanie...");
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
        System.out.println("4. Dodaj cel oszczędzania");
        System.out.println("5. " + nextStateAction);
        System.out.println("6. Przełącz miesiąc");
        System.out.println("7. Pokaż raport");
        System.out.println("8. Eksportuj dane");
        System.out.println("9. Pokaż prognozę");
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

    private void handleAddSavingsGoal() {
        try {
            System.out.println("\n--- OBECNE CELE OSZCZEDZANIA ---");
            if (savingsGoals.isEmpty()) {
                System.out.println("Brak zarejestrowanych celów.");
            } else {
                for (SavingsGoals goal : savingsGoals) {
                    System.out.println(goal.getStatus());
                }
            }

            System.out.println("\n--- DODAWANIE CELU OSZCZEDZANIA ---");
            System.out.print("Nazwa celu: ");
            String name = reader.readLine();
            System.out.print("Kwota docelowa: ");
            double target = Double.parseDouble(reader.readLine());

            SavingsGoals newGoal = manager.addSavingsGoal(name, target);
            savingsGoals.add(newGoal);

            System.out.println("Dodano cel: " + newGoal.getStatus());
        } catch (Exception e) {
            System.out.println("Błąd przy dodawaniu celu.");
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
    private void handleShowForecast() {
            System.out.println("\n=== SYMULACJA BUDŻETOWA ===");

            if (manager.getCurrentMonth() == null) {
                System.out.println("Błąd: Nie wybrano miesiąca.");
                return;
            }

            double actualSavings = manager.getCurrentSavings();
            double forecastSavings = manager.getForecast();

            System.out.println("Stan konta na DZIŚ: " + String.format("%.2f", actualSavings) + " zł");

            System.out.println("---------------------------------");
            System.out.print("Prognoza na KONIEC miesiąca: ");

            if (forecastSavings > 0) {
                System.out.println("\u001B[32m" + String.format("%.2f", forecastSavings) + " zł (NA PLUSIE)\u001B[0m");
                System.out.println("Komentarz: Wydajesz rozsądnie. Utrzymaj to tempo!");
            } else {
                System.out.println("\u001B[31m" + String.format("%.2f", forecastSavings) + " zł (ZAGROŻENIE)\u001B[0m");
                System.out.println("Komentarz: UWAGA! Wydajesz za szybko. Jeśli nie zwolnisz, zabraknie Ci "
                        + String.format("%.2f", Math.abs(forecastSavings)) + " zł.");
            }
            System.out.println("---------------------------------");
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
        double totalAllocated = 0.0;
        for (SavingsGoals sg : savingsGoals) {
            totalAllocated += sg.getAllocated();
        }
        double available = manager.getIncome() - manager.getTotalExpenses() - totalAllocated;
        if (available > 1e-9) {
            List<SavingsGoals> needy = new ArrayList<>();
            for (SavingsGoals sg : savingsGoals) if (sg.getRemainingNeed() > 0.0) needy.add(sg);
            while (available > 1e-6 && !needy.isEmpty()) {
                int n = needy.size();
                double per = available / n;
                boolean any = false;
                java.util.Iterator<SavingsGoals> it = needy.iterator();
                while (it.hasNext()) {
                    SavingsGoals sg = it.next();
                    double need = sg.getRemainingNeed();
                    double toAlloc = Math.min(per, need);
                    if (toAlloc > 0.0) {
                        double actually = sg.allocate(toAlloc);
                        available -= actually;
                        any = true;
                        System.out.println(String.format("Przydzielono %.2f do celu '%s' (%.2f/%.2f)", actually, sg.getGoalName(), sg.getAllocated(), sg.getAllocated() + sg.getRemainingNeed()));
                    }
                    if (sg.getRemainingNeed() <= 1e-6) it.remove();
                }
                if (!any) break;
            }
        }

        if (manager == null || manager.getRootCategory() == null) return;
        checkExceededRecursive(manager.getRootCategory());
    }

    private void checkExceededRecursive(Category cat) {
        if (cat == null) return;
        double amount = cat.getAmount();
        double limit = cat.getLimit();
        if (limit > 0) {
            if (amount >= limit) {
                System.out.println(String.format("Uważaj: przekroczyłeś budżet w kategorii '%s': %.2f/%.2f", cat.getName(), amount, limit));
            } else if (amount >= 0.8 * limit) {
                System.out.println(String.format("Uwaga: wydatki osiągnęły 80%% budżetu w kategorii '%s': %.2f/%.2f", cat.getName(), amount, limit));
            }
        }
        for (BudgetComponent child : cat.getChildren()) {
            if (child instanceof Category) {
                checkExceededRecursive((Category) child);
            }
        }
    }
}
