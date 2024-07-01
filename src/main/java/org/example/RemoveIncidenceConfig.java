package org.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class RemoveIncidenceConfig {

    public static void main(String[] args) {
        try {
            // Read incidence codes from the text file
            Set<String> incidenceCodes = new HashSet<>();
            BufferedReader reader = new BufferedReader(new FileReader("/home/administrator/Migration/INCODE/incidencemaster_core.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                incidenceCodes.add(line.trim());
            }
            reader.close();

            // Parse the XML file
            File inputFile = new File("/home/administrator/Migration/INCODE/IncidenceConfigUnique_Remaining.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            // Create a new document for removed elements
            Document removedDoc = dBuilder.newDocument();
            Element rootElement = removedDoc.createElement("RemovedIncidenceConfigs");
            removedDoc.appendChild(rootElement);

            // Find and separate matching elements
            NodeList nList = doc.getElementsByTagName("IncidenceConfig");
            for (int i = 0; i < nList.getLength(); i++) {
                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String incdnceCd = element.getElementsByTagName("IncdnceCd").item(0).getTextContent();
                    if (incidenceCodes.contains(incdnceCd)) {
                        Node importedNode = removedDoc.importNode(element, true);
                        rootElement.appendChild(importedNode);
                        element.getParentNode().removeChild(element);
                        i--; // Decrement the index to account for the removed element
                    }
                }
            }

            // Write the remaining XML to the original file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("/home/administrator/Migration/INCODE/modified_incidence_config.xml"));
            transformer.transform(source, result);

            // Write the removed elements to a new file
            DOMSource removedSource = new DOMSource(removedDoc);
            StreamResult removedResult = new StreamResult(new File("/home/administrator/Migration/INCODE/removed_incidence_config.xml"));
            transformer.transform(removedSource, removedResult);

            System.out.println("Modified XML file saved as modified_incidence_config.xml");
            System.out.println("Removed elements saved as removed_incidence_config.xml");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


