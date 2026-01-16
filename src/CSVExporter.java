import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CSVExporter implements FileExporter {

    @Override
    public void export(BudgetComponent root, BudgetMonth currentMonth) {
        int Month = currentMonth.getMonth();
        int Year = currentMonth.getYear();
        System.out.println("\tZAPIS DO PLIKU CSV");

        String directoryPath = "exports/CSVExport/";
        LocalDateTime now = LocalDateTime.now();
        String timeId = now.format(DateTimeFormatter.ofPattern("yyMMddHHmmss"));

        String fileName = directoryPath + Month + "_" + Year + "_" + timeId + ".csv";

        File directory = new File(directoryPath);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (!created) {
                System.out.println("Błąd: Nie można utworzyć katalogu " + directoryPath + "\n");
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
                CSVPrinter printer = new CSVPrinter(writer,
                        CSVFormat.DEFAULT.withHeader("nazwa", "typ", "ilość", "limit", "ścieżka"))) {

            buildCSVRecursive(root, "", printer);

            System.out.println("Eksport zakończony pomyślnie");
        } catch (IOException e) {
            System.out.println("Błąd podczas eksportu: " + e.getMessage());
        }
    }

    private void buildCSVRecursive(BudgetComponent root, String path, CSVPrinter printer) throws IOException {
        String currentPath = path.isEmpty() ? root.getName() : path + "/" + root.getName();

        if (root instanceof Category) {
            Category category = (Category) root;
            printer.printRecord(root.getName(), root.getType(), root.getAmount(), category.getLimit(), currentPath);

            for (BudgetComponent child : category.getChildren()) {
                buildCSVRecursive(child, currentPath, printer);
            }
        } else {
            printer.printRecord(root.getName(), root.getType(), root.getAmount(), "", currentPath);
        }
    }
}
