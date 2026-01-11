import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class JSONExporter implements FileExporter {

    @Override
    public void export(BudgetComponent root) {
        System.out.println("\tZAPIS DO PLIKU JSON");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/out.json"))) {
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
        json.addProperty("name", root.getName());
        json.addProperty("type", root.getType());
        json.addProperty("amount", root.getAmount());

        if (root instanceof Category) {
            Category category = (Category) root;
            json.addProperty("limit", category.getLimit());

            JsonArray children = new JsonArray();
            for (BudgetComponent child : category.getChildren()) {
                children.add(buildJsonRecursive(child));
            }
            json.add("children", children);
        }
        return json;
    }
}
