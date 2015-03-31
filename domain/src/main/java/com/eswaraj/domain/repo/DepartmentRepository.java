package com.eswaraj.domain.repo;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.eswaraj.domain.nodes.Category;
import com.eswaraj.domain.nodes.Department;

public interface DepartmentRepository extends GraphRepository<Department>{

    @Query("start category=node({0}) match (category)<-[:BELONGS]-(departments) where departments.root = true return departments")
    public List<Department> getAllRootDepartmentsOfCategory(Category category);

    @Query("start category=node({0}) match (category)<-[:BELONGS]-(departments) where departments.root = true return departments")
    public List<Department> getAllRootDepartmentsOfCategory(Long categoryId);

    @Query("start department=node({0}) match (department)<-[:UNDER]-(childDepartments) return childDepartments")
    public List<Department> getAllChildDepartments(Department department);

    @Query("start department=node({0}) match (department)<-[:UNDER]-(childDepartments) return childDepartments")
    public List<Department> getAllChildDepartments(Long departmentId);

    @Query("start user=node({0}) match (user)-[:ATTACHED_TO]-(person)-[:IS]--(departmentAdmin)-[:WORKS_FOR]-(department) where person.__type__ = 'Person'  and departmentAdmin.__type__='DepartmentAdmin' return department")
    public List<Department> getUserDepartments(Long userId);

}
