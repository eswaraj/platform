package com.eswaraj.web.admin.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.web.dto.ExecutiveBodyDto;

@Controller
public class ExecutiveBodyController extends BaseController {

    /*
	@RequestMapping(value = "/ajax/eb/getroot/{departmentId}", method = RequestMethod.GET)
	public @ResponseBody List<ExecutiveBodyDto> getExecutiveBody(ModelAndView mv, @PathVariable Long departmentId) throws ApplicationException {
		List<ExecutiveBodyDto> executiveBodyDtos = appService.getAllRootExecutiveBodyOfDepartment(departmentId);
		return executiveBodyDtos;
	}
	
	@RequestMapping(value = "/ajax/eb/getchild/{executiveBodyId}", method = RequestMethod.GET)
	public @ResponseBody List<ExecutiveBodyDto> getChildExecutiveBody(ModelAndView mv, @PathVariable Long executiveBodyId) throws ApplicationException {
		List<ExecutiveBodyDto> executiveBodyDtos = appService.getAllChildExecutiveBodyOfParent(executiveBodyId);
		return executiveBodyDtos;
	}

	@RequestMapping(value = "/ajax/eb/save", method = RequestMethod.POST)
	public @ResponseBody ExecutiveBodyDto savePoliticalBodyTypes(ModelAndView mv, @RequestBody ExecutiveBodyDto executiveBodyDto) throws ApplicationException {
		executiveBodyDto = appService.saveExecutiveBody(executiveBodyDto);
		return executiveBodyDto;
	}
	*/
}
