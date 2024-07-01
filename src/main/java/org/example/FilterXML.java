package org.example;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FilterXML {

    public static void main(String[] args) {
        String incidenceCodesFilePath = "/home/administrator/Migration/INCODE/incidence_notincore.txt";
        String inputFilePath = "/home/administrator/Migration/INCODE/combined.xml";
        String outputFilePath = "/home/administrator/Migration/INCODE/incidencetobeadded_core.xml";

        try {
            // Step 1: Read incidence codes from the text file
            Set<String> incidenceCodes = readIncidenceCodes(incidenceCodesFilePath);

            // Step 2: Read the input XML file
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new File(inputFilePath));
            doc.getDocumentElement().normalize();

            // Step 3: Filter <IncidenceConfig> elements based on the incidence codes
            NodeList incidenceList = doc.getElementsByTagName("IncidenceConfig");
            List<Element> filteredIncidences = new ArrayList<>();
            for (int i = 0; i < incidenceList.getLength(); i++) {
                Element incidence = (Element) incidenceList.item(i);
                String incdncCd = getElementValue(incidence, "IncdnceCd");
                if (incidenceCodes.contains(incdncCd)) {
                    filteredIncidences.add(incidence);
                }
            }

            // Step 4: Create a new XML document for the output
            Document newDoc = dBuilder.newDocument();
            Element rootElement = newDoc.createElement("Incidences");
            newDoc.appendChild(rootElement);

            // Append filtered elements to the new document
            for (Element incidence : filteredIncidences) {
                Node importedNode = newDoc.importNode(incidence, true);
                rootElement.appendChild(importedNode);
            }

            // Step 5: Write the new XML document to the output file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(newDoc);
            StreamResult result = new StreamResult(new File(outputFilePath));
            transformer.transform(source, result);

            System.out.println("Filtered XML file created successfully: " + outputFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Helper method to read incidence codes from a text file
    private static Set<String> readIncidenceCodes(String filePath) throws IOException {
        Set<String> codes = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                codes.add(line.trim());
            }
        }
        return codes;
    }

    // Helper method to get the text content of a child element by tag name
    private static String getElementValue(Element parent, String tagName) {
        NodeList nodeList = parent.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        return null;
    }
}
