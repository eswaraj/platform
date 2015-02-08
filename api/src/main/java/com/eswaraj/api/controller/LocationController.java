package com.eswaraj.api.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.eswaraj.cache.LocationCache;
import com.eswaraj.cache.LocationPointCache;
import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.LocationService;
import com.eswaraj.web.dto.LocationDto;

@Controller
public class LocationController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private LocationPointCache locationPointCache;
    @Autowired
    private LocationCache locationCache;
    @Autowired
    private LocationService locationService;

    @RequestMapping(value = "/api/v0/getpointlocations", method = RequestMethod.GET)
    public @ResponseBody String getLocationAtPoint(HttpServletRequest httpServletRequest, ModelAndView mv) throws ApplicationException {
        logger.info("Lat = " + Double.parseDouble(httpServletRequest.getParameter("lat")));
        logger.info("Long = " + Double.parseDouble(httpServletRequest.getParameter("long")));
        // First Redis operation
        Set<Long> locations = locationPointCache.getPointLocations(Double.parseDouble(httpServletRequest.getParameter("lat")), Double.parseDouble(httpServletRequest.getParameter("long")));
        // Second redis Operation
        redisUtil.addLocationsExpandOperation(locations);
        List<Map> results = redisUtil.executeAll();
        return convertToJsonArray(results.get(0).values()).toString();
    }

    @RequestMapping(value = "/api/v0/location/{locationId}", method = RequestMethod.GET)
    public @ResponseBody String getLocationById(HttpServletRequest httpServletRequest, ModelAndView mv, @PathVariable String locationId) throws ApplicationException {
        return locationCache.getLocationInfoById(locationId);
    }


    @RequestMapping(value = "/api/v0/location/getchild/{parentId}", method = RequestMethod.GET)
    public @ResponseBody List<LocationDto> getChildLocationNode(ModelAndView mv, @PathVariable Long parentId) throws ApplicationException {
        List<LocationDto> locationDtos = locationService.getChildLocationsOfParent(parentId);
        return locationDtos;
    }

}
