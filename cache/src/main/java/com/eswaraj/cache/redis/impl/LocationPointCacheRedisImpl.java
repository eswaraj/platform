package com.eswaraj.cache.redis.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.eswaraj.cache.LocationPointCache;
import com.eswaraj.core.exceptions.ApplicationException;

@Component
public class LocationPointCacheRedisImpl extends BaseCacheRedisImpl implements LocationPointCache {

    @Autowired
    @Qualifier("locationStringRedisTemplate")
    private StringRedisTemplate locationStringRedisTemplate;

    @Override
    public void attachPointToLocations(double x, double y, Set<Long> locations) throws ApplicationException {
        String redisKey = appKeyService.buildLocationKey(x, y);
        String[] valueString = convertToStringArray(locations);
        locationStringRedisTemplate.opsForSet().add(redisKey, valueString);
    }

    @Override
    public void dettachPointFromLocations(double x, double y, Set<Long> locations) throws ApplicationException {
        String redisKey = appKeyService.buildLocationKey(x, y);
        String[] valueString = convertToStringArray(locations);
        locationStringRedisTemplate.opsForSet().remove(redisKey, valueString);
    }


    @Override
    public Set<Long> getPointLocations(double x, double y) throws ApplicationException {
        String redisKey = appKeyService.buildLocationKey(x, y);
        logger.info("Redis Key = " + redisKey);
        Set<String> locations = locationStringRedisTemplate.opsForSet().members(redisKey);
        return convertToLongSet(locations);
    }

}
