import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class JSONExporter implements FileExporter {

    @Override
    public void export(BudgetComponent root, BudgetMonth currentMonth) {
        int Month = currentMonth.getMonth();
        int Year = currentMonth.getYear();
        System.out.println("\tZAPIS DO PLIKU JSON");

        String directoryPath = "exports/JSONExport/";
        LocalDateTime now = LocalDateTime.now();
        String timeId = now.format(DateTimeFormatter.ofPattern("yyMMddHHmmss"));

        String fileName = directoryPath + Month + "_" + Year + "_" + timeId + ".json";

        File directory = new File(directoryPath);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (!created) {
                System.out.println("Błąd: Nie można utworzyć katalogu " + directoryPath + "\n");
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonObject rootJson = buildJsonRecursive(root);
            writer.write(gson.toJson(rootJson));
            System.out.println("Eksport zakończony pomyślnie");
        } catch (IOException e) {
            System.out.println("Błąd podczas eksportu: " + e.getMessage());
        }
    }

    private JsonObject buildJsonRecursive(BudgetComponent root) {
        JsonObject json = new JsonObject();
        json.addProperty("Name", root.getName());
        json.addProperty("Type", root.getType());
        json.addProperty("Amount", root.getAmount());

        if (root instanceof Category) {
            Category category = (Category) root;
            json.addProperty("Limit", category.getLimit());

            JsonArray children = new JsonArray();
            for (BudgetComponent child : category.getChildren()) {
                children.add(buildJsonRecursive(child));
            }
            json.add("children", children);
        }
        return json;
    }
}
