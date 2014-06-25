package com.eswaraj.web.admin.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.web.dto.ExecutiveBodyDto;
import com.eswaraj.web.dto.PoliticalBodyTypeDto;

@Controller
public class ExecutiveBodyController extends BaseController {

	/*
	@RequestMapping(value = "/ajax/eb/getall/{}", method = RequestMethod.GET)
	public @ResponseBody List<ExecutiveBodyDto> getExecutiveBody(ModelAndView mv) throws ApplicationException {
		List<PoliticalBodyTypeDto> politicalBodyTypes = appService.getAllPoliticalBodyTypes();
		return politicalBodyTypes;
	}
	
	@RequestMapping(value = "/ajax/pbtype/save", method = RequestMethod.POST)
	public @ResponseBody PoliticalBodyTypeDto savePoliticalBodyTypes(ModelAndView mv, @RequestBody PoliticalBodyTypeDto politicalBodyTypeDto) throws ApplicationException {
		PoliticalBodyTypeDto politicalBodyType = appService.savePoliticalBodyType(politicalBodyTypeDto);
		return politicalBodyType;
	}
	*/
}
