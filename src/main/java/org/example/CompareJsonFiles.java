package org.example;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class CompareJsonFiles {

    public static void main(String[] args) {
        String file1Path = "/home/administrator/Migration/Compare_DBS/118-4/LocaleMaster_118-4.json";
        String file2Path = "/home/administrator/Migration/Compare_DBS/singlesdk/LocaleMaster.json";
        String outputFilePath = "/home/administrator/Migration/Compare_DBS/difference.json";

        try {
            JsonArray jsonArray1 = readJsonFile(file1Path);
            JsonArray jsonArray2 = readJsonFile(file2Path);

            Map<String, JsonObject> file1Map = new HashMap<>();
            Set<String> file2Ids = new HashSet<>();

            for (JsonElement element : jsonArray1) {
                JsonObject jsonObject = element.getAsJsonObject();
                String id = jsonObject.get("_id").getAsString();
                file1Map.put(id, jsonObject);
            }

            for (JsonElement element : jsonArray2) {
                JsonObject jsonObject = element.getAsJsonObject();
                String id = jsonObject.get("_id").getAsString();
                file2Ids.add(id);
            }

            JsonArray differenceArray = new JsonArray();
            for (Map.Entry<String, JsonObject> entry : file1Map.entrySet()) {
                if (!file2Ids.contains(entry.getKey())) {
                    differenceArray.add(entry.getValue());
                }
            }

            saveJsonToFile(differenceArray, outputFilePath);
            System.out.println("Differences saved to " + outputFilePath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static JsonArray readJsonFile(String filePath) throws IOException {
        FileReader reader = new FileReader(filePath);
        JsonElement jsonElement = JsonParser.parseReader(reader);
        reader.close();
        return jsonElement.getAsJsonArray();
    }

    private static void saveJsonToFile(JsonArray jsonArray, String filePath) throws IOException {
        FileWriter writer = new FileWriter(filePath);
        writer.write(jsonArray.toString());
        writer.close();
    }
}
