package com.eswaraj.web.admin.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.web.controller.beans.CategoryBean;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

@Controller
public class CitizenServiceController extends BaseController {

    @Autowired
    private ApiUtil apiUtil;

    Gson gson = new Gson();

    @RequestMapping(value = "/citizenservices.html", method = RequestMethod.GET)
    public ModelAndView showIndexPage(ModelAndView mv, HttpServletRequest httpServletRequest) throws ApplicationException {
        addGenericValues(mv, httpServletRequest);

        String allCategoriesString = apiUtil.getAllCategopries(httpServletRequest, null, true);
        List<CategoryBean> allRootcategories = gson.fromJson(allCategoriesString, new TypeToken<List<CategoryBean>>() {
        }.getType());
        Long totalComplaints = addTotalComplaintCountToModel(mv, allRootcategories);
        mv.getModel().put("rootCategories", allRootcategories);

        mv.setViewName("home");
        return mv;
    }
}
