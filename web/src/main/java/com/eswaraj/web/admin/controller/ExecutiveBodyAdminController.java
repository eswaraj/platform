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
import com.eswaraj.web.dto.ExecutiveBodyAdminDto;

@Controller
public class ExecutiveBodyAdminController extends BaseController {

	@RequestMapping(value = "/ajax/eba/getroot/{executiveBodyId}", method = RequestMethod.GET)
	public @ResponseBody List<ExecutiveBodyAdminDto> getExecutiveBodyAdminOfExecutiveBody(ModelAndView mv, @PathVariable Long executiveBodyId) throws ApplicationException {
		List<ExecutiveBodyAdminDto> executiveBodyAdminDtos = appService.getAllExecutiveBodyAdminOfExecutiveBody(executiveBodyId);
		return executiveBodyAdminDtos;
	}
	
	@RequestMapping(value = "/ajax/eba/save", method = RequestMethod.POST)
	public @ResponseBody ExecutiveBodyAdminDto saveExecutiveBodyAdmin(ModelAndView mv, @RequestBody ExecutiveBodyAdminDto executiveBodyAdminDto) throws ApplicationException {
		executiveBodyAdminDto = appService.saveExecutiveBodyAdmin(executiveBodyAdminDto);
		return executiveBodyAdminDto;
	}
}
