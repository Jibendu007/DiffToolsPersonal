package org.example;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class JSONtoXMLParser {

    private static final int INDENTATION_FACTOR = 4;

    public static void main(String[] args) {
        // Path to the JSON input file
        String inputFilePath = "/home/administrator/Migration/INCODE/IncidenceMaster_core.json";
        // Path to the XML output file
        String outputFilePath = "/home/administrator/Migration/INCODE/IncidenceMaster_Core.xml";

        try {
            // Read JSON file content
            String jsonString = readFile(inputFilePath);

            // Convert JSON to XML
            String xmlContent = jsonToXml(jsonString);

            // Write the XML content to a file
            writeFile(outputFilePath, xmlContent);

            System.out.println("JSON has been converted to XML successfully.");
        } catch (IOException e) {
            System.err.println("Error occurred: " + e.getMessage());
        }
    }

    private static String readFile(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }
        reader.close();
        return stringBuilder.toString();
    }

    private static String jsonToXml(String jsonString) {
        // Check if the JSON string starts with an array
        if (jsonString.trim().startsWith("[")) {
            // Parse JSON array
            JSONArray jsonArray = new JSONArray(jsonString);

            // Convert JSONArray to XML string
            return XML.toString(jsonArray, String.valueOf(INDENTATION_FACTOR));
        } else {
            // Parse single JSONObject
            JSONObject jsonObject = new JSONObject(jsonString);

            // Convert JSONObject to XML string
            return XML.toString(jsonObject, String.valueOf(INDENTATION_FACTOR));
        }
    }

    private static void writeFile(String filePath, String content) throws IOException {
        FileWriter writer = new FileWriter(filePath);
        writer.write(content);
        writer.close();
    }
}

