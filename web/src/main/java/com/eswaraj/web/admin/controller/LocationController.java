package com.eswaraj.web.admin.controller;

import java.util.ArrayList;
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
                addTotalComplaintCountToModel(mv, allRootcategories);
                mv.getModel().put("rootCategories", allRootcategories);
                String locationComplaints = null;
                if (categoryId == null) {
                    locationComplaints = apiUtil.getLocationComplaints(httpServletRequest, locationId);
                } else {
                    locationComplaints = apiUtil.getLocationCategoryComplaints(httpServletRequest, locationId, Long.parseLong(categoryId));
                }
                
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

    private void addPaginationInfo(HttpServletRequest httpServletRequest, ModelAndView mv, long totalComplaint, String selectedCategory, List<CategoryBean> categories) {
        String pageNumberString = httpServletRequest.getParameter("page");
        if (pageNumberString == null) {
            pageNumberString = "1";
        }
        long currentPage = Long.parseLong(pageNumberString);
        long totalPages = 0;
        if (selectedCategory == null) {
            totalPages = totalComplaint / 10 + 1;
        } else {
            for (CategoryBean oneCategoryBean : categories) {
                if (oneCategoryBean.getId().toString().equals(selectedCategory)) {
                    totalPages = oneCategoryBean.getLocationCount() / 10 + 1;
                }
            }
        }

        long startPage = 1;
        long endPage = totalPages;
        if (totalPages > 5) {
            if(currentPage > 3){
                if(totalPages - currentPage >= 3){
                    startPage = currentPage - 3 + 1;
                    endPage = currentPage + 3 - 1;
                }
            }
        }
        List<Long> allPages = new ArrayList<>();
        for (long i = startPage; i <= endPage; i++) {
            allPages.add(i);
        }
        mv.getModel().put("pages", allPages);
        if (startPage == 1) {
            mv.getModel().put("enableFirst", false);
        } else {
            mv.getModel().put("enableFirst", true);
        }
        if (endPage == totalPages) {
            mv.getModel().put("enableLast", false);
        } else {
            mv.getModel().put("enableLast", true);
        }
        mv.getModel().put("totalPages", totalPages);
        mv.getModel().put("currentPage", currentPage);
    }

    private void addTotalComplaintCountToModel(ModelAndView mv, List<CategoryBean> allRootcategories) {
        if (allRootcategories == null) {
            mv.getModel().put("total", 0);
            return;
        }
        Long total = 0L;
        for (CategoryBean oneCategoryBean : allRootcategories) {
            total = oneCategoryBean.getLocationCount() + total;
        }
        mv.getModel().put("total", total);
    }

}
