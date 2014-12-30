package com.eswaraj.api.controller;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.eswaraj.cache.CategoryCache;
import com.eswaraj.cache.CounterCache;
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
    private AppKeyService appKeyService;
    @Autowired
    private CategoryCache categoryCache;
    @Autowired
    private CounterCache counterCache;

    private Gson gson = new Gson();

    @RequestMapping(value = "/stats/counter/location/{locationId}/category/{categoryId}", method = RequestMethod.GET)
    @ResponseBody
    public String getLocationCategoryCount(ModelAndView mv, @PathVariable Long locationId, @PathVariable Long categoryId) throws ApplicationException {
        Long count = counterCache.getLocationCategoryComplaintCounter(locationId, categoryId);
        JsonObject jsonObject = new JsonObject();
        if (count == null) {
            jsonObject.addProperty("count", 0);
        } else {
            jsonObject.addProperty("count", count);
        }
        return jsonObject.toString();
    }

    @RequestMapping(value = "/stats/counter/location/{locationId}/parentcategory/{parentCategoryId}", method = RequestMethod.GET)
    @ResponseBody
    public String getLocationParentCategoryCounts(ModelAndView mv, @PathVariable Long locationId, @PathVariable Long parentCategoryId) throws ApplicationException {

        String allCategories = categoryCache.getAllCategories();
        Type listType = new TypeToken<ArrayList<CategoryWithChildCategoryDto>>() {
        }.getType();
        List<CategoryWithChildCategoryDto> categories = new Gson().fromJson(allCategories, listType);
        List<CategoryWithChildCategoryDto> childCategories = null;
        for (CategoryWithChildCategoryDto oneCategoryWithChildCategoryDto : categories) {
            if (oneCategoryWithChildCategoryDto.getId().equals(parentCategoryId)) {
                childCategories = oneCategoryWithChildCategoryDto.getChildCategories();
            }
        }

        if (childCategories != null) {
            JsonArray jsonArray = getCountersForLocationAndCategories(locationId, childCategories);
            return jsonArray.toString();
        }
        return "[]";
    }

    @RequestMapping(value = "/stats/counter/location/{locationId}", method = RequestMethod.GET)
    @ResponseBody
    public String getLocationAllCategoryCounts(ModelAndView mv, @PathVariable Long locationId) throws ApplicationException {

        String allCategories = categoryCache.getAllCategories();

        Type listType = new TypeToken<ArrayList<CategoryWithChildCategoryDto>>() {
        }.getType();
        List<CategoryWithChildCategoryDto> categories = new Gson().fromJson(allCategories, listType);

        JsonArray jsonArray = getCountersForLocationAndCategories(locationId, categories);
        return jsonArray.toString();
    }

    private JsonArray getCountersForLocationAndCategories(Long locationId, List<CategoryWithChildCategoryDto> categories) throws ApplicationException {
        JsonArray jsonArray = new JsonArray();
        if (categories != null && !categories.isEmpty()) {
            List<Long> allCategoryIds = new ArrayList<>(categories.size());
            for (CategoryDto oneChildCategory : categories) {
                allCategoryIds.add(oneChildCategory.getId());
            }

            List<Long> result = counterCache.getLocationCategoryComplaintCounter(locationId, allCategoryIds);
            int i = 0;
            for (Long oneCount : result) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("count", oneCount);
                jsonObject.addProperty("name", categories.get(i).getName());
                jsonObject.addProperty("id", categories.get(i).getId());
                jsonArray.add(jsonObject);
                i++;
            }
        }
        return jsonArray;
    }

}
