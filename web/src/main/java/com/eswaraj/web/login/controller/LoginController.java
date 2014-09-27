package com.eswaraj.web.login.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.web.admin.controller.BaseController;

@Controller
public class LoginController extends BaseController {

    @RequestMapping(value = "/web/login", method = RequestMethod.GET)
    public ModelAndView login(ModelAndView mv, HttpServletRequest httpServletRequest) throws ApplicationException {
        String facebookUrl = "/web/login/facebook";
        if (httpServletRequest.getQueryString() != null) {
            facebookUrl = facebookUrl + "?" + httpServletRequest.getQueryString();
        }
        RedirectView rv = new RedirectView(facebookUrl);
        mv.setView(rv);
        return mv;
    }

    @RequestMapping(value = "/web/logout", method = RequestMethod.GET)
    public ModelAndView logout(ModelAndView mv, HttpServletRequest httpServletRequest) throws ApplicationException {
        httpServletRequest.getSession().invalidate();
        RedirectView rv = new RedirectView("/index.html");
        mv.setView(rv);
        return mv;
    }

}
