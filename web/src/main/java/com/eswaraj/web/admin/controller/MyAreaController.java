package com.eswaraj.web.admin.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.eswaraj.web.dto.UserDto;
import com.eswaraj.web.session.SessionUtil;

@Controller
public class MyAreaController {

    @Autowired
    private SessionUtil sessionUtil;

    @RequestMapping(value = "/state/**", method = RequestMethod.GET)
    public ModelAndView showLocationPage(ModelAndView mv, HttpServletRequest httpServletRequest) {
        UserDto loggedInUser = sessionUtil.getLoggedInUserFromSession(httpServletRequest);
        return mv;
    }
}
