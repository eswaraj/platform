package com.eswaraj.web.admin.controller;

import java.util.List;

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
import com.eswaraj.web.controller.beans.ComplaintBean;

@Controller
public class ApiController {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private AppKeyService appKeyService;
    @Autowired
    private ApiUtil apiUtil;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/api/location/{locationId}/info", method = RequestMethod.GET)
    @ResponseBody
    public String getLocationCategoryCount(ModelAndView mv, HttpServletRequest httpServletRequest, @PathVariable Long locationId) throws ApplicationException {
        return apiUtil.getResponseFrom(httpServletRequest, "/api/v0/location/" + locationId + "/info");
    }

    @RequestMapping(value = "/api/location/{locationId}/complaintcounts/last30", method = RequestMethod.GET)
    @ResponseBody
    public String getLocationComplaintCountForLast30Days(ModelAndView mv, HttpServletRequest httpServletRequest, @PathVariable Long locationId) throws ApplicationException {
        return apiUtil.getResponseFrom(httpServletRequest, "/api/v0/location/" + locationId + "/complaintcounts/last30");
    }

    @RequestMapping(value = "/api/complaint/location/{locationId}", method = RequestMethod.GET)
    @ResponseBody
    public List<ComplaintBean> getComplaintsOfLocation(ModelAndView mv, HttpServletRequest httpServletRequest, @PathVariable Long locationId) throws ApplicationException {
        return apiUtil.getLocationComplaints(httpServletRequest, locationId);
    }

    @RequestMapping(value = "/api/complaint/location/{locationId}/{categoryId}", method = RequestMethod.GET)
    @ResponseBody
    public String getComplaintsOfLocationAndCategory(ModelAndView mv, HttpServletRequest httpServletRequest, @PathVariable Long locationId, @PathVariable Long categoryId)
            throws ApplicationException {
        return apiUtil.getResponseFrom(httpServletRequest, "/api/v0/complaint/location/" + locationId + "/" + categoryId);

    }

}
