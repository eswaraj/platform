package com.eswaraj.cache.redis.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.eswaraj.cache.CategoryCache;
import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.web.dto.CategoryWithChildCategoryDto;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Component
public class CategoryCacheRedisImpl extends BaseCacheRedisImpl implements CategoryCache {

    @Autowired
    @Qualifier("complaintStringRedisTemplate")
    private StringRedisTemplate complaintStringRedisTemplate;


    private JsonParser jsonParser = new JsonParser();

    @Override
    public void refreshAllCategories() throws ApplicationException {
        List<CategoryWithChildCategoryDto> categories = appService.getAllCategories();
        String redisKey = appKeyService.getAllCategoriesKey();
        String allCategories = gson.toJson(categories);
        logger.info("Writing Key {} to redis with Value as {}", redisKey, allCategories);
        complaintStringRedisTemplate.opsForValue().set(redisKey, allCategories);

    }

    @Override
    public String getAllCategories() throws ApplicationException {
        String allCategoriesKey = appKeyService.getAllCategoriesKey();
        String allCategories = complaintStringRedisTemplate.opsForValue().get(allCategoriesKey);
        return allCategories;
    }

    @Override
    public JsonArray getAllCategoryStatsForLocation(Long locationId) throws ApplicationException {
        String locationRedisKey = appKeyService.getLocationCounterKey(locationId);
        String allCategories = getAllCategories();
        JsonArray categoryJsonArray = jsonParser.parse(allCategories).getAsJsonArray();
        List<Object> hashKeys = new ArrayList<>(categoryJsonArray.size());
        JsonObject oneJsonObject;
        Long categoryId;
        for (int i = 0; i < categoryJsonArray.size(); i++) {
            oneJsonObject = (JsonObject) categoryJsonArray.get(i);
            categoryId = oneJsonObject.get("id").getAsLong();
            hashKeys.add(appKeyService.getTotalComplaintCounterKey(appKeyService.getCategoryCounterKey(categoryId)));
        }
        logger.info("redisKey = " + locationRedisKey + ", hashKeys =" + hashKeys);
        List<Object> resultList = complaintStringRedisTemplate.opsForHash().multiGet(locationRedisKey, hashKeys);

        for (int i = 0; i < categoryJsonArray.size(); i++) {
            oneJsonObject = (JsonObject) categoryJsonArray.get(i);
            if (resultList.get(i) != null) {
                oneJsonObject.addProperty("locationCount", resultList.get(i).toString());
            } else {
                oneJsonObject.addProperty("locationCount", 0);
            }
        }
        return categoryJsonArray;
    }

}
