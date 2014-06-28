package com.eswaraj.core.service;

import java.util.List;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.web.dto.ComplaintDto;

/**
 * Complaint service for complaint related calls
 * @author anuj
 * @data Jun 22, 2014
 */

public interface ComplaintService {
	
	List<ComplaintDto> getPagedUserComplaints(Long personId, int start, int end) throws ApplicationException;
	
	List<ComplaintDto> getAllUserComplaints(Long personId) throws ApplicationException;
	
	ComplaintDto getComplaintById(Long complaintId) throws ApplicationException;
	
	ComplaintDto saveComplaint(ComplaintDto complaintDto) throws ApplicationException;
}
