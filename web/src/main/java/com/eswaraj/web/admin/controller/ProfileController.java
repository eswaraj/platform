package com.eswaraj.web.admin.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.LocalDate;
import org.joda.time.Years;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.eswaraj.core.service.AppService;
import com.eswaraj.core.service.LocationService;
import com.eswaraj.web.dto.UserDto;

@Controller
public class ProfileController extends BaseController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private LocationService locationService;
    @Autowired
    private AppService appService;
    @Autowired
    private ApiUtil apiUtil;

    @RequestMapping(value = "/editprofile.html", method = RequestMethod.GET)
    public ModelAndView showLocationPage(ModelAndView mv, HttpServletRequest httpServletRequest) {
        System.out.println("Request URI : " + httpServletRequest.getRequestURI());
        addGenericValues(mv, httpServletRequest);
        addLoggedInUserAge(mv, httpServletRequest);
        mv.setViewName("editprofile");
        return mv;
    }

    protected void addLoggedInUserAge(ModelAndView mv, HttpServletRequest httpServletRequest) {
        UserDto loggedInUser = sessionUtil.getLoggedInUserFromSession(httpServletRequest);
        Date dob = loggedInUser.getPerson().getDob();
        if (dob != null) {
            LocalDate birthdate = new LocalDate(dob);
            LocalDate now = new LocalDate();
            Years age = Years.yearsBetween(birthdate, now);
            mv.getModel().put("age", age.getYears());
        }

    }

}
