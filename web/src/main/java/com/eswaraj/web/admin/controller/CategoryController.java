package com.eswaraj.web.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.AppService;
import com.eswaraj.queue.service.QueueService;
import com.eswaraj.web.dto.CategoryWithChildCategoryDto;

/**
 * @author ravi
 * @date Mar 01, 2014
 *
 */
@Controller
public class CategoryController extends BaseController{

	@Autowired
	private AppService appService;
	
    @Autowired
    private QueueService queueService;

    @Value("${aws_category_queue_name}")
    private String categoryQueue;

	@RequestMapping(value = "/mobile/categories", method = RequestMethod.GET)
	public @ResponseBody List<CategoryWithChildCategoryDto> getAllCategories(ModelAndView mv) throws ApplicationException {
		List<CategoryWithChildCategoryDto> allCategories = appService.getAllCategories();
		return allCategories;
	}

}
