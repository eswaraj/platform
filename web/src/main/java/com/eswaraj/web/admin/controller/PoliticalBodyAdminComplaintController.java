package com.eswaraj.web.admin.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.web.dto.ComplaintStatusChangeByPoliticalAdminRequestDto;
import com.eswaraj.web.dto.ComplaintViewdByPoliticalAdminRequestDto;
import com.eswaraj.web.dto.PoliticalPositionDto;
import com.eswaraj.web.dto.UserDto;
import com.eswaraj.web.dto.comment.CommentSaveRequestDto;

@Controller
public class PoliticalBodyAdminComplaintController extends BaseController {

    @Autowired
    private ApiUtil apiUtil;

    @RequestMapping(value = "/complaints.html", method = RequestMethod.GET)
    public ModelAndView showLocationPage(ModelAndView mv, HttpServletRequest httpServletRequest) throws ApplicationException {
        UserDto user = sessionUtil.getLoggedInUserFromSession(httpServletRequest);
        List<PoliticalPositionDto> politicalPositionDtos = apiUtil.getPersonPoliticalPositions(httpServletRequest, user.getPerson().getId(), false);
        mv.getModel().put("positions", politicalPositionDtos);
        addGenericValues(mv, httpServletRequest);
        mv.setViewName("complaints");
        return mv;
    }

    @RequestMapping(value = "/ajax/complaint/leader/view", method = RequestMethod.POST)
    public @ResponseBody String complaintViewedByPoliticalAdmin(HttpServletRequest httpServletRequest, ModelAndView mv,
            @RequestBody ComplaintViewdByPoliticalAdminRequestDto complaintViewdByPoliticalAdminRequestDto)
            throws ApplicationException {
        UserDto loggedInUser = sessionUtil.getLoggedInUserFromSession(httpServletRequest);
        complaintViewdByPoliticalAdminRequestDto.setPersonId(loggedInUser.getPerson().getId());
        return apiUtil.updateComplaintViewStatus(httpServletRequest, complaintViewdByPoliticalAdminRequestDto);
    }

    @RequestMapping(value = "/ajax/complaint/leader/status", method = RequestMethod.POST)
    public @ResponseBody String getLeaderComplaints(HttpServletRequest httpServletRequest, ModelAndView mv,
            @RequestBody ComplaintStatusChangeByPoliticalAdminRequestDto complaintStatusChangeByPoliticalAdminRequestDto) throws ApplicationException {
        String complaints = apiUtil.updateComplaintStatus(httpServletRequest, complaintStatusChangeByPoliticalAdminRequestDto);
        return complaints;
    }

    @RequestMapping(value = "/ajax/complaint/leader/merge", method = RequestMethod.POST)
    public @ResponseBody String mergeComplaints(HttpServletRequest httpServletRequest, ModelAndView mv,
 @RequestBody List<Long> complaintIds) throws ApplicationException {
        String complaints = apiUtil.mergeComplaints(httpServletRequest, complaintIds);
        return complaints;
    }

    @RequestMapping(value = "/ajax/complaint/leader/comment", method = RequestMethod.POST)
    public @ResponseBody String commentOnComplaints(HttpServletRequest httpServletRequest, ModelAndView mv,
 @RequestBody CommentSaveRequestDto commentRequestDto) throws ApplicationException {
        String complaints = apiUtil.commentOn(httpServletRequest, commentRequestDto);
        return complaints;
    }

    @RequestMapping(value = "/ajax/complaint/leader/positions", method = RequestMethod.GET)
    public @ResponseBody String getLeaderPositions(HttpServletRequest httpServletRequest, ModelAndView mv) throws ApplicationException {
        UserDto user = sessionUtil.getLoggedInUserFromSession(httpServletRequest);
        String response = apiUtil.getPersonPoliticalPositionsString(httpServletRequest, user.getPerson().getId(), false);
        return response;
    }

}
