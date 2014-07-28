package com.eswaraj.tasks.bolt;

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
            for (String oneLocationPoint : locationPoints) {
                latLong = oneLocationPoint.split(",");
                redisKey = locationKeyService.buildLocationKey(Double.parseDouble(latLong[0]), Double.parseDouble(latLong[1]));
                System.out.println("Will save key " + redisKey + " in database");
                Set<Long> existingData = template.opsForSet().members(redisKey);
                if (existingData != null) {
                    System.out.println("Existings values");
                    for (Long oneLocation : existingData) {
                        System.out.println("   Location Id : " + oneLocation);
                    }
                } else {
                    System.out.println("No Existings values");
                }
                template.opsForSet().add(redisKey, locationId);
            }
        } catch (Exception ex) {
            logError("Unable to save lcoation file in redis ", ex);
        }

    }

}
