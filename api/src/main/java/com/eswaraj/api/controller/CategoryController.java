package com.eswaraj.api.controller;

import org.omg.CORBA.portable.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.eswaraj.core.service.AppKeyService;

@Controller
public class CategoryController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private AppKeyService appKeyService;

    @RequestMapping(value = "/api/v0/categories", method = RequestMethod.GET)
    public @ResponseBody String getAllCategories(ModelAndView mv) throws ApplicationException {
        String allCategories = stringRedisTemplate.opsForValue().get(appKeyService.getAllCategoriesKey());
        return allCategories;
    }

}
