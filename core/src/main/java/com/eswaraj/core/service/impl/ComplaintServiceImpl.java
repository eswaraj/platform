package com.eswaraj.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.eswaraj.core.service.ComplaintService;
import com.eswaraj.domain.repo.ComplaintRepository;
import com.eswaraj.web.dto.ComplaintDto;

/**
 * Implementation for complaint service
 * @author anuj
 * @data Jun 22, 2014
 */

@Component
@Transactional
public class ComplaintServiceImpl implements ComplaintService {
	
	@Autowired
	private ComplaintRepository complaintRepository;

	@Override
	public List<ComplaintDto> getPagedUserComplaints(String personId, int start, int end) {
		return null;
	}

	@Override
	public ComplaintDto getUserComplaint(String complaintId) {
		return null;
	}
}
