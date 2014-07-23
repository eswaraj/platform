package com.eswaraj.core.service;

import java.util.List;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.web.dto.ComplaintDto;
import com.eswaraj.web.dto.PhotoDto;
import com.eswaraj.web.dto.SaveComplaintRequestDto;

/**
 * Complaint service for complaint related calls
 * @author anuj
 * @data Jun 22, 2014
 */

public interface ComplaintService {
	
	List<ComplaintDto> getPagedUserComplaints(Long personId, int start, int end) throws ApplicationException;
	
	List<ComplaintDto> getAllUserComplaints(Long personId) throws ApplicationException;
	
	List<ComplaintDto> getPagedDeviceComplaints(String deviceId, int start, int end) throws ApplicationException;
	
	List<ComplaintDto> getAllDeviceComplaints(String deviceId) throws ApplicationException;

	ComplaintDto getComplaintById(Long complaintId) throws ApplicationException;
	
	ComplaintDto saveComplaint(SaveComplaintRequestDto complaintDto) throws ApplicationException;
	
	PhotoDto addPhotoToComplaint(Long complaintId, PhotoDto photoDto) throws ApplicationException;
}
