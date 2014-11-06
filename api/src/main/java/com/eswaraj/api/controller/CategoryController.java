package com.eswaraj.api.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.eswaraj.cache.CategoryCache;
import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.AppKeyService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Controller
public class CategoryController extends BaseController {

    @Autowired
    @Qualifier("stringRedisTemplate")
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private AppKeyService appKeyService;
    @Autowired
    private CategoryCache categoryCache;

    private JsonParser jsonParser = new JsonParser();

    @RequestMapping(value = "/api/v0/categories", method = RequestMethod.GET)
    public @ResponseBody String getAllCategories(ModelAndView mv, HttpServletRequest httpServletRequest) throws ApplicationException {
        String allCategories = categoryCache.getAllCategories();
        JsonArray categoryJsonArray = new JsonArray();
        if (allCategories != null) {
            categoryJsonArray = (JsonArray) jsonParser.parse(allCategories);
            addGlobalCounterTotalForCategory(httpServletRequest, categoryJsonArray);
            addLocationCounterTotalForCategory(httpServletRequest, categoryJsonArray);
        }
        return categoryJsonArray.toString();
    }

    private void addGlobalCounterTotalForCategory(HttpServletRequest httpServletRequest, JsonArray categoryJsonArray) {
        if (categoryJsonArray == null || categoryJsonArray.size() == 0) {
            return;
        }
        JsonObject oneJsonObject;
        Long categoryId;
        
        String counter = httpServletRequest.getParameter("counter");
        if(counter == null || !counter.equals("global")){
            return;
        }

        for (int i = 0; i < categoryJsonArray.size(); i++) {
            oneJsonObject = (JsonObject) categoryJsonArray.get(i);
            categoryId = oneJsonObject.get("id").getAsLong();
            String redisKey = appKeyService.getCategoryKey(categoryId);
            String hashKey = appKeyService.getTotalComplaintCounterKey("");
            logger.info("Getting Redis key = {} , HashKey ={}", redisKey, hashKey);
            String value = (String) stringRedisTemplate.opsForHash().get(redisKey, hashKey);
            if (value != null) {
                oneJsonObject.addProperty("globalCount", value);
            } else {
                oneJsonObject.addProperty("globalCount", 0);
            }
        }

    }

    private void addLocationCounterTotalForCategory(HttpServletRequest httpServletRequest, JsonArray categoryJsonArray) {
        if (categoryJsonArray == null || categoryJsonArray.size() == 0) {
            return;
        }
        Long locationId = getLongParameter(httpServletRequest, "locationId", 0);
        if(locationId == 0){
            return;
        }
        JsonObject oneJsonObject;
        Long categoryId;
        
        String locationRedisKey = appKeyService.getLocationCounterKey(locationId);
        List<Object> hashKeys = new ArrayList<>(categoryJsonArray.size());
        for (int i = 0; i < categoryJsonArray.size(); i++) {
            oneJsonObject = (JsonObject) categoryJsonArray.get(i);
            categoryId = oneJsonObject.get("id").getAsLong();
            hashKeys.add(appKeyService.getTotalComplaintCounterKey(appKeyService.getCategoryCounterKey(categoryId)));
        }
        logger.info("redisKey = " + locationRedisKey + ", hashKeys =" + hashKeys);
        List<Object> resultList = stringRedisTemplate.opsForHash().multiGet(locationRedisKey, hashKeys);

        for (int i = 0; i < categoryJsonArray.size(); i++) {
            oneJsonObject = (JsonObject) categoryJsonArray.get(i);
            if (resultList.get(i) != null) {
                oneJsonObject.addProperty("locationCount", resultList.get(i).toString());
            } else {
                oneJsonObject.addProperty("locationCount", 0);
            }
        }
       
    }

}
