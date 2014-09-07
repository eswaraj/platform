package com.eswaraj.api.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.omg.CORBA.portable.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.eswaraj.core.service.AppKeyService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Controller
public class CategoryController extends BaseController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private AppKeyService appKeyService;

    private JsonParser jsonParser = new JsonParser();

    @RequestMapping(value = "/api/v0/categories", method = RequestMethod.GET)
    public @ResponseBody String getAllCategories(ModelAndView mv, HttpServletRequest httpServletRequest) throws ApplicationException {
        String allCategories = stringRedisTemplate.opsForValue().get(appKeyService.getAllCategoriesKey());
        if (allCategories != null) {
            JsonArray categoryJsonArray = (JsonArray) jsonParser.parse(allCategories);
            addGlobalCounterTotalForCategory(httpServletRequest, categoryJsonArray);
            addLocationCounterTotalForCategory(httpServletRequest, categoryJsonArray);
        }
        return allCategories;
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
        String redisKey = appKeyService.getGlobalComplaintCounterKey();
        List<Object> hashKeys = new ArrayList<>(categoryJsonArray.size());
        for (int i = 0; i < categoryJsonArray.size(); i++) {
            oneJsonObject = (JsonObject) categoryJsonArray.get(i);
            categoryId = oneJsonObject.get("id").getAsLong();
            hashKeys.add(appKeyService.getCategoryKey(categoryId));
        }
        List<Object> resultList = stringRedisTemplate.opsForHash().multiGet(redisKey, hashKeys);
       
        for (int i = 0; i < categoryJsonArray.size(); i++) {
            oneJsonObject = (JsonObject) categoryJsonArray.get(i);
            if (resultList.get(i) != null) {
                oneJsonObject.addProperty("globalCount", resultList.get(i).toString());
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
        
        String locationRedisKey = appKeyService.getLocationKey(locationId);
        
        List<Object> hashKeys = new ArrayList<>(categoryJsonArray.size());
        for (int i = 0; i < categoryJsonArray.size(); i++) {
            oneJsonObject = (JsonObject) categoryJsonArray.get(i);
            categoryId = oneJsonObject.get("id").getAsLong();
            hashKeys.add(appKeyService.getCategoryKey(categoryId));
        }
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
