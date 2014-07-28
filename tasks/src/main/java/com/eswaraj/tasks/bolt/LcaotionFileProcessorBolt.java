package com.eswaraj.tasks.bolt;

import java.util.HashSet;
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
            jedisConnectionFactory.afterPropertiesSet();

            RedisTemplate<String, Set<Long>> template = new RedisTemplate<>();
            template.setConnectionFactory(jedisConnectionFactory);
            template.afterPropertiesSet();

            String message = input.getString(0);
            Long locationId = input.getLong(1);
            logInfo("Recived = " + getComponentId() + " " + message);
            String[] locationPoints = message.split(" ");
            String[] latLong;
            String redisKey;
            Set<Long> redisData;
            for (String oneLocationPoint : locationPoints) {
                latLong = oneLocationPoint.split(",");
                redisKey = locationKeyService.buildLocationKey(Double.parseDouble(latLong[0]), Double.parseDouble(latLong[1]));
                System.out.println("Will save key " + redisKey + " in database");
                redisData = template.opsForValue().get(redisKey);
                System.out.println("Existing Value = " + redisData);
                if (redisData == null) {
                    redisData = new HashSet<>();
                }
                redisData.add(locationId);
                template.opsForValue().set(redisKey, redisData);
            }
        } catch (Exception ex) {
            logError("Unable to save lcoation file in redis ", ex);
        }

    }

}
