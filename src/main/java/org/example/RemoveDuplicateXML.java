package org.example;


import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class RemoveDuplicateXML {

    public static void main(String[] args) {
        try {
            File inputFile = new File("/home/administrator/Migration/CORE/IncidenceConfig/IncidenceCfg_core.v3.3.0.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("IncidenceConfig");

            Set<String> seen = new HashSet<>();
            for (int i = 0; i < nList.getLength(); i++) {
                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String incdnceCd = element.getElementsByTagName("IncdnceCd").item(0).getTextContent();
                    String bizFctnCd = element.getElementsByTagName("BizFctnCd").item(0).getTextContent();
                    String prcSts = element.getElementsByTagName("PrcSts").item(0).getTextContent();
                    String key = incdnceCd + bizFctnCd + prcSts;

                    if (seen.contains(key)) {
                        element.getParentNode().removeChild(element);
                        i--; // Decrement i as NodeList is live
                    } else {
                        seen.add(key);
                    }
                }
            }

            // Write the content into an output XML file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("/home/administrator/Migration/CORE/IncidenceConfig/output.xml"));
            transformer.transform(source, result);

            System.out.println("Duplicate elements removed and output.xml generated.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
