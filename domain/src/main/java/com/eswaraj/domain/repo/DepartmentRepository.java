package com.eswaraj.domain.repo;

import java.util.Collection;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.eswaraj.domain.nodes.Category;
import com.eswaraj.domain.nodes.Department;

public interface DepartmentRepository extends GraphRepository<Department>{

	@Query("start category=node({0}) match (category)<-[:UNDER]-(departments) return departments")
	public Collection<Department> getAllDepartmentsOfCategory(Category category);

}
