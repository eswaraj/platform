package com.eswaraj.web.admin.controller;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.ComplaintService;
import com.eswaraj.core.service.FileService;
import com.eswaraj.web.dto.ComplaintDto;
import com.eswaraj.web.dto.PhotoDto;
import com.eswaraj.web.dto.SaveComplaintRequestDto;
import com.google.gson.Gson;

/**
 * 
 * @author anuj
 * @data Jun 22, 2014
 */

@Controller
public class ComplaintController extends BaseController{
	
	@Autowired
	private ComplaintService complaintService;	
	@Autowired
	private FileService fileService;
	@Value("${aws_s3_directory_for_complaint_photo}") 
	private String awsDirectoryForComplaintPhoto;

	@RequestMapping(value = "/user/complaints/{userId}", method = RequestMethod.GET)
	public @ResponseBody List<ComplaintDto> getUserComplaints(@PathVariable Long userId, @RequestParam(value= "start", required=false) Integer start, @RequestParam(value= "end", required=false) Integer end) throws ApplicationException {
		if(start == null){
			return complaintService.getAllUserComplaints(userId);	
		}else{
			return complaintService.getPagedUserComplaints(userId, start, end);	
		}
		
	}
	@RequestMapping(value = "/device/complaints/{userId}", method = RequestMethod.GET)
	public @ResponseBody List<ComplaintDto> getDeviceComplaints(@PathVariable String deviceId, @RequestParam(value= "start", required=false) Integer start, @RequestParam(value= "end", required=false) Integer end) throws ApplicationException {
		if(start == null){
			return complaintService.getAllDeviceComplaints(deviceId);	
		}else{
			return complaintService.getPagedDeviceComplaints(deviceId, start, end);	
		}
		
	}
	
	@RequestMapping(value = "/mobile/complaint", method = RequestMethod.POST)
	public @ResponseBody ComplaintDto saveComplaint(HttpServletRequest httpServletRequest) throws ApplicationException, IOException, ServletException {
		
		printInfo(httpServletRequest);
		Part saveComplaintRequestPart = httpServletRequest.getPart("SaveComplaintRequest");
		StringWriter writer = new StringWriter();
		IOUtils.copy(saveComplaintRequestPart.getInputStream(), writer);
		String saveComplaintRequestString = writer.toString();
		
		SaveComplaintRequestDto saveComplaintRequestDto = new Gson().fromJson(saveComplaintRequestString, SaveComplaintRequestDto.class);
		ComplaintDto savedComplaintDto = complaintService.saveComplaint(saveComplaintRequestDto);
		addPhoto(httpServletRequest, savedComplaintDto);
		
		return savedComplaintDto;
	}
	private void addPhoto(HttpServletRequest httpServletRequest, ComplaintDto complaintDto) throws IOException, ServletException, ApplicationException{
		String imageHttpUrl = "";
		Part uploadedImagePart = httpServletRequest.getPart("img");
		if(uploadedImagePart != null){
			String directory = awsDirectoryForComplaintPhoto+"/" + complaintDto.getId();
			String fileName = getFileName(uploadedImagePart.getSubmittedFileName());
			imageHttpUrl = fileService.saveFile(directory, fileName, uploadedImagePart.getInputStream());
			PhotoDto photoDto = new PhotoDto();
			photoDto.setOrgUrl(imageHttpUrl);
			complaintService.addPhotoToComplaint(complaintDto.getId(), photoDto);
		}
	}
	private void printInfo(HttpServletRequest httpServletRequest){
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
	}
	@RequestMapping(value = "/mobile/complaints", method = RequestMethod.POST)
	public @ResponseBody String saveComplaintsasa(HttpServletRequest httpServletRequest) throws ApplicationException {
		
		printInfo(httpServletRequest);
		return "Done";
	}
	/*
	@RequestMapping(value = "/user/complaints/{userId}", method = RequestMethod.GET)
	public @ResponseBody List<ComplaintDto> getUserComplaints(@PathVariable Long userId) throws ApplicationException {
		
	}
	*/
}
