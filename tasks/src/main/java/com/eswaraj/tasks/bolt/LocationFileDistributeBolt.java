package com.eswaraj.tasks.bolt;

import java.awt.Rectangle;
import java.awt.geom.Path2D;
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
import backtype.storm.tuple.Values;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.LocationKeyService;
import com.eswaraj.core.service.impl.LocationkeyServiceImpl;
import com.eswaraj.tasks.topology.EswarajBaseBolt;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class LocationFileDistributeBolt extends EswarajBaseBolt {

    private static final long serialVersionUID = 1L;
    LocationKeyService locationKeyService = new LocationkeyServiceImpl();

    @Override
    public Result processTuple(Tuple inputTuple) {
        Date startTime = new Date();
        try {
            // Read the incoming Message
            String message = inputTuple.getString(0);
            logInfo("Recived = " + getComponentId() + " " + message + " , message id = " + inputTuple.getMessageId());

            JsonParser parser = new JsonParser();
            JsonObject jsonObject = (JsonObject) parser.parse(message);
            Long locationId = jsonObject.get("locationId").getAsLong();
            if (jsonObject.get("oldLocationBoundaryFileId") != null) {
                Long oldLocationBoundaryFileId = jsonObject.get("oldLocationBoundaryFileId").getAsLong();
                //distributeBoundaryFile(locationId, oldLocationBoundaryFileId, false);
                // remove old file points process old file
            }

            Long newLocationBoundaryFileId = jsonObject.get("newLocationBoundaryFileId").getAsLong();

            distributeBoundaryFile(locationId, newLocationBoundaryFileId, true, inputTuple);
            return Result.Success;
        } catch (Exception ex) {
            logError("Unable to save lcoation file in redis ", ex);
        } finally {
            Date endTime = new Date();
            logInfo("Total time taken to process file " + ((endTime.getTime() - startTime.getTime()) / 1000) + " seconds");
        }
        return Result.Failed;

    }

    @Override
    protected String[] getFields() {
        return new String[] { "MapBoundary", "Points", "LocationId" };
    }


    private void distributeBoundaryFile(Long locationId, Long boundaryFileId, boolean add, Tuple inputTuple) throws ApplicationException {
        org.neo4j.graphdb.Node dbLocationFileNode = getNodeByid(boundaryFileId);
        try {
            String s3HttpUrl = (String) dbLocationFileNode.getProperty("fileNameAndPath");
            logInfo("Getting Location file from " + s3HttpUrl);
            URL url = new URL(s3HttpUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream is = urlConnection.getInputStream();

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
                    /*
                    if (temp == 0) {
                        processCoordinates(coordinates, locationId, add, totalPointsMissed, totalPointsProcessed, new BigDecimal(9.549));
                    } else {
                        processCoordinates(coordinates, locationId, add, totalPointsMissed, totalPointsProcessed, null);
                    }
                    */
                    processCoordinates(coordinates, locationId, add, totalPointsMissed, totalPointsProcessed, null, inputTuple);
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

    private void processCoordinates(String coordinates, Long locationId, boolean add, AtomicLong totalPointsMissed, AtomicLong totalPointsProcessed, BigDecimal startValue, Tuple inputTuple)
            throws ApplicationException {

        Rectangle coveringRectangle = createPolygonRectangle(coordinates);
        MathContext topLeftMc = new MathContext(3, RoundingMode.DOWN);
        BigDecimal topLeftLat = new BigDecimal(coveringRectangle.getMinX()).round(topLeftMc).setScale(3, RoundingMode.DOWN);
        BigDecimal topLeftLong = new BigDecimal(coveringRectangle.getMinY()).round(topLeftMc).setScale(3, RoundingMode.DOWN);
        MathContext bottomRightMc = new MathContext(3, RoundingMode.UP);
        BigDecimal bottomRightLat = new BigDecimal(coveringRectangle.getMaxX()).round(bottomRightMc).setScale(3, RoundingMode.UP);
        BigDecimal bottomRightLong = new BigDecimal(coveringRectangle.getMaxY()).round(bottomRightMc).setScale(3, RoundingMode.DOWN);
        BigDecimal addedValue = new BigDecimal(.001);
        StringBuilder sb = new StringBuilder();
        if (startValue != null) {
            topLeftLat = startValue;
        }

        int count = 0;
        boolean first = true;
        int totalDivide = 0;
        for (BigDecimal latitude = topLeftLat; latitude.compareTo(bottomRightLat) <= 0; latitude = latitude.add(addedValue).setScale(3, RoundingMode.DOWN)) {
            for (BigDecimal longitude = topLeftLong; longitude.compareTo(bottomRightLong) <= 0; longitude = longitude.add(addedValue).setScale(3, RoundingMode.DOWN)) {
                if (first) {
                    first = false;
                } else {
                    sb.append(" ");
                }
                //logInfo("Point " + longitude.toString() + "," + latitude.toString());
                sb.append(longitude.toString());
                sb.append(",");
                sb.append(latitude.toString());
            }
            count++;
            if (count % 10 == 0) {
                totalDivide++;
                logInfo("Writing to stream");
                writeToStream(inputTuple, new Values(coordinates, sb.toString(), locationId));
                logInfo("Writing Done");
                sb = new StringBuilder();
                first = true;
            }
        }
        if (count % 10 > 0){
            writeToStream(inputTuple, new Values(coordinates, sb.toString(), locationId));
            totalDivide++;
        }
        logInfo("Total Divides are : " + totalDivide);
    }
        


    private Rectangle createPolygonRectangle(String coordinates) {
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
        Rectangle coveringRectangle = myPolygon.getBounds();
        return coveringRectangle;
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

}