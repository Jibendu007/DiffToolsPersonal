package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.File;
import java.io.IOException;

public class JSONModifier {

    public static void main(String[] args) {
        // Hard-coded file paths
        String[] filePaths = {
                "/home/administrator/Migration/CORE/IncidenceConfig/IncidenceConfig_SEPAIP.json",
                "/home/administrator/Migration/CORE/IncidenceConfig/IncidenceConfig_SEPACT.json",
                "/home/administrator/Migration/CORE/IncidenceConfig/IncidenceConfig_ACH.json",
                "/home/administrator/Migration/CORE/IncidenceConfig/IncidenceConfig_FEDNOW.json",
                "/home/administrator/Migration/CORE/IncidenceConfig/IncidenceConfig_RTP.json",
                "/home/administrator/Migration/CORE/IncidenceConfig/IncidenceConfig_SWIFT.json"
        };

        for (String filePath : filePaths) {
            File file = new File(filePath);
            if (file.exists() && file.isFile() && file.getName().endsWith(".json")) {
                System.out.println("Processing file: " + filePath);
                updateJSONFile(file);
            } else {
                System.out.println("Invalid file path: " + filePath);
            }
        }
    }

    private static void updateJSONFile(File file) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            // Read the JSON file
            JsonNode rootNode = mapper.readTree(file);
            boolean modified = false;

            // Check if rootNode is an array
            if (rootNode.isArray()) {
                ArrayNode arrayNode = (ArrayNode) rootNode;
                for (JsonNode node : arrayNode) {
                    if (node.isObject() && updateObjectNode((ObjectNode) node)) {
                        modified = true;
                    }
                }
            } else if (rootNode.isObject()) {
                if (updateObjectNode((ObjectNode) rootNode)) {
                    modified = true;
                }
            }

            // Write the updated JSON back to the file if modified
            if (modified) {
                mapper.writerWithDefaultPrettyPrinter().writeValue(file, rootNode);
                System.out.println("Updated file: " + file.getAbsolutePath());
            } else {
                System.out.println("No changes needed for file: " + file.getAbsolutePath());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean updateObjectNode(ObjectNode objectNode) {
        if (!objectNode.has("PrcSts")) {
            objectNode.put("PrcSts", "NONE");
            System.out.println("Added PrcSts to object: " + objectNode);
            return true;
        }
        return false;
    }
}

