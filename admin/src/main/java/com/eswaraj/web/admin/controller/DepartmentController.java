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
import com.eswaraj.web.dto.DepartmentDto;

/**
 * @author ravi
 * @date Mar 01, 2014
 *
 */
@Controller
public class DepartmentController extends BaseController{

	@RequestMapping(value = "/ajax/department/getall/{categoryId}", method = RequestMethod.GET)
	@ResponseBody
	public List<DepartmentDto> getAllDepartmentOfCategory(ModelAndView mv, @PathVariable Long categoryId) throws ApplicationException {
		return appService.getAllDepartmentsOfCategory(categoryId);
	}
	
	@RequestMapping(value = "/ajax/department/save", method = RequestMethod.POST)
	public @ResponseBody DepartmentDto saveLocationTypes(ModelAndView mv, @RequestBody DepartmentDto departmentDto) throws ApplicationException {
		departmentDto = appService.saveDepartment(departmentDto);
		return departmentDto;
	}


}
