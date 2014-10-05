package com.eswaraj.web.admin.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.eswaraj.web.dto.UserDto;
import com.eswaraj.web.session.SessionUtil;

@Controller
public class MyLeaderController {

    @Autowired
    private SessionUtil sessionUtil;

    @RequestMapping(value = "/mymla", method = RequestMethod.GET)
    public ModelAndView showMyAcPage(ModelAndView mv, HttpServletRequest httpServletRequest) {
        UserDto loggedInUser = sessionUtil.getLoggedInUserFromSession(httpServletRequest);
        String redirectUrl = "/index.html";
        if (loggedInUser.getPerson().getPersonAddress() == null || loggedInUser.getPerson().getPersonAddress().getAc() == null) {
            redirectUrl = "/editprofile.html";
        } else {
            redirectUrl = "/leader" + loggedInUser.getPerson().getPersonAddress().getAc().getUrlIdentifier() + "/mla.html";
        }
        RedirectView rv = new RedirectView(redirectUrl);
        mv.setView(rv);
        return mv;
    }

    @RequestMapping(value = "/mymp", method = RequestMethod.GET)
    public ModelAndView showPcPage(ModelAndView mv, HttpServletRequest httpServletRequest) {
        UserDto loggedInUser = sessionUtil.getLoggedInUserFromSession(httpServletRequest);
        String redirectUrl = "/index.html";
        if (loggedInUser.getPerson().getPersonAddress() == null || loggedInUser.getPerson().getPersonAddress().getPc() == null) {
            redirectUrl = "/editprofile.html";
        } else {
            redirectUrl = "/leader" + loggedInUser.getPerson().getPersonAddress().getPc().getUrlIdentifier() + "/mp.html";
        }
        RedirectView rv = new RedirectView(redirectUrl);
        mv.setView(rv);
        return mv;
    }

    @RequestMapping(value = "/mywardmember", method = RequestMethod.GET)
    public ModelAndView showWardPage(ModelAndView mv, HttpServletRequest httpServletRequest) {
        UserDto loggedInUser = sessionUtil.getLoggedInUserFromSession(httpServletRequest);
        String redirectUrl = "/index.html";
        if (loggedInUser.getPerson().getPersonAddress() == null || loggedInUser.getPerson().getPersonAddress().getWard() == null) {
            redirectUrl = "/editprofile.html";
        } else {
            redirectUrl = "/leader" + loggedInUser.getPerson().getPersonAddress().getWard().getUrlIdentifier() + "/ward.html";
        }
        RedirectView rv = new RedirectView(redirectUrl);
        mv.setView(rv);
        return mv;
    }
}
