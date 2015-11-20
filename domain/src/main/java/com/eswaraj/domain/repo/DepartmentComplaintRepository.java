package com.eswaraj.domain.repo;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.eswaraj.domain.nodes.Complaint;
import com.eswaraj.domain.nodes.Department;
import com.eswaraj.domain.nodes.relationships.DepartmentComplaint;

public interface DepartmentComplaintRepository extends GraphRepository<DepartmentComplaint> {
	
    @Query("start complaint=node({0}), department=node({1}) match (complaint)-[departmentComplaint:DEPT_COMPLAINT_REL]-(department) return departmentComplaint")
    DepartmentComplaint getDepartmentComplaintRelation(Complaint complaint, Department department);

    @Query("start complaint=node({0}) match (complaint)-[departmentComplaint:DEPT_COMPLAINT_REL]-(department) return departmentComplaint")
    List<DepartmentComplaint> getAllDepartmentComplaintRelationOfComplaint(Complaint complaint);

    @Query("start department=node({0}) match (complaint)-[departmentComplaint:DEPT_COMPLAINT_REL]-(department) return departmentComplaint")
    List<DepartmentComplaint> getAllDepartmentComplaintRelationOfDepartment(Department department);

    @Query("start complaint=node({0}) match (complaint)-[departmentComplaint:DEPT_COMPLAINT_REL]-(department) return department order by department.dateCreated desc")
    List<Department> getAllDepartmentOfComplaint(Complaint complaint);

    @Query("start department=node({0}) match (complaint)-[departmentComplaint:DEPT_COMPLAINT_REL]-(department) return complaint")
    List<Complaint> getAllComplaintOfDepartment(Department department);

    @Query("start department=node({0}) match (complaint)-[departmentComplaint:DEPT_COMPLAINT_REL]-(department) return complaint order by complaint.dateCreated desc  skip {1} limit {2}")
    List<Complaint> getPagedComplaintOfDepartment(Department department, int start, int size);

}
