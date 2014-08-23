package com.eswaraj.web.admin.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.AppKeyService;
import com.eswaraj.core.service.CounterKeyService;
import com.eswaraj.core.service.LocationKeyService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Controller
public class ApiController {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private LocationKeyService locationKeyService;
    @Autowired
    private CounterKeyService counterKeyService;
    @Autowired
    private AppKeyService appKeyService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/api/location/{locationId}/info", method = RequestMethod.GET)
    @ResponseBody
    public String getLocationCategoryCount(ModelAndView mv, @PathVariable Long locationId) throws ApplicationException {
        String redisKeyForLocationInfo = locationKeyService.getLocationInformationKey(locationId);
        logger.info("getting data from Redis for key {}", redisKeyForLocationInfo);
        String data = stringRedisTemplate.opsForValue().get(redisKeyForLocationInfo);
        return data;
    }

    @RequestMapping(value = "/api/location/{locationId}/complaintcounts/last30", method = RequestMethod.GET)
    @ResponseBody
    public String getLocationComplaintCountForLast30Days(ModelAndView mv, @PathVariable Long locationId) throws ApplicationException {
        String keyPrefix = counterKeyService.getLocationKeyPrefix(locationId);
        List<String> redisKeyForLocation30DaysCounter = counterKeyService.getHourComplaintKeysForLast30Days(keyPrefix, new Date());
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

    @RequestMapping(value = "/api/complaint/location/{locationId}", method = RequestMethod.GET)
    @ResponseBody
    public String getComplaintsOfLocation(ModelAndView mv, HttpServletRequest httpServletRequest, @PathVariable Long locationId) throws ApplicationException {
        int start = getIntParameter(httpServletRequest, "start", 0);
        int end = getIntParameter(httpServletRequest, "end", 20);

        String locationComplaintKey = locationKeyService.getLocationComplaintsKey(locationId);
        logger.info("locationComplaintKey : {}", locationComplaintKey);
        
        return getComplaintsOfKey(locationComplaintKey, start, end);
    }

    @RequestMapping(value = "/api/complaint/location/{locationId}/{categoryId}", method = RequestMethod.GET)
    @ResponseBody
    public String getComplaintsOfLocationAndCategory(ModelAndView mv, HttpServletRequest httpServletRequest, @PathVariable Long locationId, @PathVariable Long categoryId)
            throws ApplicationException {
        int start = getIntParameter(httpServletRequest, "start", 0);
        int end = getIntParameter(httpServletRequest, "end", 20);

        String locationComplaintCategoryKey = locationKeyService.getLocationCategoryComplaintsKey(locationId, categoryId);
        logger.info("locationComplaintKey : {}", locationComplaintCategoryKey);
        return getComplaintsOfKey(locationComplaintCategoryKey, start, end);

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

    private String getComplaintsOfKey(String key, int start, int end) {
        Set<String> complaintIds = stringRedisTemplate.opsForZSet().range(key, start, end);
        logger.info("complaintIds : {}", complaintIds);
        List<String> complaintKeys = new ArrayList<>();
        for (String oneComplaintId : complaintIds) {
            complaintKeys.add(appKeyService.getComplaintObjectKey(oneComplaintId));
        }
        List<String> complaintList = stringRedisTemplate.opsForValue().multiGet(complaintKeys);
        return convertList(complaintList);
    }

    private int getIntParameter(HttpServletRequest httpServletRequest, String parameter, int defaultValue) {
        String paramValue = httpServletRequest.getParameter(parameter);
        if (paramValue == null) {
            return defaultValue;
        }
        return Integer.parseInt(paramValue);
    }
}
