package com.next.eswaraj.admin.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.conversion.EndResult;
import org.springframework.stereotype.Service;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.domain.nodes.Category;
import com.eswaraj.domain.nodes.Comment;
import com.eswaraj.domain.nodes.Complaint;
import com.eswaraj.domain.nodes.Person;
import com.eswaraj.domain.nodes.Photo;
import com.eswaraj.domain.nodes.PoliticalAdminComplaintStatus;
import com.eswaraj.domain.nodes.PoliticalBodyAdmin;
import com.eswaraj.domain.nodes.extended.ComplaintSearchResult;
import com.eswaraj.domain.nodes.extended.PoliticalBodyAdminStaffSearchResult;
import com.eswaraj.domain.nodes.relationships.ComplaintComment;
import com.eswaraj.domain.nodes.relationships.ComplaintPoliticalAdmin;
import com.eswaraj.domain.repo.CategoryRepository;
import com.eswaraj.domain.repo.CommentRepository;
import com.eswaraj.domain.repo.ComplaintCommentRepository;
import com.eswaraj.domain.repo.ComplaintPoliticalAdminRepository;
import com.eswaraj.domain.repo.ComplaintRepository;
import com.eswaraj.domain.repo.PersonRepository;
import com.eswaraj.domain.repo.PhotoRepository;
import com.eswaraj.domain.repo.PoliticalBodyAdminRepository;
import com.eswaraj.domain.repo.PoliticalBodyAdminStaffRepository;

@Service
public class AdminServiceImpl implements AdminService {


    @Autowired
    private ComplaintRepository complaintRepository;
    @Autowired
    private PoliticalBodyAdminRepository politicalBodyAdminRepository;
    @Autowired
    private PhotoRepository photoRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ComplaintPoliticalAdminRepository complaintPoliticalAdminRepository;
    @Autowired
    private ComplaintCommentRepository complaintCommentRepository;
    @Autowired
    private PoliticalBodyAdminStaffRepository politicalBodyAdminStaffRepository;

    @Override
    public List<Complaint> getPoliticalAdminComplaints(Long politicalAdminId) throws ApplicationException {
        return null;
    }

    @Override
    public List<ComplaintSearchResult> getPoliticalAdminComplaintsAll(Long politicalAdminId) throws ApplicationException {
        return complaintRepository.searchAllPagedComplaintsOfPoliticalAdmin(politicalAdminId, 0, 1000);
    }

    private <T> List<T> convertToList(EndResult<T> dbResult) {
        List<T> returnList = new ArrayList<>();
        for (T oneT : dbResult) {
            returnList.add(oneT);
        }
        return returnList;
    }

    @Override
    public List<PoliticalBodyAdmin> getUserPoliticalBodyAdmins(Long userId) throws ApplicationException {
        return politicalBodyAdminRepository.getActivePoliticalAdminHistoryByUserId(userId);
    }

    @Override
    public List<Photo> getComplaintPhotos(Long complaintId) throws ApplicationException {
        return photoRepository.getComplaintPhotos(complaintId);
    }

    @Override
    public List<Person> getComplaintCreators(Long complaintId) throws ApplicationException {
        return personRepository.getPersonsLoggedComplaint(complaintId);
    }

    @Override
    public List<Category> getAllcategories() throws ApplicationException {
        return categoryRepository.getAllCategories();
    }

    @Override
    public Complaint saveComplaint(Complaint complaint) throws ApplicationException {
        return complaintRepository.save(complaint);
    }

    @Override
    public List<Comment> getComplaintComments(Long complaintId) throws ApplicationException {
        return commentRepository.findAllCommentsByComplaintId(complaintId);
    }

    @Override
    public ComplaintPoliticalAdmin saveComplaintPoliticalAdmin(ComplaintPoliticalAdmin complaintPoliticalAdmin) throws ApplicationException {
        return complaintPoliticalAdminRepository.save(complaintPoliticalAdmin);
    }

    @Override
    public Comment saveComplaintComment(Complaint complaint, PoliticalBodyAdmin politicalBodyAdmin, Long personId, String text) throws ApplicationException {
        Person createdBy = personRepository.findOne(personId);
        Comment comment = new Comment();
        comment.setCreatedBy(createdBy);
        comment.setCreationTime(new Date());
        comment.setPoliticalBodyAdmin(politicalBodyAdmin);
        comment.setText(text);
        comment = commentRepository.save(comment);
        System.out.println("Save Comment = " + comment);

        ComplaintComment complaintComment = new ComplaintComment();
        complaintComment.setComment(comment);
        complaintComment.setComplaint(complaint);
        complaintComment.setDateCreated(new Date());
        complaintComment.setDateModified(new Date());
        complaintComment = complaintCommentRepository.save(complaintComment);
        return comment;
    }

    @Override
    public void markComplaintViewed(Long complaintId, Long politicalBodyAdminId) throws ApplicationException {
        ComplaintPoliticalAdmin complaintPoliticalAdmin = complaintPoliticalAdminRepository.getComplaintPoliticalAdminRelation(complaintId, politicalBodyAdminId);
        if (!complaintPoliticalAdmin.isViewed()) {
            complaintPoliticalAdmin.setViewDate(new Date());
            complaintPoliticalAdmin.setViewed(true);
            complaintPoliticalAdmin.setStatus(PoliticalAdminComplaintStatus.Viewed);
            complaintPoliticalAdmin = complaintPoliticalAdminRepository.save(complaintPoliticalAdmin);
        }
    }

    @Override
    public List<PoliticalBodyAdminStaffSearchResult> getAdminStaffList(Long politicalBodyAdminId) throws ApplicationException {
        List<PoliticalBodyAdminStaffSearchResult> staff = politicalBodyAdminStaffRepository.searchPoliticalAdminStaffForAdmin(politicalBodyAdminId);
        return staff;
    }

    @Override
    public List<Person> searchPersonByName(String name) throws ApplicationException {
        return personRepository.searchPersonByName("name:*" + name + "*");
    }
}
