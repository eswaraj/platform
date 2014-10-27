package com.eswaraj.api.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.eswaraj.api.redis.MultiLocationOperation;
import com.eswaraj.api.redis.RedisOperation;
import com.eswaraj.core.service.AppKeyService;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

@Component
public class RedisUtil {

    @Autowired
    private AppKeyService appKeyService;
    @Autowired
    @Qualifier("stringRedisTemplate")
    private StringRedisTemplate stringRedisTemplate;

    public JsonArray expandLocations(Collection<Long> locationIds) {
        List<String> keys = new ArrayList<>(locationIds.size());
        for(Long oneLocationId : locationIds){
            keys.add(appKeyService.getEnityInformationHashKey());
        }
        List<String> results = stringRedisTemplate.opsForValue().multiGet(keys);
        JsonArray jsonArray = new JsonArray();
        JsonParser jsonParser = new JsonParser();
        for (String oneResult : results) {
            if (oneResult == null) {
                continue;
            }
            jsonArray.add(jsonParser.parse(oneResult));
        }
        return jsonArray;
    }

    List<RedisOperation> redisOperations = new ArrayList<>();

    public void addLocationsExpandOperation(Collection<Long> locationIds) {
        MultiLocationOperation multiLocationOperation = new MultiLocationOperation(locationIds, appKeyService);
        redisOperations.add(multiLocationOperation);
    }

    public List<Map> executeAll() {
        List<String> allkeys = new ArrayList<>();
        for (RedisOperation oneRedisOperation : redisOperations) {
            allkeys.addAll(oneRedisOperation.getKeys());
        }

        List<String> results = stringRedisTemplate.opsForValue().multiGet(allkeys);

        // Prepare Result
        int index = 0;
        List<Map> finalResults = new ArrayList<>(redisOperations.size());
        for (RedisOperation oneRedisOperation : redisOperations) {
            finalResults.add(oneRedisOperation.prepareResult(results, index));
        }

        return finalResults;
    }


}
