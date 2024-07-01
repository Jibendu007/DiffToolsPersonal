package org.example;

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

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class RemoveDuplicates {

    public static void main(String[] args) {
        try {
            // Define the input and output file paths
            String inputFile = "/home/administrator/Migration/CORE/IncidenceConfig/infinal.xml";
            String outputFile = "/home/administrator/Migration/CORE/IncidenceConfig/outfinal.xml";

            // Parse the XML file
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(inputFile));
            doc.getDocumentElement().normalize();

            // Get all 'IncidenceConfig' elements
            NodeList incidenceList = doc.getElementsByTagName("IncidenceConfig");

            // Use a Set to track unique combinations
            Set<String> uniqueCombinations = new HashSet<>();

            // Iterate through the NodeList in reverse to safely remove nodes
            for (int i = incidenceList.getLength() - 1; i >= 0; i--) {
                Node incidence = incidenceList.item(i);

                if (incidence.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) incidence;

                    // Create a unique key based on the specified elements
                    String combination = element.getElementsByTagName("IncdnceCd").item(0).getTextContent() +
                            element.getElementsByTagName("PrcCd").item(0).getTextContent() +
                            element.getElementsByTagName("WorkflwCd").item(0).getTextContent() +
                            element.getElementsByTagName("MsgFctnCd").item(0).getTextContent() +
                            element.getElementsByTagName("BizFctnCd").item(0).getTextContent();

                    // Check if this combination is already in the set
                    if (uniqueCombinations.contains(combination)) {
                        // If it is, remove this 'IncidenceConfig' element from the document
                        element.getParentNode().removeChild(element);
                    } else {
                        // If not, add the combination to the set
                        uniqueCombinations.add(combination);
                    }
                }
            }

            // Write the modified XML to a new file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(outputFile));
            transformer.transform(source, result);

            System.out.println("Duplicates removed. Output saved to " + outputFile);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
