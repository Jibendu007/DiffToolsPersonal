package org.example;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class XMLModifier {

    public static void main(String[] args) {
        // Hard-coded file path
        String filePath = "/home/administrator/Migration/CORE/IncidenceConfig/IncidenceCfg_core.v3.3.0.xml";

        File file = new File(filePath);
        if (file.exists() && file.isFile() && file.getName().endsWith(".xml")) {
            System.out.println("Processing file: " + filePath);
            updateXMLFile(file);
        } else {
            System.out.println("Invalid file path: " + filePath);
        }
    }

    private static void updateXMLFile(File file) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("IncidenceConfig");

            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    eElement.getElementsByTagName("IncdnceId").item(0).setTextContent("INCIDENCE" + (i + 1));
                }
            }

            // Write the updated XML back to the file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);

            System.out.println("Updated file: " + file.getAbsolutePath());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
