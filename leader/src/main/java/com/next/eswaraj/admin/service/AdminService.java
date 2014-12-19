package com.next.eswaraj.admin.service;

import java.util.List;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.domain.nodes.Category;
import com.eswaraj.domain.nodes.Comment;
import com.eswaraj.domain.nodes.Complaint;
import com.eswaraj.domain.nodes.Person;
import com.eswaraj.domain.nodes.Photo;
import com.eswaraj.domain.nodes.PoliticalBodyAdmin;
import com.eswaraj.domain.nodes.extended.ComplaintSearchResult;

public interface AdminService {

    List<Complaint> getPoliticalAdminComplaints(Long politicalAdminId) throws ApplicationException;

    List<ComplaintSearchResult> getPoliticalAdminComplaintsAll(Long politicalAdminId) throws ApplicationException;

    List<PoliticalBodyAdmin> getUserPoliticalBodyAdmins(Long userId) throws ApplicationException;

    List<Photo> getComplaintPhotos(Long complaintId) throws ApplicationException;

    List<Person> getComplaintCreators(Long complaintId) throws ApplicationException;

    List<Category> getAllcategories() throws ApplicationException;

    Complaint saveComplaint(Complaint complaint) throws ApplicationException;

    List<Comment> getComplaintComments(Long complaintId) throws ApplicationException;
}
