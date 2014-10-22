package com.eswaraj.web.admin.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eswaraj.core.exceptions.ApplicationException;

/**
 * @author ravi
 * @date Mar 01, 2014
 *
 */
@Controller
public class CategoryController extends BaseController{

    @Autowired
    private ApiUtil apiUtil;

    @RequestMapping(value = "/ajax/categories", method = RequestMethod.GET)
    public @ResponseBody String getAllCategories(HttpServletRequest httpServletRequest) throws ApplicationException {
        String categories = apiUtil.getAllCategopries(httpServletRequest);
        return categories;
	}

}
