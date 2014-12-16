package com.next.eswaraj.admin.service;

import java.util.List;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.domain.nodes.Complaint;
import com.eswaraj.domain.nodes.PoliticalBodyAdmin;
import com.eswaraj.domain.nodes.extended.ComplaintSearchResult;

public interface AdminService {

    List<Complaint> getPoliticalAdminComplaints(Long politicalAdminId) throws ApplicationException;

    List<ComplaintSearchResult> getPoliticalAdminComplaintsAll(Long politicalAdminId) throws ApplicationException;

    List<PoliticalBodyAdmin> getUserPoliticalBodyAdmins(Long userId) throws ApplicationException;
}
