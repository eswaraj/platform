package com.next.eswaraj.admin.service;

import java.util.List;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.domain.nodes.Category;
import com.eswaraj.domain.nodes.Comment;
import com.eswaraj.domain.nodes.Complaint;
import com.eswaraj.domain.nodes.Department;
import com.eswaraj.domain.nodes.User;
import com.eswaraj.domain.nodes.extended.ComplaintDepartmentSearchResult;

public interface AdminService {

    List<Department> getUserDepartments(Long userId) throws ApplicationException;

    List<Category> getAllcategories() throws ApplicationException;
    
    Comment saveComplaintComment(Complaint complaint, Department department, Long personId, String text) throws ApplicationException;

    List<ComplaintDepartmentSearchResult> getDepartmentComplaintsAll(Long departmentId) throws ApplicationException;

    User login(String userName, String password) throws ApplicationException;
}
