package com.next.eswaraj.admin.service;

import java.util.List;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.domain.nodes.Complaint;

public interface AdminService {

    List<Complaint> getPoliticalAdminComplaints(Long politicalAdminId) throws ApplicationException;
}
