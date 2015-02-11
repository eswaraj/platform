package com.eswaraj.cache.redis.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.eswaraj.cache.ComplaintCache;
import com.eswaraj.cache.PoliticalAdminCache;
import com.eswaraj.core.exceptions.ApplicationException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Component
public class ComplaintCacheRedisImpl extends BaseCacheRedisImpl implements ComplaintCache {

    @Autowired
    @Qualifier("complaintStringRedisTemplate")
    private StringRedisTemplate complaintStringRedisTemplate;

    @Autowired
    private PoliticalAdminCache politicalAdminCache;

    @Override
    public void refreshComplaintInfo(long complaintId) throws ApplicationException {
        JsonObject jsonObject = stormCacheAppServices.getCompleteComplaintInfo(complaintId);
        String redisKeyForComplaint = appKeyService.getComplaintObjectKey(complaintId);
        complaintStringRedisTemplate.opsForValue().set(redisKeyForComplaint, jsonObject.toString());
    }

    @Override
    public String getComplaintById(long complaintId) throws ApplicationException {
        String redisKeyForComplaint = appKeyService.getComplaintObjectKey(complaintId);
        return complaintStringRedisTemplate.opsForValue().get(redisKeyForComplaint);
    }

    @Override
    public String getComplaintById(String complaintId) throws ApplicationException {
        String redisKeyForComplaint = appKeyService.getComplaintObjectKey(complaintId);
        return complaintStringRedisTemplate.opsForValue().get(redisKeyForComplaint);
    }

    @Override
    public String getComplaintsOfLocation(Long locationId, int start, int count) throws ApplicationException {
        String locationComplaintKey = appKeyService.getLocationComplaintsKey(locationId);
        logger.info("locationComplaintKey : {}, start:{}, count{}", locationComplaintKey);

        return getComplaintsOfKey(locationComplaintKey, start, count);
    }

    @Override
    public String getComplaintsOfLocationCategory(Long locationId, Long categoryId, int start, int count) throws ApplicationException {
        String locationComplaintCategoryKey = appKeyService.getLocationCategoryComplaintsKey(locationId, categoryId);
        logger.info("locationComplaintCategoryKey : {}", locationComplaintCategoryKey);
        return getComplaintsOfKey(locationComplaintCategoryKey, start, count);
    }

    private String getComplaintsOfKey(String key, int start, int count) throws ApplicationException {
        Set<String> complaintIds = complaintStringRedisTemplate.opsForZSet().reverseRange(key, start, start + count);
        logger.info("complaintIds : {}", complaintIds);
        List<String> complaintKeys = new ArrayList<>();
        for (String oneComplaintId : complaintIds) {
            complaintKeys.add(appKeyService.getComplaintObjectKey(oneComplaintId));
        }
        logger.info("complaintKeys : {}", complaintKeys);
        List<String> complaintList = complaintStringRedisTemplate.opsForValue().multiGet(complaintKeys);

        // Add Executive Admin Info
        JsonObject oneComplaintJsonObject;
        JsonParser jsonParser = new JsonParser();
        JsonArray complaintJsonArray = new JsonArray();
        for (String oneComplaint : complaintList) {
            if (oneComplaint == null) {
                continue;
            }
            oneComplaintJsonObject = (JsonObject) jsonParser.parse(oneComplaint);
            addExecutiveBodyAdminInformation(oneComplaintJsonObject);
            addPoliticalBodyAdminInformation(oneComplaintJsonObject);

            complaintJsonArray.add(oneComplaintJsonObject);
        }
        return complaintJsonArray.toString();// convertList(complaintList);
    }

    private String convertList(List<String> complaints) {
        JsonObject jsonObject;
        JsonArray jsonArray = new JsonArray();
        for (String oneComplaint : complaints) {
            jsonObject = (JsonObject) jsonParser.parse(oneComplaint);
            jsonArray.add(jsonObject);
        }
        return jsonArray.toString();
    }
    private void addPoliticalBodyAdminInformation(JsonObject oneComplaintJsonObject) throws ApplicationException {
        if (oneComplaintJsonObject == null) {
            return;
        }
        if (oneComplaintJsonObject.get("pba") != null && !oneComplaintJsonObject.get("pba").isJsonNull()) {
            JsonArray jsonArray = oneComplaintJsonObject.get("pba").getAsJsonArray();
            // remove eba
            oneComplaintJsonObject.remove("pba");
            JsonObject ebaOneJsonObject;
            Set<String> politicalBodyAdminIds = new LinkedHashSet<>();
            for (int i = 0; i < jsonArray.size(); i++) {
                ebaOneJsonObject = (JsonObject) jsonArray.get(i);
                politicalBodyAdminIds.add(ebaOneJsonObject.get("id").getAsString());
            }
            JsonArray pbaJsonInforArray = politicalAdminCache.getPoliticalBodyAdminByIds(politicalBodyAdminIds);
            oneComplaintJsonObject.add("politicalAdmins", pbaJsonInforArray);

        }
    }
    
    private void addExecutiveBodyAdminInformation(JsonObject oneComplaintJsonObject) {
        if (oneComplaintJsonObject == null) {
            return;
        }
        /*
        if (oneComplaintJsonObject.get("eba") != null && !oneComplaintJsonObject.get("eba").isJsonNull()) {
            logger.info("eba is null = " + oneComplaintJsonObject.get("eba").isJsonNull());
            JsonArray jsonArray = oneComplaintJsonObject.get("eba").getAsJsonArray();
            // remove eba
            oneComplaintJsonObject.remove("eba");
            JsonObject ebaOneJsonObject;
            String redisKey;
            String hashKey;
            JsonArray ebaJsonInforArray = new JsonArray();
            for (int i = 0; i < jsonArray.size(); i++) {
                ebaOneJsonObject = (JsonObject) jsonArray.get(i);
                redisKey = appKeyService.getExecutiveBodyAdminObjectKey(ebaOneJsonObject.get("id").getAsString());
                hashKey = appKeyService.getEnityInformationHashKey();
                String ebaInfo = (String) stringRedisTemplate.opsForHash().get(redisKey, hashKey);
                if (ebaInfo != null) {
                    JsonObject oneEbaJsonObject = (JsonObject) jsonParser.parse(ebaInfo);
                    ebaJsonInforArray.add(oneEbaJsonObject);
                }
            }
            oneComplaintJsonObject.add("executiveAdmins", ebaJsonInforArray);

        }
        */
    }

    @Override
    public String getComplaintsByIds(List<Long> complaintIds) throws ApplicationException {
        List<String> rediskeys = new ArrayList<String>();
        for (Long complaintId : complaintIds) {
            String redisKeyForComplaint = appKeyService.getComplaintObjectKey(complaintId);
            rediskeys.add(redisKeyForComplaint);
        }

        JsonArray jsonArray = new JsonArray();
        if (!rediskeys.isEmpty()) {
            List<String> complaintData = complaintStringRedisTemplate.opsForValue().multiGet(rediskeys);

            for (String oneComplaintAsString : complaintData) {
                JsonObject jsonObject = (JsonObject) jsonParser.parse(oneComplaintAsString);
                jsonArray.add(jsonObject);
            }
        }
        return jsonArray.toString();
    }

    @Override
    public Long getUserComplaintsForTheDay(String userId) throws ApplicationException {
        String redisKey = appKeyService.getPersonDailyComplaintCountKey(userId, new Date());
        String count = complaintStringRedisTemplate.opsForValue().get(redisKey);
        if (count == null) {
            return 0L;
        }
        return Long.parseLong(count);
    }

    @Override
    public Long incrementPersonComplaintsForTheDay(String userId) throws ApplicationException {
        String redisKey = appKeyService.getPersonDailyComplaintCountKey(userId, new Date());
        Long count = complaintStringRedisTemplate.opsForValue().increment(redisKey, 1L);
        // Expire it just after midnight
        int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        complaintStringRedisTemplate.expire(redisKey, 24 - currentHour, TimeUnit.HOURS);
        return count;
    }

}
