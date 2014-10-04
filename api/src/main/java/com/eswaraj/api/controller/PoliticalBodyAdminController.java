package com.eswaraj.api.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.eswaraj.core.exceptions.ApplicationException;

@Controller
public class PoliticalBodyAdminController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private RedisTemplate<String, Long> redisTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisUtil redisUtil;

    @RequestMapping(value = "/api/v0/leader", method = RequestMethod.GET)
    public @ResponseBody String getLeader(HttpServletRequest httpServletRequest, ModelAndView mv) throws ApplicationException {
        String urlKey = httpServletRequest.getParameter("urlkey");
        String locationType = httpServletRequest.getParameter("locationType");
        logger.info("urlkey = " + urlKey);
        logger.info("locationType = " + locationType);
        // First Redis Operation
        String locationIdString = stringRedisTemplate.opsForValue().get(urlKey);
        // Second Redis operation
        List<Map> results = redisUtil.executeAll();
        return convertToJsonArray(results.get(0).values()).toString();
    }

}
