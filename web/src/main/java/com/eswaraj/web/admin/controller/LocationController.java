package com.eswaraj.web.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LocationController extends BaseController {

    @RequestMapping(value = "/const.html", method = RequestMethod.GET)
    public ModelAndView showIndexPage(ModelAndView mv) {
        addGenericValues(mv);
        mv.setViewName("constituency");
        return mv;
    }
}
