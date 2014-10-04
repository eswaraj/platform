package com.eswaraj.web.admin.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.eswaraj.core.exceptions.ApplicationException;

/**
 * Leader url will be like this /leader/state/{stateId}/{locationType}.html
 * 
 * @author Ravi
 *
 */
@Controller
public class LeaderController extends BaseController {

    @Autowired
    private ApiUtil apiUtil;

    @RequestMapping(value = "/leader/**/{locationType}.html", method = RequestMethod.GET)
    public ModelAndView showIndexPage(ModelAndView mv, HttpServletRequest httpServletRequest, @PathVariable String locationType) throws ApplicationException {
        addGenericValues(mv, httpServletRequest);
        System.out.println("Request URI : " + httpServletRequest.getRequestURI());
        System.out.println("locationType : " + locationType);
        String urlkey = httpServletRequest.getRequestURI().replace(".html", "");
        String urlkeyWithoutLocationType = urlkey.replace("/" + locationType, "");
        System.out.println("urlkey : " + urlkey);
        System.out.println("urlkeyWithoutLocationType : " + urlkeyWithoutLocationType);
        String pbInfo = apiUtil.getLeaderInfo(httpServletRequest, urlkey);
        System.out.println("pbInfo : " + pbInfo);
        mv.setViewName("leader");
        return mv;
    }
}
