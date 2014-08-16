package com.eswaraj.web.admin.controller;

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
import com.eswaraj.core.service.LocationKeyService;

@Controller
public class ApiController {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private LocationKeyService locationKeyService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/api/location/{locationId}/info", method = RequestMethod.GET)
    @ResponseBody
    public String getLocationCategoryCount(ModelAndView mv, @PathVariable Long locationId) throws ApplicationException {
        String redisKeyForLocationInfo = locationKeyService.getLocationInformationKey(locationId);
        logger.info("getting data from Redis for key {}", redisKeyForLocationInfo);
        String data = stringRedisTemplate.opsForValue().get(redisKeyForLocationInfo);
        return data;
    }
}
