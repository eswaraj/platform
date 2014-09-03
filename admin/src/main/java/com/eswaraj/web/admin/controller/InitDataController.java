package com.eswaraj.web.admin.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.AppService;
import com.eswaraj.core.service.LocationService;

@Controller
public class InitDataController extends BaseController {

    @Autowired
    private AppService appService;

    @Autowired
    private LocationService locationService;


    @RequestMapping(value = "/data/init", method = RequestMethod.GET)
    public @ResponseBody String handleFileUpload(HttpServletRequest httpServletRequest) throws IOException, ServletException, ApplicationException {
        appService.initializeData();
        locationService.initializeData();
        return "Data Loaded";
    }

    @RequestMapping(value = "/data/updateurl", method = RequestMethod.GET)
    public @ResponseBody String update(HttpServletRequest httpServletRequest) throws IOException, ServletException, ApplicationException {
        System.out.println("Updating Location Urls");
        try {
            locationService.updateAllLocationUrls();
            appService.updateAllUrls();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("Finished");
        return "Data updated";
    }

}
