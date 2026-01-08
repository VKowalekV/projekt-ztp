import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


public class CSVExporter implements FileExporter{

    @Override
    public void export(BudgetComponent root){
        System.out.println("\tZAPIS DO PLIKU CSV");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/out.csv"));
             CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("Name", "Type", "Amount", "Limit", "Path"))){

            buildCSVRecursive(root, "", printer);

            System.out.println("Eksport zakończony pomyślnie");
        } catch (IOException e){
            System.out.println("Błąd podczas eksportu: " + e.getMessage());
        }
    }

    private void buildCSVRecursive(BudgetComponent root, String path, CSVPrinter printer) throws IOException {
        String currentPath = path.isEmpty() ? root.getName() : path + "/" + root.getName();

        if (root instanceof Category){
            Category category = (Category) root;
            printer.printRecord(root.getName(), root.getType(), root.getAmount(), category.getLimit(), currentPath);

            for (BudgetComponent child : category.getChildren()){
                buildCSVRecursive(child, currentPath, printer);
            }
        } else {
            printer.printRecord(root.getName(), root.getType(), root.getAmount(), "", currentPath);
        }
    }
}
