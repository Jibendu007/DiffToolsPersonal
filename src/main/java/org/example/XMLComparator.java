package org.example;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class XMLComparator {

    public static void main(String[] args) {
        try {
            // Paths to the XML files
            String fileAPath = "/home/administrator/Migration/INCODE/CORE.xml";
            String fileBPath = "/home/administrator/Migration/INCODE/combined.xml";

            // Extract identifiers from both files
            Set<String> identifiersA = extractIdentifiers(fileAPath);
            Set<String> identifiersB = extractIdentifiers(fileBPath);

            // Find entries in file B that are not in file A
            identifiersB.removeAll(identifiersA);

            // Output the result
            System.out.println("Number of entries in file B that are not in file A: " + identifiersB.size());
            System.out.println("Entries in file B not in file A:");
            for (String entry : identifiersB) {
                System.out.println(entry);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Set<String> extractIdentifiers(String filePath) throws Exception {
        Set<String> identifiers = new HashSet<>();

        File xmlFile = new File(filePath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(xmlFile);
        doc.getDocumentElement().normalize();

        NodeList nList = doc.getElementsByTagName("IncidenceConfig");

        for (int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) nNode;
                String incdnceCd = element.getElementsByTagName("IncdnceCd").item(0).getTextContent();
                //String bizFctnCd = element.getElementsByTagName("BizFctnCd").item(0).getTextContent();
                String identifier = incdnceCd ;//+ "+" + bizFctnCd; // Combining the values
                identifiers.add(identifier);

            }
        }

        return identifiers;
    }
}
