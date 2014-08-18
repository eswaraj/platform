package com.eswaraj.web.outh2.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {
	
	private static final Logger log = LoggerFactory.getLogger(TestController.class);
	
	@RequestMapping(value="/test", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody String test() {
		return "Hello world";
	}
}
