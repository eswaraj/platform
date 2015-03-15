package com.eswaraj.cache.redis.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.eswaraj.cache.CounterCache;
import com.eswaraj.core.exceptions.ApplicationException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Component
public class CounterCacheRedisImpl extends BaseCacheRedisImpl implements CounterCache {

    @Autowired
    @Qualifier("complaintStringRedisTemplate")
    private StringRedisTemplate complaintStringRedisTemplate;

    @Override
    public JsonObject getLastNDayLocationCounters(Long locationId, Date endDate, int NumberOfDays) throws ApplicationException {
        String keyPrefix = appKeyService.getLocationCounterKey(locationId);
        List<String> redisKeyForLocationNDaysCounter = appKeyService.getHourComplaintKeysForLastNDays("", endDate, NumberOfDays);
        logger.info("getting data from Redis for keys {}", redisKeyForLocationNDaysCounter);
        List<String> data = complaintStringRedisTemplate.opsForValue().multiGet(redisKeyForLocationNDaysCounter);
        Long totalComplaints = 0L;
        int count = 0;
        JsonArray jsonArray = new JsonArray();
        JsonObject jsonObject;
        for (String oneString : data) {
            jsonObject = new JsonObject();
            if (oneString != null) {
                jsonObject.addProperty(redisKeyForLocationNDaysCounter.get(count).replace(keyPrefix, ""), oneString);
                totalComplaints = totalComplaints + Long.parseLong(oneString);
            } else {
                jsonObject.addProperty(redisKeyForLocationNDaysCounter.get(count).replace(keyPrefix, ""), "0");
            }
            jsonArray.add(jsonObject);
            count++;
        }
        JsonObject returnObject = new JsonObject();
        returnObject.addProperty("totalComplaints", totalComplaints);
        returnObject.add("dayWise", jsonArray);
        return returnObject;
    }

    @Override
    public JsonObject getLast30DayLocationCounters(Long locationId, Date endDate) throws ApplicationException {
        return getLastNDayLocationCounters(locationId, endDate, 30);
    }

    @Override
    public Long getLocationComplaintCounter(Long locationId) throws ApplicationException {
        String rediskey = appKeyService.getLocationCounterKey(locationId);
        String hash = appKeyService.getTotalComplaintCounterKey("");
        logger.info("Getting from hash for Key : {}, hash : {}", rediskey, hash);
        Long returnCounter = 0L;
        try {
            Object redisValue = complaintStringRedisTemplate.opsForHash().get(rediskey, hash);
            returnCounter = Long.parseLong(String.valueOf(redisValue));
        } catch (Exception ex) {
        }
        return returnCounter;
    }

    @Override
    public Long getLocationCategoryComplaintCounter(Long locationId, Long categoryId) throws ApplicationException {
        String rediskey = appKeyService.getLocationCounterKey(locationId);
        String categoryKey = appKeyService.getCategoryCounterKey(categoryId);
        String hash = appKeyService.getTotalComplaintCounterKey(categoryKey);
        logger.info("Getting from hash for Key : {}, hash : {}", rediskey, hash);
        Long returnCounter = 0L;
        try {
            Object redisValue = complaintStringRedisTemplate.opsForHash().get(rediskey, hash);
            returnCounter = Long.parseLong(String.valueOf(redisValue));
        } catch (Exception ex) {
        }
        return returnCounter;
    }

    @Override
    public List<Long> getLocationCategoryComplaintCounter(Long locationId, List<Long> categoryIds) throws ApplicationException {
        String rediskey = appKeyService.getLocationCounterKey(locationId);
        List<Object> hashes = new ArrayList<Object>();
        for (Long oneCategoryId : categoryIds) {
            String categoryKey = appKeyService.getCategoryCounterKey(oneCategoryId);
            String hash = appKeyService.getTotalComplaintCounterKey(categoryKey);
            hashes.add(hash);
        }
        logger.info("Getting from hash for Key : {}, hashes : {}", rediskey, hashes);
        List<Long> returnList = new ArrayList<Long>(hashes.size());
        List<Object> redisValues = complaintStringRedisTemplate.opsForHash().multiGet(rediskey, hashes);
        for(Object oneValue:redisValues){
            if (oneValue == null) {
                returnList.add(0L);
            } else {
                try {
                    returnList.add(Long.parseLong(String.valueOf(oneValue)));
                } catch (Exception ex) {
                    returnList.add(0L);
                }

            }

        }
        return returnList;
    }

}
