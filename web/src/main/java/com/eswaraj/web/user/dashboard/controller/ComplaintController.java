package com.eswaraj.web.user.dashboard.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.ComplaintService;
import com.eswaraj.web.dto.ComplaintDto;

/**
 * 
 * @author anuj
 * @data Jun 22, 2014
 */

@Controller
public class ComplaintController {
	
	@Autowired
	private ComplaintService complaintService;

	@RequestMapping(value = "/user/complaints/{userId}", method = RequestMethod.GET)
	public @ResponseBody List<ComplaintDto> getUserComplaints(@RequestParam("start") int start, @RequestParam("end") int end, @PathVariable String userId) throws ApplicationException {
		return complaintService.getPagedUserComplaints(userId, start, end);
	}
}
