package com.eswaraj.api.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.AppService;
import com.eswaraj.core.service.PersonService;
import com.eswaraj.queue.service.QueueService;
import com.eswaraj.web.dto.PersonDto;
import com.eswaraj.web.dto.PoliticalPositionDto;
import com.eswaraj.web.dto.RegisterDeviceRequest;
import com.eswaraj.web.dto.RegisterFacebookAccountRequest;
import com.eswaraj.web.dto.RegisterFacebookAccountWebRequest;
import com.eswaraj.web.dto.UpdateMobileUserRequestDto;
import com.eswaraj.web.dto.UpdateUserRequestWebDto;
import com.eswaraj.web.dto.UserDto;
import com.eswaraj.web.dto.device.RegisterGcmDeviceId;

@Controller
public class UserController extends BaseController {

    @Autowired
    private PersonService personService;
    @Autowired
    private AppService appService;
    @Autowired
    private QueueService queueService;

    @RequestMapping(value = "/api/v0/web/user/profile", method = RequestMethod.POST)
    public @ResponseBody UserDto updateUser(HttpServletRequest httpServletRequest, @RequestBody UpdateUserRequestWebDto updateUserRequestWebDto) throws ApplicationException {
        UserDto userDto = personService.updateUserInfo(updateUserRequestWebDto);
        queueService.sendRefreshPerson(userDto.getPerson().getId(), "web");
        return userDto;
    }

    @RequestMapping(value = "/api/v0/mobile/user/profile", method = RequestMethod.POST)
    public @ResponseBody UserDto updateMobileUser(HttpServletRequest httpServletRequest, @RequestBody UpdateMobileUserRequestDto updateMobileRequestDto) throws ApplicationException {
        UserDto userDto = personService.updateMobileUserInfo(updateMobileRequestDto);
        queueService.sendRefreshPerson(userDto.getPerson().getId(), "mobile");
        return userDto;
    }

    @RequestMapping(value = "/api/v0/mobile/user/profile/{facebookToken}", method = RequestMethod.GET)
    public @ResponseBody UserDto getUser(HttpServletRequest httpServletRequest, @PathVariable String facebookToken) throws ApplicationException {
        UserDto userDto = personService.getUserByFacebookToken(facebookToken);
        return userDto;
    }

    @RequestMapping(value = "/api/v0/user/facebook", method = RequestMethod.POST)
    public @ResponseBody UserDto registerFacebookUser(HttpServletRequest httpServletRequest, @RequestBody RegisterFacebookAccountRequest registerFacebookAccountRequest) throws ApplicationException {
        UserDto userDto = personService.registerFacebookAccount(registerFacebookAccountRequest);
        queueService.sendRefreshPerson(userDto.getPerson().getId(), "web");
        return userDto;
    }

    @RequestMapping(value = "/api/v0/user/device", method = RequestMethod.POST)
    @Deprecated
    public @ResponseBody UserDto registerDevice(HttpServletRequest httpServletRequest, @RequestBody RegisterDeviceRequest registerDeviceRequest) throws ApplicationException {
        return personService.registerDevice(registerDeviceRequest, registerDeviceRequest.getUserExternalId());
    }

    @RequestMapping(value = "/api/v0/user/device/gcm", method = RequestMethod.POST)
    public @ResponseBody String registerdAndroidDeviceGcmId(HttpServletRequest httpServletRequest, @RequestBody RegisterGcmDeviceId registerGcmDeviceId) throws ApplicationException {
        personService.registerAndroidDeviceGcmId(registerGcmDeviceId);
        return "{'status':'success'}";
    }

    @RequestMapping(value = "/api/v0/web/user/facebook", method = RequestMethod.POST)
    public @ResponseBody UserDto registerFacebookUserWeb(HttpServletRequest httpServletRequest, @RequestBody RegisterFacebookAccountWebRequest registerFacebookAccountWebRequest)
            throws ApplicationException {
        logger.info("Registering user : " + registerFacebookAccountWebRequest);
        UserDto userDto = personService.registerFacebookAccountWebUser(registerFacebookAccountWebRequest);
        queueService.sendRefreshPerson(userDto.getPerson().getId(), "web");
        return userDto;
    }

    @RequestMapping(value = "/api/v0/person/politicalpositions/{personId}", method = RequestMethod.GET)
    public @ResponseBody List<PoliticalPositionDto> getPersonPoliticalPositions(HttpServletRequest httpServletRequest, ModelAndView mv, @PathVariable Long personId)
            throws ApplicationException {
        boolean activeOnly = getBooleanParameter(httpServletRequest, "active_only", false);

        List<PoliticalPositionDto> politicalPositionDtos = appService.getAllPoliticalPositionsOfPerson(personId, activeOnly);
        return politicalPositionDtos;
    }

    @RequestMapping(value = "/api/v0/person/search/email", method = RequestMethod.GET)
    @ResponseBody
    public List<PersonDto> searchPersonByEmailRequestParam(ModelAndView mv, @RequestParam("term") String term) throws ApplicationException {
        return personService.searchPersonWithEmail(term);
    }

    @RequestMapping(value = "/api/v0/person/search/name", method = RequestMethod.GET)
    @ResponseBody
    public List<PersonDto> searchPersonByNameRequestParam(ModelAndView mv, @RequestParam("term") String term) throws ApplicationException {
        return personService.searchPersonWithName(term);
    }

}
