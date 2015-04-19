package com.next.eswaraj.web.login.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.eswaraj.core.exceptions.ApplicationException;

@RestController
public class IndexController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = { "/", "/index.html" }, method = RequestMethod.GET)
    public ModelAndView login(ModelAndView mv, HttpServletRequest httpServletRequest) throws ApplicationException {
        RedirectView rv = new RedirectView("/admin/complaints.xhtml");
        logger.info("Redirecting to /admin/complaints.xhtml ");
        mv.setView(rv);
        return mv;
    }

}