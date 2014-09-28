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
import com.eswaraj.web.dto.RegisterDeviceRequest;
import com.eswaraj.web.dto.RegisterFacebookAccountRequest;
import com.eswaraj.web.dto.RegisterFacebookAccountWebRequest;
import com.eswaraj.web.dto.UpdateUserRequestWebDto;
import com.eswaraj.web.dto.UserDto;

@Controller
public class UserController extends BaseController {

    @Autowired
    private PersonService personService;

    @RequestMapping(value = "/api/v0/user/facebook", method = RequestMethod.POST)
    public @ResponseBody UserDto registerFacebookUser(HttpServletRequest httpServletRequest, @RequestBody RegisterFacebookAccountRequest registerFacebookAccountRequest) throws ApplicationException {
        return personService.registerFacebookAccount(registerFacebookAccountRequest);
    }

    @RequestMapping(value = "/api/v0/user/device", method = RequestMethod.POST)
    public @ResponseBody UserDto registerFacebookUser(HttpServletRequest httpServletRequest, @RequestBody RegisterDeviceRequest registerDeviceRequest) throws ApplicationException {
        return personService.registerDevice(registerDeviceRequest, registerDeviceRequest.getUserExternalId());
    }

    @RequestMapping(value = "/api/v0/web/user/facebook", method = RequestMethod.POST)
    public @ResponseBody UserDto registerFacebookUserWeb(HttpServletRequest httpServletRequest, @RequestBody RegisterFacebookAccountWebRequest registerFacebookAccountWebRequest)
            throws ApplicationException {
        logger.info("Registering user : " + registerFacebookAccountWebRequest);
        UserDto userDto = personService.registerFacebookAccountWebUser(registerFacebookAccountWebRequest);
        return userDto;
    }

    @RequestMapping(value = "/api/v0/web/user/profile", method = RequestMethod.POST)
    public @ResponseBody UserDto updateUser(HttpServletRequest httpServletRequest, @RequestBody UpdateUserRequestWebDto updateUserRequestWebDto) throws ApplicationException {
        return personService.updateUserInfo(updateUserRequestWebDto);
    }

}
