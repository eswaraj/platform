package com.eswaraj.api.controller;

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

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.CounterKeyService;
import com.eswaraj.web.dto.CategoryDto;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Controller
public class AnalyticsCounterController extends BaseController {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private CounterKeyService counterKeyService;

    @RequestMapping(value = "/stats/counter/location/{locationId}/category/{categoryId}", method = RequestMethod.GET)
    @ResponseBody
    public String getLocationCategoryCount(ModelAndView mv, @PathVariable Long locationId, @PathVariable Long categoryId) throws ApplicationException {
        String keyPreFix = counterKeyService.getLocationCategoryKeyPrefix(locationId, categoryId);
        String redisKeyForAllTime = counterKeyService.getTotalComplaintCounterKey(keyPreFix);
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
        /*
         * List<CategoryDto> rootCategories = appService.getAllRootCategories();
         * JsonArray jsonArray = getCountersForLocationAndCategories(locationId,
         * rootCategories); return jsonArray.toString();
         */
        return "Test";
    }

    private JsonArray getCountersForLocationAndCategories(Long locationId, List<CategoryDto> categories) {
        JsonArray jsonArray = new JsonArray();
        if (categories != null && !categories.isEmpty()) {
            List<String> allKeys = new ArrayList<>(categories.size());
            for (CategoryDto oneChildCategory : categories) {
                String keyPreFix = counterKeyService.getLocationCategoryKeyPrefix(locationId, oneChildCategory.getId());
                String redisKeyForAllTime = counterKeyService.getTotalComplaintCounterKey(keyPreFix);
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
