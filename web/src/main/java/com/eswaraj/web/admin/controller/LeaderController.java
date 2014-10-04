package com.eswaraj.web.admin.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Leader url will be like this /leader/state/{stateId}/{locationType}.html
 * 
 * @author Ravi
 *
 */
@Controller
public class LeaderController extends BaseController {

    @RequestMapping(value = "/leader/**/{locationType}.html", method = RequestMethod.GET)
    public ModelAndView showIndexPage(ModelAndView mv, HttpServletRequest httpServletRequest, @PathVariable String locationType) {
        addGenericValues(mv, httpServletRequest);
        System.out.println("Request URI : " + httpServletRequest.getRequestURI());
        System.out.println("locationType : " + locationType);
        String urlkey = httpServletRequest.getRequestURI().replace(".html", "");
        urlkey = urlkey.replace("/" + locationType, "");
        System.out.println("urlkey : " + urlkey);

        mv.setViewName("leader");
        return mv;
    }
}
