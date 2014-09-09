package com.eswaraj.api.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.eswaraj.core.exceptions.ApplicationException;

@Controller
public class LocationController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private RedisTemplate<String, Long> redisTemplate;
    @Autowired
    private RedisUtil redisUtil;

    @RequestMapping(value = "/api/v0/getpointlocations", method = RequestMethod.GET)
    public @ResponseBody String getLocationAtPoint(HttpServletRequest httpServletRequest, ModelAndView mv) throws ApplicationException {
        logger.info("Lat = " + Double.parseDouble(httpServletRequest.getParameter("lat")));
        logger.info("Long = " + Double.parseDouble(httpServletRequest.getParameter("long")));
        // First Redis operation
        String redisKey = appKeyService.buildLocationKey(Double.parseDouble(httpServletRequest.getParameter("lat")), Double.parseDouble(httpServletRequest.getParameter("long")));
        logger.info("Redis Key = " + redisKey);
        Set<Long> locations = redisTemplate.opsForSet().members(redisKey);

        // Second redis Operation
        redisUtil.addLocationsExpandOperation(locations);
        List<Map> results = redisUtil.executeAll();
        return convertToJsonArray(results.get(0).values()).toString();
    }

}
