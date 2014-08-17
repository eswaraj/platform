package com.eswaraj.web.admin.controller;

import java.util.Date;
import java.util.List;

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
import com.eswaraj.core.service.CounterKeyService;
import com.eswaraj.core.service.LocationKeyService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Controller
public class ApiController {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private LocationKeyService locationKeyService;
    @Autowired
    private CounterKeyService counterKeyService;

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
}
