package com.eswaraj.web.admin.controller;

import java.util.Collection;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
	public @ResponseBody List<ComplaintDto> getUserComplaints(@PathVariable Long userId, @RequestParam(value= "start", required=false) Integer start, @RequestParam(value= "end", required=false) Integer end) throws ApplicationException {
		if(start == null){
			return complaintService.getAllUserComplaints(userId);	
		}else{
			return complaintService.getPagedUserComplaints(userId, start, end);	
		}
		
	}
	
	@RequestMapping(value = "/mobile/complaint", method = RequestMethod.POST)
	public @ResponseBody ComplaintDto saveComplaint(@RequestBody ComplaintDto complaintDto) throws ApplicationException {
		return complaintService.saveComplaint(complaintDto);
	}
	
	@RequestMapping(value = "/mobile/complaints", method = RequestMethod.POST)
	public @ResponseBody String saveComplaint(HttpServletRequest httpServletRequest) throws ApplicationException {
		Enumeration<String> params = httpServletRequest.getParameterNames();
		System.out.println("*** ALL PARAMETERS*****");
		while(params.hasMoreElements()){
			System.out.println(params.nextElement());
		}
		{
			Enumeration<String> headers = httpServletRequest.getHeaderNames();
			System.out.println("*** ALL HEADERS*****");
			while(headers.hasMoreElements()){
				System.out.println(headers.nextElement());
			}
			
		}
		try{
			Collection<Part> parts = httpServletRequest.getParts();
			System.out.println("*** ALL Parts*****");
			for(Part onePart : parts){
				System.out.println(onePart.getName() +" , "+onePart.getSize() +" , "+ onePart.getContentType() +" , "+onePart.getSubmittedFileName() +" , "+onePart.getInputStream());
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}

		return "Done";
	}
	/*
	@RequestMapping(value = "/user/complaints/{userId}", method = RequestMethod.GET)
	public @ResponseBody List<ComplaintDto> getUserComplaints(@PathVariable Long userId) throws ApplicationException {
		
	}
	*/
}
