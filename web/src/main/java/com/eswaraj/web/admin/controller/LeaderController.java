package com.eswaraj.web.admin.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LeaderController extends BaseController {

    @RequestMapping(value = "/leader.html", method = RequestMethod.GET)
    public ModelAndView showIndexPage(ModelAndView mv, HttpServletRequest httpServletRequest) {
        addGenericValues(mv, httpServletRequest);
        mv.setViewName("leader");
        return mv;
    }
}
