package com.eswaraj.domain.repo;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.eswaraj.domain.nodes.Category;
import com.eswaraj.domain.nodes.Department;
import com.eswaraj.domain.nodes.relationships.DepartmentCategory;

public interface DepartmentCategoryRepository extends GraphRepository<DepartmentCategory> {
	
    @Query("start category=node({0}), department=node({1}) match (category)-[categoryDepartment:BELONGS]-(department) return categoryDepartment")
    DepartmentCategory getDepartmentCategoryRelation(Category category, Department department);

    @Query("start category=node({0}) match (category)-[categoryDepartment:BELONGS]-(department) return categoryDepartment")
    List<DepartmentCategory> getAllDepartmentCategoryRelationOfCategory(Category category);

    @Query("start department=node({0}) match (category)-[categoryDepartment:BELONGS]-(department) return categoryDepartment")
    List<DepartmentCategory> getAllDepartmentCategoryRelationOfDepartment(Department department);

    @Query("start category=node({0}) match (category)-[categoryDepartment:BELONGS]-(department) return department order by department.updateTime desc")
    List<Department> getAllDepartmentOfCategory(Category category);

    @Query("start category=node({0}) match (category)-[categoryDepartment:BELONGS]-(department) return department order by department.updateTime desc skip {1} limit {2}")
    List<Department> getPagedDepartmentOfCategory(Long categoryid, int start, int size);

    @Query("start department=node({0}) match (category)-[categoryDepartment:BELONGS]-(department) return category")
    List<Category> getAllCategoryOfDepartment(Department department);

}
