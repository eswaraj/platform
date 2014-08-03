package com.eswaraj.tasks.bolt;

import java.awt.Rectangle;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.concurrent.atomic.AtomicLong;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.eswaraj.core.exceptions.ApplicationException;

public class GeoPtTest {

    public static void main(String[] args) throws ApplicationException {
        // TODO Auto-generated method stub
        GeoPtTest geoPtTest = new GeoPtTest();
        geoPtTest.processBoundaryFile(1L, 2L, true);
        ;
    }

    private void processBoundaryFile(Long locationId, Long boundaryFileId, boolean add) throws ApplicationException {
        try {
            FileInputStream is = new FileInputStream("/Users/Ravi/Downloads/KML_zip_20140801063847.kml");

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);
            doc.getDocumentElement().normalize();

            NodeList coordinateList = doc.getElementsByTagName("coordinates");
            String coordinates = null;
            AtomicLong totalPointsMissed = new AtomicLong(0);
            AtomicLong totalPointsProcessed = new AtomicLong(0);
            for (int temp = 0; temp < coordinateList.getLength(); temp++) {
                Node nNode = coordinateList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    coordinates = eElement.getTextContent();
                    processCoordinates(coordinates, locationId, add, totalPointsMissed, totalPointsProcessed);
                }
            }
        } catch (IOException ioe) {
            throw new ApplicationException(ioe);
        } catch (ParserConfigurationException pce) {
            throw new ApplicationException(pce);
        } catch (SAXException se) {
            throw new ApplicationException(se);
        }
    }

    private void processCoordinates(String coordinates, Long locationId, boolean add, AtomicLong totalPointsMissed, AtomicLong totalPointsProcessed) throws ApplicationException {

        Path2D myPolygon = createPolygon(coordinates);
        Rectangle coveringRectangle = myPolygon.getBounds();
        MathContext topLeftMc = new MathContext(3, RoundingMode.DOWN);
        BigDecimal topLeftLat = new BigDecimal(coveringRectangle.getMinX()).round(topLeftMc);
        BigDecimal topLeftLong = new BigDecimal(coveringRectangle.getMinY()).round(topLeftMc);
        MathContext bottomRightMc = new MathContext(3, RoundingMode.UP);
        BigDecimal bottomRightLat = new BigDecimal(coveringRectangle.getMaxX()).round(bottomRightMc);
        BigDecimal bottomRightLong = new BigDecimal(coveringRectangle.getMaxY()).round(bottomRightMc);
        BigDecimal addedValue = new BigDecimal(.001);
        Point2D onePoint;
        int i = 0;
        topLeftLat = new BigDecimal(8.080).round(topLeftMc);
        logInfo("topLeftLat = " + topLeftLat);
        logInfo("topLeftLong = " + topLeftLong);
        logInfo("bottomRightLat = " + bottomRightLat);
        logInfo("bottomRightLong = " + bottomRightLong);

        for (BigDecimal latitude = topLeftLat; latitude.compareTo(bottomRightLat) <= 0; latitude = latitude.add(addedValue)) {
            for (BigDecimal longitude = topLeftLong; longitude.compareTo(bottomRightLong) <= 0; longitude = longitude.add(addedValue)) {
                onePoint = new Point2D.Double(latitude.doubleValue(), longitude.doubleValue());
                i++;
                processOnePoint(myPolygon, onePoint, locationId, add, totalPointsMissed, totalPointsProcessed);
            }
        }

    }

    private void logInfo(String message) {
        System.out.println(message);
    }

    private Path2D createPolygon(String coordinates) {
        String[] locationPoints = coordinates.split(" ");
        String[] latLong;
        BigDecimal oneLat, oneLong;
        Path2D myPolygon = new Path2D.Double();
        boolean first = true;
        for (String oneLocationPoint : locationPoints) {
            latLong = oneLocationPoint.split(",");
            oneLong = new BigDecimal(latLong[0]);
            oneLat = new BigDecimal(latLong[1]);
            if (first) {
                myPolygon.moveTo(oneLat.doubleValue(), oneLong.doubleValue());
                first = false;
            } else {
                myPolygon.lineTo(oneLat.doubleValue(), oneLong.doubleValue());
            }
        }
        myPolygon.closePath();
        return myPolygon;
    }

    private void processOnePoint(Path2D myPolygon, Point2D onePoint, Long locationId, boolean add, AtomicLong totalPointsMissed, AtomicLong totalPointsProcessed) throws ApplicationException {
        if (myPolygon.contains(onePoint)) {
            totalPointsProcessed.incrementAndGet();
        } else {
            totalPointsMissed.incrementAndGet();
        }
        if ((totalPointsProcessed.get() + totalPointsMissed.get()) % 10000 == 0) {
            logInfo("Total Point Processed [" + totalPointsProcessed.get() + "] , total point missed [" + totalPointsMissed.get() + "] " + onePoint);
        }
    }

}
