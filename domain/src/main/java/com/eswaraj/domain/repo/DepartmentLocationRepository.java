package com.eswaraj.domain.repo;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.eswaraj.domain.nodes.Category;
import com.eswaraj.domain.nodes.Department;
import com.eswaraj.domain.nodes.Location;
import com.eswaraj.domain.nodes.relationships.DepartmentLocation;

public interface DepartmentLocationRepository extends GraphRepository<DepartmentLocation> {
	
    @Query("start location=node({0}), department=node({1}) match (location)-[locationDepartment:DEPT_LOCATION_REL]-(department) return locationDepartment")
    DepartmentLocation getDepartmentLocationRelation(Location location, Department department);

    @Query("start location=node({0}) match (location)-[locationDepartment:DEPT_LOCATION_REL]-(department) return locationDepartment")
    List<DepartmentLocation> getAllDepartmentLocationRelationOfLocation(Location location);

    @Query("start department=node({0}) match (location)-[locationDepartment:DEPT_LOCATION_REL]-(department) return locationDepartment")
    List<DepartmentLocation> getAllDepartmentLocationRelationOfDepartment(Department department);

    @Query("start location=node({0}) match (location)-[locationDepartment:DEPT_LOCATION_REL]-(department) return department order by department.updateTime desc")
    List<Department> getAllDepartmentOfLocation(Location location);

    @Query("start location=node({0}), category=node({1}) match (location)-[locationDepartment:DEPT_LOCATION_REL]-(department)-[:BELONGS]-(category) return department order by department.updateTime desc")
    List<Department> getAllDepartmentOfLocationAndCategory(Location location, Category category);

    @Query("start location=node({0}) match (location)-[locationDepartment:DEPT_LOCATION_REL]-(department) return department order by department.updateTime desc skip {1} limit {2}")
    List<Department> getPagedDepartmentOfLocation(Long locationid, int start, int size);

    @Query("start department=node({0}) match (location)-[locationDepartment:DEPT_LOCATION_REL]-(department) return location")
    List<Location> getAllLocationOfDepartment(Department department);

}
