package org.example;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RemoveAndSortXML {

    public static void main(String[] args) {
        try {
            File inputFile = new File("/home/administrator/Migration/CORE/IncidenceConfig/input.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("IncidenceConfig");

            Map<String, Set<String>> seen = new HashMap<>();
            for (int i = 0; i < nList.getLength(); i++) {
                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String incdnceCd = element.getElementsByTagName("IncdnceCd").item(0).getTextContent();
                    String bizFctnCd = element.getElementsByTagName("BizFctnCd").item(0).getTextContent();
                    String prcSts = element.getElementsByTagName("PrcSts").item(0).getTextContent();
                    String key = incdnceCd + prcSts;

                    Set<String> bizFctnCdSet = seen.getOrDefault(key, new HashSet<>());

                    if (bizFctnCdSet.contains("ALL")) {
                        element.getParentNode().removeChild(element);
                        i--; // Decrement i as NodeList is live
                    } else {
                        if (bizFctnCd.equals("ALL")) {
                            bizFctnCdSet.clear();
                        }
                        bizFctnCdSet.add(bizFctnCd);
                        seen.put(key, bizFctnCdSet);
                    }
                }
            }

            // Write the content into an output XML file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("/home/administrator/Migration/CORE/IncidenceConfig/output.xml"));
            transformer.transform(source, result);

            System.out.println("Duplicate elements removed and output.xml generated.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
