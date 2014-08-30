package com.eswaraj.tasks.bolt.processors;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import backtype.storm.tuple.Tuple;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.LocationKeyService;
import com.eswaraj.core.service.LocationService;
import com.eswaraj.tasks.topology.EswarajBaseBolt.Result;
import com.eswaraj.web.dto.LocationDto;

@Component
public class LocationOneFileProcessorBoltProcessor extends AbstractBoltProcessor {

    @Autowired
    private LocationKeyService locationKeyService;
    @Autowired
    private LocationService locationService;

    @Override
    public Result processTuple(Tuple input) {
        Date startTime = new Date();
        try {
            // Read the incoming Message
            String[] coordinates = (String[]) input.getValue(0);
            String pointsToProcess = input.getString(1);
            Long locationId = input.getLong(2);
            logInfo("Recived coordinates=" + coordinates.length);
            logInfo("Recived pointsToProcess=" + pointsToProcess.length());
            logInfo("Recived locationId=" + locationId);

            Set<Long> allLocations = getAllParents(locationId);

            AtomicLong totalPointsMissed = new AtomicLong(0);
            AtomicLong totalPointsProcessed = new AtomicLong(0);
            processCoordinates(coordinates, allLocations, pointsToProcess, totalPointsMissed, totalPointsProcessed);
            logInfo("totalPointsMissed(Redis)= " + incrementCounterInMemoryStore("totalPointsMissed", totalPointsMissed.get()));
            logInfo("totalPointsProcessed(Redis)= " + incrementCounterInMemoryStore("totalPointsProcessed", totalPointsProcessed.get()));
            return Result.Success;
        } catch (Throwable ex) {
            logError("Unable to save lcoation file in redis ", ex);
        } finally {
            Date endTime = new Date();
            logInfo("Total time taken to process file " + ((endTime.getTime() - startTime.getTime()) / 1000) + " seconds");
        }
        return Result.Failed;

    }

    private Set<Long> getAllParents(Long locationId) {
        List<LocationDto> locations;
        Set<Long> returnCollection = new LinkedHashSet<Long>();
        returnCollection.add(locationId);
        try {
            locations = locationService.getAllParents(locationId);
            for (LocationDto oneLocationDto : locations) {
                returnCollection.add(oneLocationDto.getId());
            }
        } catch (ApplicationException e) {
            e.printStackTrace();
        }

        return returnCollection;
    }


    private void processCoordinates(String[] boundaryCoordinates, Set<Long> allLocations, String pointsToProcess, AtomicLong totalPointsMissed, AtomicLong totalPointsProcessed)
            throws ApplicationException {

        List<Path2D> myPolygons = createPolygon(boundaryCoordinates);

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
            processOnePoint(myPolygons, onePoint, allLocations, totalPointsMissed, totalPointsProcessed);
        }

    }

    private List<Path2D> createPolygon(String[] boundaryCoordinates) {
        List<Path2D> allPaths = new ArrayList<>();
        for (String oneCoordinateString : boundaryCoordinates) {
            String[] locationPoints = oneCoordinateString.split(" ");
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
            allPaths.add(myPolygon);
        }

        return allPaths;
    }

    private void processOnePoint(List<Path2D> myPolygons, Point2D onePoint, Set<Long> allLocations, AtomicLong totalPointsMissed, AtomicLong totalPointsProcessed) throws ApplicationException {
        boolean insideBoundaries = false;
        for (Path2D onePolygon : myPolygons) {
            if (onePolygon.contains(onePoint)) {
                insideBoundaries = true;
                break;
            }
        }
        String redisKey = locationKeyService.buildLocationKey(onePoint.getX(), onePoint.getY());
        if (insideBoundaries) {

            writeToMemoryStoreSet(redisKey, allLocations);
            totalPointsProcessed.incrementAndGet();
        } else {
            removeFromMemoryStoreSet(redisKey, allLocations);
            totalPointsMissed.incrementAndGet();
        }
        

        if ((totalPointsProcessed.get() + totalPointsMissed.get()) % 10000 == 0) {
            logInfo("Total Point Processed [" + totalPointsProcessed.get() + "] , total point missed [" + totalPointsMissed.get() + "] " + onePoint);
        }
    }

}
