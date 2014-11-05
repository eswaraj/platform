package com.eswaraj.web.admin.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.AppService;
import com.eswaraj.core.service.LocationService;
import com.eswaraj.web.controller.beans.ComplaintBean;
import com.eswaraj.web.dto.UserDto;

@Controller
public class UserDashboardController extends BaseController {

    @Autowired
    @Qualifier("stringRedisTemplate")
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private LocationService locationService;
    @Autowired
    private AppService appService;
    @Autowired
    private ApiUtil apiUtil;

    @RequestMapping(value = "/dashboard.html", method = RequestMethod.GET)
    public ModelAndView showLocationPage(ModelAndView mv, HttpServletRequest httpServletRequest) throws ApplicationException {
        System.out.println("Request URI : " + httpServletRequest.getRequestURI());
        UserDto loggedInUser = sessionUtil.getLoggedInUserFromSession(httpServletRequest);
        List<ComplaintBean> complaints = apiUtil.getUserComplaints(httpServletRequest, loggedInUser.getId());
        mv.getModel().put("userComplaints", complaints);
        addGenericValues(mv, httpServletRequest);
        mv.setViewName("user");
        return mv;
    }

}
