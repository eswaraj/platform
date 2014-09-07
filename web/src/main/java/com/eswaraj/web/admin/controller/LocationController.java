package com.eswaraj.web.admin.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.AppService;
import com.eswaraj.core.service.LocationService;
import com.eswaraj.web.controller.beans.ComplaintBean;
import com.eswaraj.web.controller.beans.LocationBean;
import com.eswaraj.web.dto.CategoryWithChildCategoryDto;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

@Controller
public class LocationController extends BaseController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private LocationService locationService;
    @Autowired
    private AppService appService;
    @Autowired
    private ApiUtil apiUtil;

    @RequestMapping(value = "/const.html", method = RequestMethod.GET)
    public ModelAndView showIndexPage(ModelAndView mv) {
        addGenericValues(mv);
        mv.setViewName("constituency");
        return mv;
    }

    @RequestMapping(value = "/state/**", method = RequestMethod.GET)
    public ModelAndView showLocationPage(ModelAndView mv, HttpServletRequest httpServletRequest) {
        System.out.println("Request URI : " + httpServletRequest.getRequestURI());
        String urlkey = httpServletRequest.getRequestURI().replace(".html", "");
        System.out.println("Looking up URL in redis : " + urlkey);
        String locationIdString = stringRedisTemplate.opsForValue().get(urlkey);
        if (locationIdString != null) {
            Long locationId = Long.parseLong(locationIdString);
            try {
                Gson gson = new Gson();
                String locationString = apiUtil.getLocation(httpServletRequest, locationId);
                mv.getModel().put("location", locationString);
                String allCategoriesString = apiUtil.getAllCategopries(httpServletRequest);
                List<CategoryWithChildCategoryDto> allRootcategories = gson.fromJson(allCategoriesString, new TypeToken<List<CategoryWithChildCategoryDto>>() {
                }.getType());
                mv.getModel().put("rootCategories", allRootcategories);
                String locationComplaints = apiUtil.getLocationComplaints(httpServletRequest, locationId);
                System.out.println("locationComplaints=" + locationComplaints);
                List<ComplaintBean> list = gson.fromJson(locationComplaints, new TypeToken<List<ComplaintBean>>() {
                }.getType());
                mv.getModel().put("complaintList", list);
            } catch (ApplicationException e) {
                e.printStackTrace();
            }
        }
        addGenericValues(mv);
        mv.setViewName("constituency");
        return mv;
    }

    @RequestMapping(value = "/state/**/category/{category}.html", method = RequestMethod.GET)
    public ModelAndView showLocationCategoryPage(ModelAndView mv, HttpServletRequest httpServletRequest, @PathVariable Long categoryId) {
        System.out.println("Request URI : " + httpServletRequest.getRequestURI());
        String urlkey = httpServletRequest.getRequestURI().replace("/category/" + categoryId + ".html", "");
        System.out.println("Looking up URL in redis : " + urlkey);
        System.out.println("categoryId : " + categoryId);
        String locationIdString = stringRedisTemplate.opsForValue().get(urlkey);
        if (locationIdString != null) {
            Long locationId = Long.parseLong(locationIdString);
            try {
                mv.getModel().put("selectedCategory", categoryId);
                Gson gson = new Gson();
                String locationString = apiUtil.getLocation(httpServletRequest, locationId);
                mv.getModel().put("location", gson.fromJson(locationString, LocationBean.class));
                String allCategoriesString = apiUtil.getAllCategopries(httpServletRequest);
                List<CategoryWithChildCategoryDto> allRootcategories = gson.fromJson(allCategoriesString, new TypeToken<List<CategoryWithChildCategoryDto>>() {
                }.getType());
                mv.getModel().put("rootCategories", allRootcategories);
                String locationComplaints = apiUtil.getLocationCategoryComplaints(httpServletRequest, locationId, categoryId);
                System.out.println("locationComplaints=" + locationComplaints);
                List<ComplaintBean> list = gson.fromJson(locationComplaints, new TypeToken<List<ComplaintBean>>() {
                }.getType());
                mv.getModel().put("complaintList", list);
            } catch (ApplicationException e) {
                e.printStackTrace();
            }
        }
        addGenericValues(mv);
        mv.setViewName("constituency");
        return mv;
    }
}
