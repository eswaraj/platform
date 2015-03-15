package com.eswaraj.tasks.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class KmlFileDisector {

    private static final String BASE_DIR = "/usr/local/dev/data/eswaraj/kmls";

    public static void main(String[] args) throws Exception {
        List<String> files = new ArrayList<>();
        
        files.add("/usr/local/dev/data/eswaraj/originals/Bangalore_BBMP_Wards_Data.kml");
        /*
        files.add("/usr/local/dev/data/eswaraj/originals/DelhiCityRegion.kml");
        files.add("/usr/local/dev/data/eswaraj/originals/DelhiDistrictRegions.kml");
        files.add("/usr/local/dev/data/eswaraj/originals/DelhiMCD_Zones.kml");
        files.add("/usr/local/dev/data/eswaraj/originals/DelhiStateRegion.kml");
        files.add("/usr/local/dev/data/eswaraj/originals/DelhiSubdistricts.kml");
        files.add("/usr/local/dev/data/eswaraj/originals/DelhiSubdistricts.kml");
        
        files.add("/usr/local/dev/data/eswaraj/originals/Delhi_AC.kml");
        files.add("/usr/local/dev/data/eswaraj/originals/Delhi_PC.kml");
        */
        for (String oneFile : files) {
            disectFile(oneFile);
        }

    }

    private static void disectFile(String inputFile) throws Exception {
        FileInputStream is = new FileInputStream(inputFile);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(is);
        doc.getDocumentElement().normalize();

        String folder_name = doc.getElementsByTagName("name").item(0).getTextContent();


        NodeList coordinateList = doc.getElementsByTagName("Placemark");
        for (int i = 0; i < coordinateList.getLength(); i++) {
            Node schema = doc.getElementsByTagName("Schema").item(0).cloneNode(true);
            Document newDoc = dBuilder.newDocument();
            newDoc.adoptNode(schema);
            Element kmlElement = newDoc.createElement("kml");
            kmlElement.setAttribute("xmlns", "http://www.opengis.net/kml/2.2");
            newDoc.appendChild(kmlElement);
            
            Element documentElement = createChildElement(newDoc, kmlElement, "Document");
            Element folderElement = createChildElement(newDoc, documentElement, "Folder");
            Element nameElement = createChildElement(newDoc, folderElement, "Name");
            nameElement.setTextContent(folder_name);
            folderElement.appendChild(schema);
            Node placeMark = coordinateList.item(i).cloneNode(true);
            newDoc.adoptNode(placeMark);
            folderElement.appendChild(placeMark);
            writeToFile(newDoc);

        }
    }

    private static Element createChildElement(Document doc, Element parentElement, String childNodeName) {
        Element element = doc.createElement(childNodeName);
        parentElement.appendChild(element);
        return element;
    }

    private static void writeToFile(Document doc) throws Exception {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        String fileNameAndPath = getFileName(doc);

        StreamResult result = new StreamResult(new File(fileNameAndPath));
        transformer.transform(source, result);

        System.out.println("File saved! " + fileNameAndPath);
    }

    private static String getFileName(Document doc) {
        String stateName = null;
        String district0Name = null;
        String wardNumber = null;
        String districtName = null;
        String wardName = null;
        String cityName = null;
        String mcdZoneName = null;
        String name = null;
        String pcName = null;
        String acName = null;
        String localityName = null;
        String subLocalityName = null;
        NodeList nodeList = doc.getElementsByTagName("SimpleData");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getAttributes().item(0).getNodeValue().equalsIgnoreCase("NAME")) {
                name = node.getTextContent();
            }
            if (node.getAttributes().item(0).getNodeValue().equalsIgnoreCase("WARD_NO")) {
                wardNumber = node.getTextContent();
            }
            if (node.getAttributes().item(0).getNodeValue().equals("STATE") || node.getAttributes().item(0).getNodeValue().equals("STATE_NAME")) {
                stateName = node.getTextContent();
            }

            if (node.getAttributes().item(0).getNodeValue().equals("DISTRICT0") || node.getAttributes().item(0).getNodeValue().equals("DIST_NAME")) {
                district0Name = node.getTextContent();
            }
            if(node.getAttributes().item(0).getNodeValue().equals("DISTRICT")){
                districtName = node.getTextContent();
            }
            if (node.getAttributes().item(0).getNodeValue().equals("WARD_NAME")) {
                wardName = node.getTextContent();
            }
            if (node.getAttributes().item(0).getNodeValue().equals("CITY")) {
                cityName = node.getTextContent();
            }
            if (node.getAttributes().item(0).getNodeValue().equals("MCD_ZONE")) {
                mcdZoneName = node.getTextContent();
            }
            if (node.getAttributes().item(0).getNodeValue().equals("PC_NAME")) {
                pcName = node.getTextContent();
            }
            if (node.getAttributes().item(0).getNodeValue().equals("AC_NAME")) {
                acName = node.getTextContent();
            }
            if (node.getAttributes().item(0).getNodeValue().equals("LOCALITY")) {
                localityName = node.getTextContent();
            }
            if (node.getAttributes().item(0).getNodeValue().equals("SUB_LOC")) {
                subLocalityName = node.getTextContent();
            }

        }
        if(stateName != null && stateName.equals("Delhi")){
            stateName = "NCT of Delhi";
            
        }
        System.out.println("stateName = " + stateName + ", cityName=" + cityName + ", district0Name=" + district0Name + ",districtName=" + districtName + ",wardName=" + wardName + ",mcdZoneName="
                + mcdZoneName);
        String directory = BASE_DIR;
        String parentDirectory = BASE_DIR;
        String fileName = null;
        if (stateName != null) {
            directory = directory + "/states/";
            parentDirectory = directory;
            createDirectory(directory);
            directory = directory + stateName;
            createDirectory(directory);
            fileName = stateName + ".kml";
        }
        if (pcName != null) {
            directory = directory + "/pcs/";
            parentDirectory = directory;
            createDirectory(directory);
            directory = directory + pcName;
            createDirectory(directory);
            fileName = pcName + ".kml";
        }
        if (acName != null) {
            directory = directory + "/acs/";
            parentDirectory = directory;
            createDirectory(directory);
            directory = directory + acName;
            createDirectory(directory);
            fileName = acName + ".kml";
        }
        if (cityName != null) {
            directory = directory + "/cities/";
            parentDirectory = directory;
            createDirectory(directory);
            directory = directory + cityName;
            createDirectory(directory);
            fileName = cityName + ".kml";
        }
        if (district0Name != null) {
            directory = directory + "/districts/";
            parentDirectory = directory;
            createDirectory(directory);
            directory = directory + district0Name;
            createDirectory(directory);
            fileName = district0Name + ".kml";
        }
        if (districtName != null) {
            directory = directory + "/subdistricts/";
            parentDirectory = directory;
            createDirectory(directory);
            directory = directory + districtName;
            createDirectory(directory);
            fileName = districtName + ".kml";
        }
        if (wardName != null || wardNumber != null) {
            directory = directory + "/wards/";
            parentDirectory = directory;
            createDirectory(directory);
            if (wardNumber != null) {
                directory = directory + wardNumber;
                createDirectory(directory);
                fileName = wardNumber + ".kml";
            } else {
                directory = directory + wardName;
                createDirectory(directory);
                fileName = wardName + ".kml";
            }

        }
        if (mcdZoneName != null) {
            directory = directory + "/mcdzones/";
            parentDirectory = directory;
            createDirectory(directory);
            directory = directory + mcdZoneName;
            createDirectory(directory);
            System.out.println("creating Directory : " + directory);
            fileName = mcdZoneName + ".kml";
        }
        if (localityName != null) {
            directory = directory + "/locality/";
            parentDirectory = directory;
            createDirectory(directory);
            directory = directory + localityName;
            createDirectory(directory);
            fileName = localityName + ".kml";
        }
        if (subLocalityName != null) {
            directory = directory + "/sublocality/";
            parentDirectory = directory;
            createDirectory(directory);
            directory = directory + subLocalityName;
            createDirectory(directory);
            fileName = subLocalityName + ".kml";
        }
        if (name != null) {
            if (wardNumber != null) {
                fileName = wardNumber + " - " + name + ".kml";
            } else {
                fileName = name + ".kml";
            }

        }
        return parentDirectory + "/" + fileName;
    }

    private static void createDirectory(String directory) {
        System.out.println("creating Directory : " + directory);
        File dir = new File(directory);
        dir.mkdir();
    }
}
