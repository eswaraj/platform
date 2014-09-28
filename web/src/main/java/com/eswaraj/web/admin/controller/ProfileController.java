package com.eswaraj.web.admin.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.LocalDate;
import org.joda.time.Years;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.AppService;
import com.eswaraj.core.service.LocationService;
import com.eswaraj.web.dto.UpdateUserRequestWebDto;
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
        UserDto loggedInUser = sessionUtil.getLoggedInUserFromSession(httpServletRequest);
        UpdateUserRequestWebDto updateUserRequestWebDto = new UpdateUserRequestWebDto();
        updateUserRequestWebDto.setName(loggedInUser.getPerson().getName());
        updateUserRequestWebDto.setVoterId(loggedInUser.getPerson().getVoterId());
        if (loggedInUser.getPerson().getPersonAddress() != null) {
            updateUserRequestWebDto.setLattitude(loggedInUser.getPerson().getPersonAddress().getLattitude());
            updateUserRequestWebDto.setLongitude(loggedInUser.getPerson().getPersonAddress().getLongitude());
        }
        mv.getModel().put("profile", updateUserRequestWebDto);
        mv.setViewName("editprofile");
        return mv;
    }

    @RequestMapping(value = "/editprofile.html", method = RequestMethod.POST)
    public ModelAndView saveUser(ModelAndView mv, HttpServletRequest httpServletRequest, @ModelAttribute("profile") UpdateUserRequestWebDto updateUserRequestWebDto, BindingResult result)
            throws ApplicationException {
        System.out.println("Request URI : " + httpServletRequest.getRequestURI());
        UserDto loggedInUser = sessionUtil.getLoggedInUserFromSession(httpServletRequest);
        updateUserRequestWebDto.setUserId(loggedInUser.getId());
        logger.info("Saving user : {}", updateUserRequestWebDto);
        UserDto user = apiUtil.updateUserProfile(httpServletRequest, updateUserRequestWebDto);
        sessionUtil.setLoggedInUserinSession(httpServletRequest, user);
        addGenericValues(mv, httpServletRequest);
        addLoggedInUserAge(mv, httpServletRequest);
        mv.getModel().put("profile", updateUserRequestWebDto);
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
