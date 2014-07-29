package com.eswaraj.tasks.bolt;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import backtype.storm.tuple.Tuple;

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
            String redisKey;
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
            List<Point2D> allPoints = locationKeyService.getAllPointsBetweenRectangle(topLeftLat, topLeftLong, bottomRightLat, bottomRightLong);
            for (Point2D onePoint : allPoints) {
                if (myPolygon.contains(onePoint)) {
                    redisKey = locationKeyService.buildLocationKey(onePoint.getX(), onePoint.getY());
                    System.out.println("Point[" + onePoint.getX() + "," + onePoint.getY() + "] is inside area Will save key " + redisKey + " in database");
                    Set<Long> existingData = template.opsForSet().members(redisKey);
                    if (existingData != null) {
                        System.out.println("Existings values");
                        for (Long oneLocation : existingData) {
                            System.out.println("   Location Id : " + oneLocation);
                        }
                    }
                    template.opsForSet().add(redisKey, locationId);
                } else {
                    System.out.println("Point[" + onePoint.getX() + "," + onePoint.getY() + "] is outside area and will not save");
                }
            }

        } catch (Exception ex) {
            logError("Unable to save lcoation file in redis ", ex);
        }

    }

}
