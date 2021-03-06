package com.eswaraj.api.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.eswaraj.cache.CategoryCache;
import com.eswaraj.cache.ComplaintCache;
import com.eswaraj.cache.CounterCache;
import com.eswaraj.cache.LocationCache;
import com.eswaraj.cache.PoliticalAdminCache;
import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.AppKeyService;
import com.eswaraj.web.dto.CategoryWithChildCategoryDto;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Controller
public class ApiController extends BaseController {
    @Autowired
    @Qualifier("stringRedisTemplate")
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private LocationCache locationCache;
    @Autowired
    private AppKeyService appKeyService;
    @Autowired
    private RedisUtil redisUtil;
    private Gson gson = new Gson();
    @Autowired
    private ComplaintCache complaintCache;
    @Autowired
    private PoliticalAdminCache politicalAdminCache;
    @Autowired
    private CategoryCache categoryCache;
    @Autowired
    private CounterCache counterCache;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/api/v0/location/{locationId}/info", method = RequestMethod.GET)
    @ResponseBody
    public String getLocationCount(ModelAndView mv, @PathVariable Long locationId) throws ApplicationException {
        String locationInfo = locationCache.getLocationInfoById(locationId);
        return locationInfo;
    }

    /**
     * 
     * @param mv
     * @param locationId
     * @return
     * @throws ApplicationException
     */
    @RequestMapping(value = "/api/v0/location/{locationId}/leaders", method = RequestMethod.GET)
    @ResponseBody
    public String getLocationLeaders(ModelAndView mv, @PathVariable Long locationId) throws ApplicationException {
        Set<String> pbAdminIds = locationCache.getLocationPoliticalAdmins(locationId);
        JsonArray jsonArray = politicalAdminCache.getPoliticalBodyAdminByIds(pbAdminIds);
        return jsonArray.toString();
    }
    @RequestMapping(value = "/api/v0/location/{locationId}/complaintcounts/last30", method = RequestMethod.GET)
    @ResponseBody
    public String getLocationComplaintCountForLast30Days(ModelAndView mv, @PathVariable Long locationId) throws ApplicationException {
        JsonObject returnObject = counterCache.getLast30DayLocationCounters(locationId, new Date());
        return returnObject.toString();
    }

    /**
     * {
          "ts":  [
                  {
                    "key": "No water", 
                    "values": [ [ 1025409600000 , 0] , [ 1028088000000 , -60.3382185140371]}
                   }
                 }
        } 
     * @param mv
     * @param locationId
     * @return
     * @throws ApplicationException
     */
    @RequestMapping(value = "/api/v0/location/{locationId}/complaintcounts/last365", method = RequestMethod.GET)
    @ResponseBody
    public String getLocationComplaintCountForLast365Days(ModelAndView mv, @PathVariable Long locationId) throws ApplicationException {
        String redisKey = appKeyService.getLocationCounterKey(locationId);
        List<CategoryWithChildCategoryDto> allCategories = getAllCategories();
        JsonArray ts = new JsonArray();
        JsonArray categoryArray = new JsonArray();
        for (CategoryWithChildCategoryDto oneCategory : allCategories) {
            JsonObject oneCategoryResult = processOneCategory(redisKey, oneCategory);
            ts.add(oneCategoryResult);
            
            JsonObject catObject = new JsonObject();
            catObject.addProperty("name", oneCategory.getName());
            catObject.addProperty("count", 0);
            catObject.addProperty("color", "#98abc5");
            categoryArray.add(catObject);
        }

        JsonObject returnObject = new JsonObject();
        returnObject.add("ts", ts);
        returnObject.add("cat", categoryArray);
        return returnObject.toString();
    }

    private JsonObject processOneCategory(String redisKey, CategoryWithChildCategoryDto category) throws ApplicationException {
        String categoryHashKeyPrefix = appKeyService.getCategoryKey(category.getId());
        List redisKeyForLocation365DaysCounter = appKeyService.getHourComplaintKeysForLastNDays(categoryHashKeyPrefix, new Date(), 365);
        logger.info("getting data from Redis for keys {}", redisKeyForLocation365DaysCounter);
        logger.info("categoryHashKeyPrefix :  {}", categoryHashKeyPrefix);
        List<Object> data = stringRedisTemplate.opsForHash().multiGet(redisKey, redisKeyForLocation365DaysCounter);
        Long totalComplaints = 0L;
        int count = 0;
        String oneKey;
        Long value;
        DateFormat dayFormat = new SimpleDateFormat("yyyyMMdd");
        Date date;
        JsonArray jsonArray = new JsonArray();
        Map<Long, Long> counterMapByDate = new LinkedHashMap<>();
        for (Object oneCounter : data) {
            oneKey = (String)redisKeyForLocation365DaysCounter.get(count);
            oneKey = oneKey.replace(categoryHashKeyPrefix + ".", "");
            try {
                date = dayFormat.parse(oneKey);
            } catch (ParseException e) {
                throw new ApplicationException(e);
            }
            if (oneCounter != null) {
                value = Long.parseLong((String) oneCounter);
                totalComplaints = totalComplaints + value;
            } else {
                value = 0L;
            }
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(String.valueOf(date.getTime()), value);
            jsonArray.add(jsonObject);
            count++;
        }
        JsonObject returnJsonObject = new JsonObject();
        returnJsonObject.addProperty("key", category.getName());
        returnJsonObject.add("values", jsonArray);

        return returnJsonObject;
    }

    List<CategoryWithChildCategoryDto> getAllCategories() throws ApplicationException {
        String allCategories = categoryCache.getAllCategories();
        List<CategoryWithChildCategoryDto> list = gson.fromJson(allCategories, new TypeToken<List<CategoryWithChildCategoryDto>>() {
        }.getType());
        return list;
    }

    @RequestMapping(value = "/api/v0/complaint/location/{locationId}", method = RequestMethod.GET)
    @ResponseBody
    public String getComplaintsOfLocation(ModelAndView mv, HttpServletRequest httpServletRequest, @PathVariable Long locationId) throws ApplicationException {
        int start = getIntParameter(httpServletRequest, "start", 0);
        int count = getIntParameter(httpServletRequest, "count", 10);

        return complaintCache.getComplaintsOfLocation(locationId, start, count);
    }

    @RequestMapping(value = "/api/v0/complaint/location/{locationId}/{categoryId}", method = RequestMethod.GET)
    @ResponseBody
    public String getComplaintsOfLocationAndCategory(ModelAndView mv, HttpServletRequest httpServletRequest, @PathVariable Long locationId, @PathVariable Long categoryId)
            throws ApplicationException {
        int start = getIntParameter(httpServletRequest, "start", 0);
        int count = getIntParameter(httpServletRequest, "count", 10);

        return complaintCache.getComplaintsOfLocationCategory(locationId, categoryId, start, count);

    }

}
