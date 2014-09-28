package com.eswaraj.api.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.PersonService;
import com.eswaraj.web.dto.UpdateUserRequestWebDto;
import com.eswaraj.web.dto.UserDto;

@Controller
public class UserProfileController extends BaseController {

    @Autowired
    private PersonService personService;

    @RequestMapping(value = "/api/v0/user/profile", method = RequestMethod.POST)
    public @ResponseBody UserDto updateUser(HttpServletRequest httpServletRequest, @RequestBody UpdateUserRequestWebDto updateUserRequestWebDto) throws ApplicationException {
        return personService.updateUserInfo(updateUserRequestWebDto);
    }
}
