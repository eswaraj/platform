package com.eswaraj.cache.redis.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.eswaraj.cache.ComplaintCache;
import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.AppKeyService;
import com.eswaraj.core.service.StormCacheAppServices;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ComplaintCacheRedisImpl implements ComplaintCache {

    @Autowired
    @Qualifier("complaintStringRedisTemplate")
    private StringRedisTemplate complaintStringRedisTemplate;

    @Autowired
    private AppKeyService appKeyService;

    @Autowired
    private StormCacheAppServices stormCacheAppServices;

    private JsonParser jsonParser = new JsonParser();

    @Override
    public void refreshComplaintInfo(long complaintId) throws ApplicationException {
        JsonObject jsonObject = stormCacheAppServices.getCompleteComplaintInfo(complaintId);
        String redisKeyForComplaint = appKeyService.getComplaintObjectKey(complaintId);
        String hashKeyForInfo = appKeyService.getEnityInformationHashKey();
        complaintStringRedisTemplate.opsForHash().put(redisKeyForComplaint, hashKeyForInfo, jsonObject.toString());
    }

    @Override
    public String getComplaintById(long complaintId) throws ApplicationException {
        String redisKeyForComplaint = appKeyService.getComplaintObjectKey(complaintId);
        String hashKeyForInfo = appKeyService.getEnityInformationHashKey();
        return (String) complaintStringRedisTemplate.opsForHash().get(redisKeyForComplaint, hashKeyForInfo);
    }

}
