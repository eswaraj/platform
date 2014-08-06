package com.eswaraj.web.admin.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.AppService;
import com.eswaraj.core.service.FileService;
import com.eswaraj.core.service.LocationKeyService;
import com.eswaraj.core.service.LocationService;

@Controller
public class InitDataController extends BaseController {

    @Autowired
    private LocationService locationService;

    @Autowired
    private FileService fileService;

    @Autowired
    private AppService appService;

    @Autowired
    private LocationKeyService LocationKeyService;
    @Autowired
    private RedisTemplate redisTemplate;


    @RequestMapping(value = "/data/init", method = RequestMethod.GET)
    public @ResponseBody String handleFileUpload(HttpServletRequest httpServletRequest) throws IOException, ServletException, ApplicationException {
        appService.initializeData();
        return "Data Loaded";
    }

}
