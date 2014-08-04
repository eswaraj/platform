package com.eswaraj.tasks.bolt;

import java.awt.Rectangle;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

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
            logInfo("totalPointsMissed(Redis)= " + incrementCounterInMemoryStore("totalPointsMissed", totalPointsMissed.get()));
            logInfo("totalPointsProcessed(Redis)= " + incrementCounterInMemoryStore("totalPointsProcessed", totalPointsProcessed.get()));
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
        Point2D onePoint;

        String[] locationPoints = pointsToProcess.split(" ");
        logInfo("Splitted Points are : " + locationPoints.length);
        String[] latLong;
        BigDecimal oneLat, oneLong;
        for (String oneLocationPoint : locationPoints) {
            latLong = oneLocationPoint.split(",");
            oneLong = new BigDecimal(latLong[0]);
            oneLat = new BigDecimal(latLong[1]);
            onePoint = new Point2D.Double(oneLat.doubleValue(), oneLong.doubleValue());
            processOnePoint(myPolygon, onePoint, locationId, add, totalPointsMissed, totalPointsProcessed);
        }

        MathContext topLeftMc = new MathContext(3, RoundingMode.DOWN);
        BigDecimal topLeftLat = new BigDecimal(coveringRectangle.getMinX()).round(topLeftMc);
        BigDecimal topLeftLong = new BigDecimal(coveringRectangle.getMinY()).round(topLeftMc);
        MathContext bottomRightMc = new MathContext(3, RoundingMode.UP);
        BigDecimal bottomRightLat = new BigDecimal(coveringRectangle.getMaxX()).round(bottomRightMc);
        BigDecimal bottomRightLong = new BigDecimal(coveringRectangle.getMaxY()).round(bottomRightMc);

        logInfo("topLeftLat = " + topLeftLat);
        logInfo("topLeftLong = " + topLeftLong);
        logInfo("bottomRightLat = " + bottomRightLat);
        logInfo("bottomRightLong = " + bottomRightLong);
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
                // writeToMemoryStoreSet(redisKey, locationId);
            } else {
                // removeFromMemoryStoreSet(redisKey, locationId);
            }
            totalPointsProcessed.incrementAndGet();
            logInfo("RedisKey [" + redisKey + "] Total Point Processed [" + totalPointsProcessed.get() + "] , total point missed [" + totalPointsMissed.get() + "] " + onePoint);
        } else {
            totalPointsMissed.incrementAndGet();
            //logInfo("Missed Total Point Processed [" + totalPointsProcessed.get() + "] , total point missed [" + totalPointsMissed.get() + "] " + onePoint);
        }
        
        if ((totalPointsProcessed.get() + totalPointsMissed.get()) % 10000 == 0) {
            logInfo("Total Point Processed [" + totalPointsProcessed.get() + "] , total point missed [" + totalPointsMissed.get() + "] " + onePoint);
        }
    }

}
