package com.eswaraj.web.admin.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.web.dto.PoliticalPositionDto;
import com.eswaraj.web.dto.SavePoliticalAdminStaffRequestDto;
import com.eswaraj.web.dto.UserDto;

@Controller
public class PoliticalBodyAdminStaffController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private ApiUtil apiUtil;

    @RequestMapping(value = "/staff.html", method = RequestMethod.GET)
    public ModelAndView showLocationPage(ModelAndView mv, HttpServletRequest httpServletRequest) throws ApplicationException {
        UserDto user = sessionUtil.getLoggedInUserFromSession(httpServletRequest);
        List<PoliticalPositionDto> politicalPositionDtos = apiUtil.getPersonPoliticalPositions(httpServletRequest, user.getPerson().getId(), false);
        mv.getModel().put("positions", politicalPositionDtos);
        addGenericValues(mv, httpServletRequest);
        mv.setViewName("politicaladminstaff");
        return mv;
    }
    @RequestMapping(value = "/ajax/leader/staff", method = RequestMethod.POST)
    public @ResponseBody String saveLeaderStaff(HttpServletRequest httpServletRequest, ModelAndView mv,
            @RequestBody SavePoliticalAdminStaffRequestDto savePoliticalAdminStaffRequestDto) throws ApplicationException {
        return apiUtil.savePoliticalAdminStaff(httpServletRequest, savePoliticalAdminStaffRequestDto);
    }

    @RequestMapping(value = "/ajax/leader/staff/{politicalAdminId}", method = RequestMethod.GET)
    public @ResponseBody String getLeaderStaff(HttpServletRequest httpServletRequest, ModelAndView mv, @PathVariable Long politicalAdminId) throws ApplicationException {
        return apiUtil.getPoliticalAdminStaff(httpServletRequest, politicalAdminId);
    }

    @RequestMapping(value = "/ajax/leader/staff/{politicalAdminStaffId}", method = RequestMethod.DELETE)
    public @ResponseBody String deleteLeaderStaff(HttpServletRequest httpServletRequest, ModelAndView mv, @PathVariable Long politicalAdminStaffId)
            throws ApplicationException {
        return apiUtil.deletePoliticalAdminStaff(httpServletRequest, politicalAdminStaffId);
    }

}
