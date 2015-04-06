package com.next.eswaraj.admin.service;

import java.util.List;

import org.springframework.social.connect.ConnectionData;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.domain.nodes.Category;
import com.eswaraj.domain.nodes.Comment;
import com.eswaraj.domain.nodes.Complaint;
import com.eswaraj.domain.nodes.FacebookAccount;
import com.eswaraj.domain.nodes.LeaderTempFacebookAccount;
import com.eswaraj.domain.nodes.Person;
import com.eswaraj.domain.nodes.Photo;
import com.eswaraj.domain.nodes.PoliticalBodyAdmin;
import com.eswaraj.domain.nodes.PoliticalBodyAdminStaff;
import com.eswaraj.domain.nodes.User;
import com.eswaraj.domain.nodes.extended.ComplaintSearchResult;
import com.eswaraj.domain.nodes.extended.PoliticalBodyAdminStaffSearchResult;
import com.eswaraj.domain.nodes.relationships.ComplaintPoliticalAdmin;

public interface AdminService {

    List<Complaint> getPoliticalAdminComplaints(Long politicalAdminId) throws ApplicationException;

    List<ComplaintSearchResult> getPoliticalAdminComplaintsAll(Long politicalAdminId) throws ApplicationException;

    List<PoliticalBodyAdmin> getUserPoliticalBodyAdmins(Long userId) throws ApplicationException;

    List<Photo> getComplaintPhotos(Long complaintId) throws ApplicationException;

    List<Person> getComplaintCreators(Long complaintId) throws ApplicationException;

    List<Category> getAllcategories() throws ApplicationException;

    Complaint saveComplaint(Complaint complaint) throws ApplicationException;

    List<Comment> getComplaintComments(Long complaintId) throws ApplicationException;

    ComplaintPoliticalAdmin saveComplaintPoliticalAdmin(ComplaintPoliticalAdmin complaintPoliticalAdmin) throws ApplicationException;

    Comment saveComplaintComment(Complaint complaint, PoliticalBodyAdmin politicalBodyAdmin, Long personId, String text) throws ApplicationException;

    void markComplaintViewed(Long complaintId, Long politicalBodyAdminId) throws ApplicationException;
    
    List<PoliticalBodyAdminStaffSearchResult> getAdminStaffList(Long politicalBodyAdminId) throws ApplicationException;

    List<Person> searchPersonByName(String name) throws ApplicationException;

    PoliticalBodyAdminStaff savePoliticalBodyAdminStaff(PoliticalBodyAdmin politicalBodyAdmin, Person person, String post) throws ApplicationException;

    PoliticalBodyAdminStaff removePoliticalBodyAdminStaff(PoliticalBodyAdminStaff politicalBodyAdminStaff) throws ApplicationException;

    LeaderTempFacebookAccount getFacebookAccountRequestForPerson(Person person) throws ApplicationException;

    FacebookAccount getFacebookAccountByPerson(Person person) throws ApplicationException;

    void validateJoinRequest(String personId, String requestId, String emailId) throws ApplicationException;

    void linkLeaderToFacebookAccount(String personId, String requestId, String emailId, ConnectionData facebookConnectionData) throws ApplicationException;

    User login(String userName, String password) throws ApplicationException;

}
