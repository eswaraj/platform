package com.eswaraj.api.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.AppKeyService;
import com.eswaraj.core.service.AppService;
import com.eswaraj.web.dto.PoliticalBodyAdminStaffDto;
import com.eswaraj.web.dto.SavePoliticalAdminStaffRequestDto;

@Controller
public class PoliticalBodyAdminController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private RedisTemplate<String, Long> redisTemplate;
    @Autowired
    @Qualifier("stringRedisTemplate")
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private AppKeyService appKeyService;
    @Autowired
    private AppService appService;

    @RequestMapping(value = "/api/v0/leader", method = RequestMethod.GET)
    public @ResponseBody String getLeader(HttpServletRequest httpServletRequest, ModelAndView mv) throws ApplicationException {
        String urlKey = httpServletRequest.getParameter("urlkey");
        String locationType = httpServletRequest.getParameter("locationType");
        logger.info("urlkey = {}", urlKey);
        logger.info("locationType = {}", locationType);
        // First Redis Operation
        String pbAdminIdString = String.valueOf(stringRedisTemplate.opsForHash().get(appKeyService.getPoliticalBodyAdminUrlsKey(), urlKey));
        logger.info("pbAdminIdString = {}", locationType);
        // Second Redis operation
        String pbRedisKey = appKeyService.getPoliticalBodyAdminObjectKey(String.valueOf(pbAdminIdString));
        logger.info("pbRedisKey = {}", pbRedisKey);
        String hashKey = appKeyService.getEnityInformationHashKey();
        String pbInfo = (String) stringRedisTemplate.opsForHash().get(pbRedisKey, hashKey);
        return pbInfo;
    }

    @RequestMapping(value = "/api/v0/leader/staff", method = RequestMethod.POST)
    public @ResponseBody List<PoliticalBodyAdminStaffDto> saveLeaderStaff(HttpServletRequest httpServletRequest, ModelAndView mv,
            @RequestBody SavePoliticalAdminStaffRequestDto savePoliticalAdminStaffRequestDto) throws ApplicationException {
        appService.savePoliticalBodyAdminStaff(savePoliticalAdminStaffRequestDto);
        List<PoliticalBodyAdminStaffDto> politicalBodyAdminStaffs = appService.getAllStaffOfPoliticalAdmin(savePoliticalAdminStaffRequestDto.getPoliticalAdminId());
        return politicalBodyAdminStaffs;
    }

    @RequestMapping(value = "/api/v0/leader/staff/{politicalAdminId}", method = RequestMethod.GET)
    public @ResponseBody List<PoliticalBodyAdminStaffDto> getLeaderStaff(HttpServletRequest httpServletRequest, ModelAndView mv, @PathVariable Long politicalAdminId) throws ApplicationException {
        List<PoliticalBodyAdminStaffDto> politicalBodyAdminStaffs = appService.getAllStaffOfPoliticalAdmin(politicalAdminId);
        return politicalBodyAdminStaffs;
    }

    @RequestMapping(value = "/api/v0/leader/staff/{politicalAdminStaffId}", method = RequestMethod.DELETE)
    public @ResponseBody PoliticalBodyAdminStaffDto deleteLeaderStaff(HttpServletRequest httpServletRequest, ModelAndView mv, @PathVariable Long politicalAdminStaffId)
            throws ApplicationException {
        PoliticalBodyAdminStaffDto politicalBodyAdminStaffDto = appService.deletePoliticalAdminStaff(politicalAdminStaffId);
        return politicalBodyAdminStaffDto;
    }

}
