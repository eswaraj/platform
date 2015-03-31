package com.next.eswaraj.admin.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.domain.nodes.Category;
import com.eswaraj.domain.nodes.Comment;
import com.eswaraj.domain.nodes.Complaint;
import com.eswaraj.domain.nodes.Department;
import com.eswaraj.domain.nodes.Person;
import com.eswaraj.domain.nodes.extended.ComplaintDepartmentSearchResult;
import com.eswaraj.domain.nodes.relationships.ComplaintComment;
import com.eswaraj.domain.repo.CategoryRepository;
import com.eswaraj.domain.repo.CommentRepository;
import com.eswaraj.domain.repo.ComplaintCommentRepository;
import com.eswaraj.domain.repo.ComplaintRepository;
import com.eswaraj.domain.repo.DepartmentRepository;
import com.eswaraj.domain.repo.PersonRepository;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ComplaintCommentRepository complaintCommentRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private ComplaintRepository complaintRepository;

    @Override
    public List<Department> getUserDepartments(Long userId) throws ApplicationException {
        return departmentRepository.getUserDepartments(userId);
    }

    @Override
    public List<Category> getAllcategories() throws ApplicationException {
        return categoryRepository.getAllCategories();
    }

    @Override
    public Comment saveComplaintComment(Complaint complaint, Department department, Long personId, String text) throws ApplicationException {
        Person createdBy = personRepository.findOne(personId);
        Comment comment = new Comment();
        comment.setCreatedBy(createdBy);
        comment.setCreationTime(new Date());
        comment.setDepartment(department);
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
    public List<ComplaintDepartmentSearchResult> getDepartmentComplaintsAll(Long departmentId) throws ApplicationException {
        return complaintRepository.searchAllPagedComplaintsOfDepartment(departmentId, 0, 1000);
    }

}
