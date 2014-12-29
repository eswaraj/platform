package com.eswaraj.api.controller;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.eswaraj.cache.CategoryCache;
import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.AppKeyService;
import com.eswaraj.web.dto.CategoryDto;
import com.eswaraj.web.dto.CategoryWithChildCategoryDto;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Controller
public class AnalyticsCounterController extends BaseController {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private AppKeyService appKeyService;
    @Autowired
    private CategoryCache categoryCache;

    private Gson gson = new Gson();

    @RequestMapping(value = "/stats/counter/location/{locationId}/category/{categoryId}", method = RequestMethod.GET)
    @ResponseBody
    public String getLocationCategoryCount(ModelAndView mv, @PathVariable Long locationId, @PathVariable Long categoryId) throws ApplicationException {
        String keyPreFix = appKeyService.getLocationCategoryKeyPrefix(locationId, categoryId);
        String redisKeyForAllTime = appKeyService.getTotalComplaintCounterKey(keyPreFix);
        logger.info("getting data from Redis for key {}", redisKeyForAllTime);
        Long count = (Long) redisTemplate.opsForValue().get(redisKeyForAllTime);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("count", count);
        return jsonObject.toString();
    }

    @RequestMapping(value = "/stats/counter/location/{locationId}/parentcategory/{parentCategoryId}", method = RequestMethod.GET)
    @ResponseBody
    public String getLocationParentCategoryCounts(ModelAndView mv, @PathVariable Long locationId, @PathVariable Long parentCategoryId) throws ApplicationException {
        /*
        List<CategoryDto> childCategories = appService.getAllChildCategoryOfParentCategory(parentCategoryId);
        JsonArray jsonArray = getCountersForLocationAndCategories(locationId, childCategories);
        return jsonArray.toString();
        */
        return "dsd";
    }

    @RequestMapping(value = "/stats/counter/location/{locationId}", method = RequestMethod.GET)
    @ResponseBody
    public String getLocationAllCategoryCounts(ModelAndView mv, @PathVariable Long locationId, @PathVariable Long parentCategoryId) throws ApplicationException {

        String allCategories = categoryCache.getAllCategories();

        Type listType = new TypeToken<ArrayList<CategoryWithChildCategoryDto>>() {
        }.getType();
        List<CategoryWithChildCategoryDto> categories = new Gson().fromJson(allCategories, listType);

        JsonArray jsonArray = getCountersForLocationAndCategories(locationId, categories);
        return jsonArray.toString();
    }

    private JsonArray getCountersForLocationAndCategories(Long locationId, List<CategoryWithChildCategoryDto> categories) {
        JsonArray jsonArray = new JsonArray();
        if (categories != null && !categories.isEmpty()) {
            List<String> allKeys = new ArrayList<>(categories.size());
            for (CategoryDto oneChildCategory : categories) {
                String keyPreFix = appKeyService.getLocationCategoryKeyPrefix(locationId, oneChildCategory.getId());
                String redisKeyForAllTime = appKeyService.getTotalComplaintCounterKey(keyPreFix);
                logger.info("getting data from Redis for key {}", redisKeyForAllTime);
                allKeys.add(redisKeyForAllTime);
            }

            List<Long> result = redisTemplate.opsForValue().multiGet(allKeys);
            int i = 0;
            for (Long oneCount : result) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("count", oneCount);
                jsonObject.addProperty("name", categories.get(i).getName());
                jsonObject.addProperty("id", categories.get(i).getId());
                jsonArray.add(jsonObject);
            }
        }
        return jsonArray;
    }

}
