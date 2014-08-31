package com.eswaraj.web.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.eswaraj.core.service.LocationService;

@Controller
public class IndexController extends BaseController {

	@Autowired
	private LocationService locationService;
	
    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
	public ModelAndView showIndexPage(ModelAndView mv) {
        addGenericValues(mv);
		mv.setViewName("home");
		return mv;
	}

}
