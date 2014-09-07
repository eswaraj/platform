package com.eswaraj.web.admin.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.AppService;
import com.eswaraj.core.service.LocationService;
import com.eswaraj.web.controller.beans.CategoryBean;
import com.eswaraj.web.controller.beans.ComplaintBean;
import com.eswaraj.web.controller.beans.LocationBean;
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

    public static void main(String[] args){
        String url = "http://dev.eswaraj.com/state/delhi/category/78270.html";
        url = url.replace(".html", "");
        String categoryId = null;
        if (url.contains("category")) {
            String urlPartForCatgeoryId = url.substring(url.indexOf("category") + 9);
            System.out.println("urlPartForCatgeoryId="+urlPartForCatgeoryId);
            Integer.parseInt(urlPartForCatgeoryId);
            
        }
    }
    @RequestMapping(value = "/state/**", method = RequestMethod.GET)
    public ModelAndView showLocationPage(ModelAndView mv, HttpServletRequest httpServletRequest) {
        System.out.println("Request URI : " + httpServletRequest.getRequestURI());
        String urlkey = httpServletRequest.getRequestURI().replace(".html", "");
        String categoryId = null;
        if (urlkey.contains("category")) {
            categoryId = urlkey.substring(urlkey.indexOf("category") + 9);
            urlkey = urlkey.replace("/category/" + categoryId, "");
            mv.getModel().put("selectedCategory", categoryId);
        }
        System.out.println("Looking up URL in redis : " + urlkey);
        String locationIdString = stringRedisTemplate.opsForValue().get(urlkey);
        if (locationIdString != null) {
            Long locationId = Long.parseLong(locationIdString);
            try {
                Gson gson = new Gson();
                String locationString = apiUtil.getLocation(httpServletRequest, locationId);
                mv.getModel().put("location", gson.fromJson(locationString, LocationBean.class));
                String allCategoriesString = apiUtil.getAllCategopries(httpServletRequest, locationId, false);
                List<CategoryBean> allRootcategories = gson.fromJson(allCategoriesString, new TypeToken<List<CategoryBean>>() {
                }.getType());
                mv.getModel().put("rootCategories", allRootcategories);
                String locationComplaints = null;
                if (categoryId == null) {
                    locationComplaints = apiUtil.getLocationComplaints(httpServletRequest, locationId);
                } else {
                    locationComplaints = apiUtil.getLocationCategoryComplaints(httpServletRequest, locationId, Long.parseLong(categoryId));
                }
                
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
