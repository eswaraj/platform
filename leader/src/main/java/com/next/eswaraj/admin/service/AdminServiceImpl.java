package com.next.eswaraj.admin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.domain.nodes.Complaint;
import com.eswaraj.domain.repo.ComplaintRepository;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private ComplaintRepository complaintRepository;

    @Override
    public List<Complaint> getPoliticalAdminComplaints(Long politicalAdminId) throws ApplicationException {
        return complaintRepository.getAllPagedComplaintsOfPoliticalAdmin(politicalAdminId, 0, 10);
    }

}
