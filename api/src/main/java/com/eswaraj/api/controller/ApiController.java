package com.eswaraj.api.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.eswaraj.cache.ComplaintCache;
import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.AppKeyService;
import com.eswaraj.web.dto.CategoryWithChildCategoryDto;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Controller
public class ApiController extends BaseController {
    @Autowired
    @Qualifier("stringRedisTemplate")
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private AppKeyService appKeyService;
    @Autowired
    private RedisUtil redisUtil;
    private JsonParser jsonParser = new JsonParser();
    private Gson gson = new Gson();
    @Autowired
    private ComplaintCache complaintCache;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/api/v0/location/{locationId}/info", method = RequestMethod.GET)
    @ResponseBody
    public String getLocationCategoryCount(ModelAndView mv, @PathVariable Long locationId) throws ApplicationException {
        List<Long> locationIds = new ArrayList<>(1);
        locationIds.add(locationId);
        String redisKey = appKeyService.getLocationKey(locationId);
        String infoHashKey = appKeyService.getEnityInformationHashKey();
        String locationInfo = (String) stringRedisTemplate.opsForHash().get(redisKey, infoHashKey);
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
        String redisKey = appKeyService.getLocationKey(locationId);
        String hashKey = appKeyService.getPoliticalBodyAdminHashKey();
        String pbAdminCommaSepratedList = (String) stringRedisTemplate.opsForHash().get(redisKey, hashKey);
        if (StringUtils.isEmpty(pbAdminCommaSepratedList)) {
            return "[]";
        }
        String[] pbAdmiIds = pbAdminCommaSepratedList.split(",");
        String infoHashKey = appKeyService.getEnityInformationHashKey();
        JsonObject jsonObject;
        JsonArray jsonArray = new JsonArray();
        for (String oneString : pbAdmiIds) {
            String pbAdminRedisKey = appKeyService.getPoliticalAdminKey(Long.parseLong(oneString));
            String oneAdmin = (String) stringRedisTemplate.opsForHash().get(pbAdminRedisKey, infoHashKey);
            jsonObject = (JsonObject) jsonParser.parse(oneAdmin);
            jsonArray.add(jsonObject);
        }
        return jsonArray.toString();
    }
    @RequestMapping(value = "/api/v0/location/{locationId}/complaintcounts/last30", method = RequestMethod.GET)
    @ResponseBody
    public String getLocationComplaintCountForLast30Days(ModelAndView mv, @PathVariable Long locationId) throws ApplicationException {
        String keyPrefix = appKeyService.getLocationKey(locationId);
        List<String> redisKeyForLocation30DaysCounter = appKeyService.getHourComplaintKeysForLast30Days(keyPrefix, new Date());
        logger.info("getting data from Redis for keys {}", redisKeyForLocation30DaysCounter);
        List<String> data = stringRedisTemplate.opsForValue().multiGet(redisKeyForLocation30DaysCounter);
        Long totalComplaints = 0L;
        int count = 0;
        JsonArray jsonArray = new JsonArray();
        JsonObject jsonObject;
        for (String oneString : data) {
            jsonObject = new JsonObject();
            if (oneString != null) {
                jsonObject.addProperty(redisKeyForLocation30DaysCounter.get(count).replace(keyPrefix, ""), oneString);
                totalComplaints = totalComplaints + Long.parseLong(oneString);
            } else {
                jsonObject.addProperty(redisKeyForLocation30DaysCounter.get(count).replace(keyPrefix, ""), "0");
            }
            jsonArray.add(jsonObject);
            count++;
        }
        JsonObject returnObject = new JsonObject();
        returnObject.addProperty("totalComplaints", totalComplaints);
        returnObject.add("dayWise", jsonArray);
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
        String redisKey = appKeyService.getLocationKey(locationId);
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

    List<CategoryWithChildCategoryDto> getAllCategories() {
        String allCategoriesKey = appKeyService.getAllCategoriesKey();
        String allCategories = stringRedisTemplate.opsForValue().get(allCategoriesKey);
        List<CategoryWithChildCategoryDto> list = gson.fromJson(allCategories, new TypeToken<List<CategoryWithChildCategoryDto>>() {
        }.getType());
        return list;
    }

    @RequestMapping(value = "/api/v0/complaint/location/{locationId}", method = RequestMethod.GET)
    @ResponseBody
    public String getComplaintsOfLocation(ModelAndView mv, HttpServletRequest httpServletRequest, @PathVariable Long locationId) throws ApplicationException {
        int start = getIntParameter(httpServletRequest, "start", 0);
        int count = getIntParameter(httpServletRequest, "count", 10);

        String locationComplaintKey = appKeyService.getLocationComplaintsKey(locationId);
        logger.info("locationComplaintKey : {}, start:{}, count{}", locationComplaintKey);
        
        return getComplaintsOfKey(locationComplaintKey, start, count);
    }

    @RequestMapping(value = "/api/v0/complaint/location/{locationId}/{categoryId}", method = RequestMethod.GET)
    @ResponseBody
    public String getComplaintsOfLocationAndCategory(ModelAndView mv, HttpServletRequest httpServletRequest, @PathVariable Long locationId, @PathVariable Long categoryId)
            throws ApplicationException {
        int start = getIntParameter(httpServletRequest, "start", 0);
        int count = getIntParameter(httpServletRequest, "count", 10);

        String locationComplaintCategoryKey = appKeyService.getLocationCategoryComplaintsKey(locationId, categoryId);
        logger.info("locationComplaintKey : {}", locationComplaintCategoryKey);
        return getComplaintsOfKey(locationComplaintCategoryKey, start, count);

    }

    private String convertList(List<String> complaints) {
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject;
        JsonArray jsonArray = new JsonArray();
        for (String oneComplaint : complaints) {
            jsonObject = (JsonObject) jsonParser.parse(oneComplaint);
            jsonArray.add(jsonObject);
        }
        return jsonArray.toString();
    }

    private String getComplaintsOfKey(String key, int start, int count) throws ApplicationException {
        Set<String> complaintIds = stringRedisTemplate.opsForZSet().reverseRange(key, start, start + count);
        logger.info("complaintIds : {}", complaintIds);
        List<String> complaintKeys = new ArrayList<>();
        List<String> complaintList = new ArrayList<>(complaintIds.size());
        for (String oneComplaintId : complaintIds) {
            complaintKeys.add(appKeyService.getComplaintObjectKey(oneComplaintId));
            complaintList.add(complaintCache.getComplaintById(oneComplaintId));
        }
        //List<String> complaintList = stringRedisTemplate.opsForValue().multiGet(complaintKeys);


        // Add Executive Admin Info
        JsonObject oneComplaintJsonObject;
        JsonParser jsonParser = new JsonParser();
        JsonArray complaintJsonArray = new JsonArray();
        for (String oneComplaint : complaintList) {
            oneComplaintJsonObject = (JsonObject) jsonParser.parse(oneComplaint);
            addExecutiveBodyAdminInformation(oneComplaintJsonObject);
            addPoliticalBodyAdminInformation(oneComplaintJsonObject);

            complaintJsonArray.add(oneComplaintJsonObject);
        }
        return convertList(complaintList);
    }

    private void addPoliticalBodyAdminInformation(JsonObject oneComplaintJsonObject) {
        if (oneComplaintJsonObject == null) {
            return;
        }
        if (oneComplaintJsonObject.get("pba") != null && !oneComplaintJsonObject.get("pba").isJsonNull()) {
            JsonArray jsonArray = oneComplaintJsonObject.get("pba").getAsJsonArray();
            // remove eba
            oneComplaintJsonObject.remove("pba");
            JsonObject ebaOneJsonObject;
            String redisKey;
            String hashKey;
            JsonArray pbaJsonInforArray = new JsonArray();
            for (int i = 0; i < jsonArray.size(); i++) {
                ebaOneJsonObject = (JsonObject) jsonArray.get(i);
                redisKey = appKeyService.getPoliticalBodyAdminObjectKey(ebaOneJsonObject.get("id").getAsString());
                hashKey = appKeyService.getEnityInformationHashKey();
                String ebaInfo = (String) stringRedisTemplate.opsForHash().get(redisKey, hashKey);
                if (ebaInfo != null) {
                    JsonObject oneEbaJsonObject = (JsonObject) jsonParser.parse(ebaInfo);
                    pbaJsonInforArray.add(oneEbaJsonObject);
                }
            }
            oneComplaintJsonObject.add("politicalAdmins", pbaJsonInforArray);

        }
    }

    private void addExecutiveBodyAdminInformation(JsonObject oneComplaintJsonObject) {
        if (oneComplaintJsonObject == null) {
            return;
        }
        if (oneComplaintJsonObject.get("eba") != null && !oneComplaintJsonObject.get("eba").isJsonNull()) {
            logger.info("eba is null = " + oneComplaintJsonObject.get("eba").isJsonNull());
            JsonArray jsonArray = oneComplaintJsonObject.get("eba").getAsJsonArray();
            // remove eba
            oneComplaintJsonObject.remove("eba");
            JsonObject ebaOneJsonObject;
            String redisKey;
            String hashKey;
            JsonArray ebaJsonInforArray = new JsonArray();
            for (int i = 0; i < jsonArray.size(); i++) {
                ebaOneJsonObject = (JsonObject) jsonArray.get(i);
                redisKey = appKeyService.getExecutiveBodyAdminObjectKey(ebaOneJsonObject.get("id").getAsString());
                hashKey = appKeyService.getEnityInformationHashKey();
                String ebaInfo = (String) stringRedisTemplate.opsForHash().get(redisKey, hashKey);
                if (ebaInfo != null) {
                    JsonObject oneEbaJsonObject = (JsonObject) jsonParser.parse(ebaInfo);
                    ebaJsonInforArray.add(oneEbaJsonObject);
                }
            }
            oneComplaintJsonObject.add("executiveAdmins", ebaJsonInforArray);

        }
    }

}
