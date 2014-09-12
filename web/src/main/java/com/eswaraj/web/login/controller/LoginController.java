package com.eswaraj.web.login.controller;

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
    public ModelAndView getAllCategories(ModelAndView mv) throws ApplicationException {
        RedirectView rv = new RedirectView("/web/login/facebook");
        mv.setView(rv);
        return mv;
    }

}
