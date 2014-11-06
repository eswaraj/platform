package com.eswaraj.cache.redis.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.eswaraj.cache.ComplaintCache;
import com.eswaraj.core.exceptions.ApplicationException;
import com.google.gson.JsonObject;

@Component
public class ComplaintCacheRedisImpl extends BaseCacheRedisImpl implements ComplaintCache {

    @Autowired
    @Qualifier("complaintStringRedisTemplate")
    private StringRedisTemplate complaintStringRedisTemplate;

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

    @Override
    public String getComplaintById(String complaintId) throws ApplicationException {
        String redisKeyForComplaint = appKeyService.getComplaintObjectKey(complaintId);
        String hashKeyForInfo = appKeyService.getEnityInformationHashKey();
        return (String) complaintStringRedisTemplate.opsForHash().get(redisKeyForComplaint, hashKeyForInfo);
    }

}
