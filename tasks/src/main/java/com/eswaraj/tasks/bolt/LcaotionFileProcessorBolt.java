package com.eswaraj.tasks.bolt;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Set;

import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import backtype.storm.tuple.Tuple;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.LocationKeyService;
import com.eswaraj.core.service.impl.LocationkeyServiceImpl;
import com.eswaraj.tasks.topology.EswarajBaseBolt;

public class LcaotionFileProcessorBolt extends EswarajBaseBolt {

    private static final long serialVersionUID = 1L;
    LocationKeyService locationKeyService = new LocationkeyServiceImpl();

    @Override
    public void execute(Tuple input) {
        try {
            JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
            jedisConnectionFactory.setHostName("cache.vyaut5.0001.usw2.cache.amazonaws.com");
            jedisConnectionFactory.setPort(6379);
            jedisConnectionFactory.setUsePool(true);
            jedisConnectionFactory.afterPropertiesSet();

            RedisTemplate<String, Long> template = new RedisTemplate<>();
            template.setConnectionFactory(jedisConnectionFactory);
            template.afterPropertiesSet();

            String message = input.getString(0);
            Long locationId = input.getLong(1);
            logInfo("Recived = " + getComponentId() + " " + message);
            String[] locationPoints = message.split(" ");
            String[] latLong;
            Path2D myPolygon = new Path2D.Double();

            BigDecimal topLeftLat = new BigDecimal(180);
            BigDecimal topLeftLong = new BigDecimal(180);
            BigDecimal bottomRightLat = new BigDecimal(-180);
            BigDecimal bottomRightLong = new BigDecimal(-180);
            
            BigDecimal oneLat, oneLong;
            for (String oneLocationPoint : locationPoints) {
                latLong = oneLocationPoint.split(",");
                oneLat = new BigDecimal(latLong[0]);
                oneLong = new BigDecimal(latLong[1]);
                if (oneLat.compareTo(topLeftLat) < 0) {
                    topLeftLat = oneLat;
                }
                if (oneLong.compareTo(topLeftLong) < 0) {
                    topLeftLong = oneLong;
                }
                if (oneLat.compareTo(bottomRightLat) > 0) {
                    bottomRightLat = oneLat;
                }
                if (oneLong.compareTo(bottomRightLong) > 0) {
                    bottomRightLong = oneLong;
                }
                myPolygon.moveTo(oneLat.doubleValue(), oneLong.doubleValue());
            }
            myPolygon.closePath();
            //List<Point2D> allPoints = locationKeyService.getAllPointsBetweenRectangle(topLeftLat, topLeftLong, bottomRightLat, bottomRightLong);
            MathContext topLeftMc = new MathContext(3, RoundingMode.DOWN);
            topLeftLat.round(topLeftMc);
            topLeftLong.round(topLeftMc);
            MathContext bottomRightMc = new MathContext(3, RoundingMode.UP);
            bottomRightLat.round(bottomRightMc);
            bottomRightLong.round(bottomRightMc);
            BigDecimal addedValue = new BigDecimal(.001);
            Point2D onePoint;
            int i=0;
            for (BigDecimal latitude = topLeftLat; latitude.compareTo(bottomRightLat) <= 0; latitude.add(addedValue)) {
                for (BigDecimal longitude = topLeftLong; longitude.compareTo(bottomRightLong) <= 0; longitude.add(addedValue)) {
                    onePoint = new Point2D.Double(latitude.doubleValue(), longitude.doubleValue());
                    i++;
                    logInfo("Created " + i + " points " + onePoint);
                    processOnePoint(myPolygon, onePoint, template, locationId);
                }
            }

            logInfo("topLeftLat = " + topLeftLat);
            logInfo("topLeftLong = " + topLeftLong);
            logInfo("bottomRightLat = " + bottomRightLat);
            logInfo("bottomRightLong = " + bottomRightLong);

        } catch (Exception ex) {
            logError("Unable to save lcoation file in redis ", ex);
        }

    }

    private void processOnePoint(Path2D myPolygon, Point2D onePoint, RedisTemplate<String, Long> template, Long locationId) throws ApplicationException {
        if (myPolygon.contains(onePoint)) {
            String redisKey = locationKeyService.buildLocationKey(onePoint.getX(), onePoint.getY());
            logInfo("Point[" + onePoint.getX() + "," + onePoint.getY() + "] is inside area Will save key " + redisKey + " in database");
            Set<Long> existingData = template.opsForSet().members(redisKey);
            if (existingData != null) {
                logInfo("Existings values");
                for (Long oneLocation : existingData) {
                    logInfo("   Location Id : " + oneLocation);
                }
            }
            template.opsForSet().add(redisKey, locationId);
        } else {
            logInfo("Point[" + onePoint.getX() + "," + onePoint.getY() + "] is outside area and will not save");
        }
    }

}