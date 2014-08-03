package com.eswaraj.tasks.bolt;

import java.awt.Rectangle;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import backtype.storm.tuple.Tuple;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.LocationKeyService;
import com.eswaraj.core.service.impl.LocationkeyServiceImpl;
import com.eswaraj.tasks.topology.EswarajBaseBolt;

public class LocationOneFileProcessorBolt extends EswarajBaseBolt {

    private static final long serialVersionUID = 1L;
    LocationKeyService locationKeyService = new LocationkeyServiceImpl();

    @Override
    public void execute(Tuple input) {
        Date startTime = new Date();
        try {
            // Read the incoming Message
            String coordinates = input.getString(0);
            String pointsToProcess = input.getString(1);
            Long locationId = input.getLong(2);
            logInfo("Recived = " + getComponentId() + ", coordinates=" + coordinates.length());
            logInfo("Recived = " + getComponentId() + ", pointsToProcess=" + pointsToProcess.length());
            logInfo("Recived = " + getComponentId() + ", locationId=" + locationId);

            AtomicLong totalPointsMissed = new AtomicLong(0);
            AtomicLong totalPointsProcessed = new AtomicLong(0);
            processCoordinates(coordinates, locationId, pointsToProcess, true, totalPointsMissed, totalPointsProcessed);
            incrementCounterInMemoryStore("totalPointsMissed", totalPointsMissed.get());
            incrementCounterInMemoryStore("totalPointsProcessed", totalPointsProcessed.get());
        } catch (Throwable ex) {
            logError("Unable to save lcoation file in redis ", ex);
        } finally {
            Date endTime = new Date();
            logInfo("Total time taken to process file " + ((endTime.getTime() - startTime.getTime()) / 1000) + " seconds");
        }

    }


    private void processCoordinates(String coordinates, Long locationId, String pointsToProcess, boolean add, AtomicLong totalPointsMissed, AtomicLong totalPointsProcessed)
            throws ApplicationException {

        Path2D myPolygon = createPolygon(coordinates);
        Rectangle coveringRectangle = myPolygon.getBounds();
        MathContext topLeftMc = new MathContext(3, RoundingMode.DOWN);
        BigDecimal topLeftLat = new BigDecimal(coveringRectangle.getMinX()).round(topLeftMc);
        BigDecimal topLeftLong = new BigDecimal(coveringRectangle.getMinY()).round(topLeftMc);
        MathContext bottomRightMc = new MathContext(3, RoundingMode.UP);
        BigDecimal bottomRightLat = new BigDecimal(coveringRectangle.getMaxX()).round(bottomRightMc);
        BigDecimal bottomRightLong = new BigDecimal(coveringRectangle.getMaxY()).round(bottomRightMc);
        Point2D onePoint;

        String[] locationPoints = pointsToProcess.split(" ");
        String[] latLong;
        BigDecimal oneLat, oneLong;
        for (String oneLocationPoint : locationPoints) {
            latLong = oneLocationPoint.split(",");
            oneLong = new BigDecimal(latLong[0]);
            oneLat = new BigDecimal(latLong[1]);
            onePoint = new Point2D.Double(oneLat.doubleValue(), oneLong.doubleValue());
            processOnePoint(myPolygon, onePoint, locationId, add, totalPointsMissed, totalPointsProcessed);
        }
        logInfo("topLeftLat = " + topLeftLat);
        logInfo("topLeftLong = " + topLeftLong);
        logInfo("bottomRightLat = " + bottomRightLat);
        logInfo("bottomRightLong = " + bottomRightLong);
    }

    private Path2D readBoundary(String s3HttpUrl) throws ApplicationException {
        try {
            logInfo("Getting Location file from " + s3HttpUrl);
            URL url = new URL(s3HttpUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream is = urlConnection.getInputStream();

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);
            doc.getDocumentElement().normalize();
            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

            NodeList nList = doc.getElementsByTagName("SimpleData");
            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);

                System.out.println("\nCurrent Element :" + nNode.getNodeName());

                System.out.println("nNode.getNodeType : " + nNode.getNodeType());
                System.out.println("nNode.getAttributes : " + nNode.getAttributes());
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;
                    System.out.println("Name : " + eElement.getAttribute("name"));
                    System.out.println("NodeValue : " + eElement.getNodeValue());
                    System.out.println("getTextContent : " + eElement.getTextContent());
                    System.out.println("getAttributes : " + eElement.getAttributes());

                }
            }

            System.out.println("\n*********Coordinates********");
            NodeList coordinateList = doc.getElementsByTagName("coordinates");
            String coordinates = null;
            for (int temp = 0; temp < coordinateList.getLength(); temp++) {

                Node nNode = coordinateList.item(temp);

                System.out.println("\nCurrent Element :" + nNode.getNodeName());
                System.out.println("nNode.getAttributes : " + nNode.getAttributes());

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;

                    System.out.println("NodeValue : " + eElement.getNodeValue());
                    coordinates = eElement.getTextContent();
                    System.out.println("getTextContent : " + coordinates);
                    System.out.println("getAttributes : " + eElement.getAttributes());

                }
            }

            Path2D myPolygon = createPolygon(coordinates);

            return myPolygon;
        } catch (IOException ioe) {
            throw new ApplicationException(ioe);
        } catch (ParserConfigurationException pce) {
            throw new ApplicationException(pce);
        } catch (SAXException se) {
            throw new ApplicationException(se);
        }
        
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

            String redisKey = locationKeyService.buildLocationKey(onePoint.getX(), onePoint.getY());
            if (add) {
                writeToMemoryStoreSet(redisKey, locationId);
            } else {
                removeFromMemoryStoreSet(redisKey, locationId);
            }
            totalPointsProcessed.incrementAndGet();

        } else {
            totalPointsMissed.incrementAndGet();
        }
        if ((totalPointsProcessed.get() + totalPointsMissed.get()) % 10000 == 0) {
            logInfo("Total Point Processed [" + totalPointsProcessed.get() + "] , total point missed [" + totalPointsMissed.get() + "] " + onePoint);
        }
    }

}
