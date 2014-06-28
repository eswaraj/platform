package com.eswaraj.core.service;

import java.util.List;

import com.eswaraj.web.dto.ComplaintDto;

/**
 * Complaint service for complaint related calls
 * @author anuj
 * @data Jun 22, 2014
 */

public interface ComplaintService {
	
	List<ComplaintDto> getPagedUserComplaints(String personId, int start, int end);
	
	ComplaintDto getUserComplaint(String complaintId);
}
