package com.eswaraj.tasks.bolt.processors;

import java.awt.Rectangle;
import java.awt.geom.Path2D;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.LocationKeyService;
import com.eswaraj.core.service.LocationService;
import com.eswaraj.tasks.topology.EswarajBaseBolt.Result;
import com.eswaraj.web.dto.LocationBoundaryFileDto;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Component
public class LocationFileDistributeBoltProcessor extends AbstractBoltProcessor {

    @Autowired
    private LocationKeyService locationKeyService;
    @Autowired
    private LocationService locationService;

    @Override
    public Result processTuple(Tuple inputTuple) {
        Date startTime = new Date();
        try {
            // Read the incoming Message
            String message = inputTuple.getString(0);

            JsonParser parser = new JsonParser();
            JsonObject jsonObject = (JsonObject) parser.parse(message);
            Long locationId = jsonObject.get("locationId").getAsLong();

            Long newLocationBoundaryFileId = jsonObject.get("newLocationBoundaryFileId").getAsLong();
            Long oldLocationBoundaryFileId = null;

            if (jsonObject.get("oldLocationBoundaryFileId") != null) {
                oldLocationBoundaryFileId = jsonObject.get("oldLocationBoundaryFileId").getAsLong();
            }

            String[] newCoordinates = getCoordinatesForBoundaryFileId(newLocationBoundaryFileId);

            AtomicLong totalPointsMissed = new AtomicLong(0);
            AtomicLong totalPointsProcessed = new AtomicLong(0);
            if (oldLocationBoundaryFileId != null) {
                String[] oldCoordinates = getCoordinatesForBoundaryFileId(oldLocationBoundaryFileId);
                // Send all points of old boundary with new Boundary.
                for (String coordinates : oldCoordinates) {
                    processCoordinates(coordinates, newCoordinates, locationId, totalPointsMissed, totalPointsProcessed, inputTuple);
                }
            }

            for (String coordinates : newCoordinates) {
                processCoordinates(coordinates, new String[] { coordinates }, locationId, totalPointsMissed, totalPointsProcessed, inputTuple);
            }

            return Result.Success;
        } catch (Exception ex) {
            logError("Unable to save lcoation file in redis ", ex);
        } finally {
            Date endTime = new Date();
            logInfo("Total time taken to process file " + ((endTime.getTime() - startTime.getTime()) / 1000) + " seconds");
        }
        return Result.Failed;

    }

    private String[] getCoordinatesFromHttpFile(String s3HttpUrl) throws ApplicationException {
        try {
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
            List<String> coordinateLists = new ArrayList<>();
            for (int temp = 0; temp < coordinateList.getLength(); temp++) {
                Node nNode = coordinateList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    coordinates = eElement.getTextContent();
                    coordinateLists.add(coordinates);
                }
            }
            return coordinateLists.toArray(new String[coordinateLists.size()]);
        } catch (Exception ex) {
            throw new ApplicationException(ex);
        }

    }

    private String[] getCoordinatesForBoundaryFileId(Long boundaryFileId) throws ApplicationException {
        LocationBoundaryFileDto locationBoundaryFileDto = locationService.getLocationBoundaryFileById(boundaryFileId);
        String s3HttpUrl = locationBoundaryFileDto.getFileNameAndPath();
        return getCoordinatesFromHttpFile(s3HttpUrl);

    }


    private void processCoordinates(String coordinates, String[] boundaryCorrdinates, Long locationId, AtomicLong totalPointsMissed, AtomicLong totalPointsProcessed,
            Tuple inputTuple) throws ApplicationException {

        Rectangle coveringRectangle = createPolygonRectangle(coordinates);
        MathContext topLeftMc = new MathContext(3, RoundingMode.DOWN);
        BigDecimal topLeftLat = new BigDecimal(coveringRectangle.getMinX()).round(topLeftMc).setScale(3, RoundingMode.DOWN);
        BigDecimal topLeftLong = new BigDecimal(coveringRectangle.getMinY()).round(topLeftMc).setScale(3, RoundingMode.DOWN);
        MathContext bottomRightMc = new MathContext(3, RoundingMode.UP);
        BigDecimal bottomRightLat = new BigDecimal(coveringRectangle.getMaxX()).round(bottomRightMc).setScale(3, RoundingMode.UP);
        BigDecimal bottomRightLong = new BigDecimal(coveringRectangle.getMaxY()).round(bottomRightMc).setScale(3, RoundingMode.DOWN);
        BigDecimal addedValue = new BigDecimal(.001);
        StringBuilder sb = new StringBuilder();

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
                // logInfo("Point " + longitude.toString() + "," +
                // latitude.toString());
                sb.append(longitude.toString());
                sb.append(",");
                sb.append(latitude.toString());
            }
            count++;
            if (count % 10 == 0) {
                totalDivide++;
                logInfo("Writing to stream");
                writeToStream(inputTuple, new Values(boundaryCorrdinates, sb.toString(), locationId));
                logInfo("Writing Done");
                sb = new StringBuilder();
                first = true;
            }
        }
        if (count % 10 > 0) {
            writeToStream(inputTuple, new Values(boundaryCorrdinates, sb.toString(), locationId));
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

}
