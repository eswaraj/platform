package com.eswaraj.cache.redis.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.eswaraj.cache.PoliticalAdminCache;
import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.AppKeyService;
import com.eswaraj.core.service.StormCacheAppServices;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Component
public class PoliticalAdminCacheRedisImpl implements PoliticalAdminCache {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private StormCacheAppServices stormCacheAppServices;
    @Autowired
    private AppKeyService appKeyService;
    private JsonParser jsonParser = new JsonParser();

    @Autowired
    @Qualifier("personStringRedisTemplate")
    private StringRedisTemplate personStringRedisTemplate;

    @Override
    public void refreshPoliticalBodyAdmin(Long politicalBodyAdminId) throws ApplicationException {
        JsonObject politicalBodyJsonObject = stormCacheAppServices.getPoliticalBodyAdmin(politicalBodyAdminId);
        String redisKey = appKeyService.getPoliticalBodyAdminObjectKey(String.valueOf(politicalBodyAdminId));
        String redisValue = politicalBodyJsonObject.toString();
        personStringRedisTemplate.opsForValue().set(redisKey, redisValue);

        // Save Location Id with Political Admin Body
        String url = politicalBodyJsonObject.get("urlIdentifier").getAsString();
        String politicalAdminUrlRedisKey = appKeyService.getPoliticalBodyAdminUrlKey(url);
        personStringRedisTemplate.opsForValue().set(politicalAdminUrlRedisKey, politicalBodyAdminId.toString());

    }

    @Override
    public String getPoliticalBodyAdminById(String politicalBodyAdminId) throws ApplicationException {
        String redisKey = appKeyService.getPoliticalBodyAdminObjectKey(String.valueOf(politicalBodyAdminId));
        String redisValue = personStringRedisTemplate.opsForValue().get(redisKey);
        return redisValue;
    }

    @Override
    public String getPoliticalBodyAdminByUrlIdentifier(String politicalBodyAdminUrlIdentifier) throws ApplicationException {
        // get Url to Id Mapping
        String politicalAdminUrlRedisKey = appKeyService.getPoliticalBodyAdminUrlKey(politicalBodyAdminUrlIdentifier);
        String politicalBodyAdminId = personStringRedisTemplate.opsForValue().get(politicalAdminUrlRedisKey);
        // get ID to actual Object
        String redisKey = appKeyService.getPoliticalBodyAdminObjectKey(politicalBodyAdminId);
        String redisValue = personStringRedisTemplate.opsForValue().get(redisKey);
        return redisValue;
    }

    @Override
    public JsonArray getPoliticalBodyAdminByIds(Collection<String> politicalBodyAdminId) throws ApplicationException {
        JsonArray politicalBodyAdminsArray = new JsonArray();
        if (politicalBodyAdminId == null || politicalBodyAdminId.isEmpty()) {
            return politicalBodyAdminsArray;
        }
        List<String> politicalBodyAdminKey = new ArrayList<>(politicalBodyAdminId.size());
        for (String onePoliticalAdminId : politicalBodyAdminId) {
            politicalBodyAdminKey.add(appKeyService.getPoliticalBodyAdminObjectKey(onePoliticalAdminId));
        }
        List<String> pbaList = personStringRedisTemplate.opsForValue().multiGet(politicalBodyAdminKey);
        int count = 0;
        for (String onePoliticalAdmin : pbaList) {
            if(onePoliticalAdmin == null){
                logger.warn("One Political Admin Not found for ID : {}", politicalBodyAdminKey.get(count));
                continue;
            }
            politicalBodyAdminsArray.add(jsonParser.parse(onePoliticalAdmin));
            count++;
        }
        return politicalBodyAdminsArray;
    }

}
