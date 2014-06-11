package com.eswaraj.web.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.AppService;
import com.eswaraj.web.dto.CategoryDto;

/**
 * @author ravi
 * @date Mar 01, 2014
 *
 */
@Controller
public class CategoryController {

	@Autowired
	private AppService appService;
	
	@RequestMapping(value = "/ajax/categories/getroot", method = RequestMethod.GET)
	@ResponseBody
	public List<CategoryDto> getAllRootCategories(ModelAndView mv) throws ApplicationException {
		return appService.getAllRootCategories();
	}

	@RequestMapping(value = "/ajax/categories/getchild/{parentCategoryId}", method = RequestMethod.GET)
	@ResponseBody
	public List<CategoryDto> getAllChildCategoriesOfParentCategory(ModelAndView mv, @PathVariable Long parentCategoryId) throws ApplicationException {
		List<CategoryDto> catgeories = appService.getAllChildCategoryOfParentCategory(parentCategoryId);
		return catgeories;
	}
	
	@RequestMapping(value = "/ajax/categories/save", method = RequestMethod.POST)
	public @ResponseBody CategoryDto saveLocationTypes(ModelAndView mv, @RequestBody CategoryDto categoryDto) throws ApplicationException {
		categoryDto = appService.saveCategory(categoryDto);
		return categoryDto;
	}


}
