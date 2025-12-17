import java.util.Scanner;

public class ConsoleUI {
    private BudgetManager manager;

    public ConsoleUI(){
        this.manager = new BudgetManager();
    }

    public void start(){
        handleExport();
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
}
