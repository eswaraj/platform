package com.eswaraj.domain.repo;

import java.util.Collection;
import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.eswaraj.domain.nodes.Department;
import com.eswaraj.domain.nodes.DepartmentAdmin;
import com.eswaraj.domain.nodes.Location;
import com.eswaraj.domain.nodes.Person;
import com.eswaraj.domain.nodes.PoliticalBodyType;

public interface DepartmentAdminRepository extends GraphRepository<DepartmentAdmin>{

    @Query("start department=node({0}) match (department)<-[:WORKS_FOR]-(executiveBodyAdmins) return executiveBodyAdmins")
    public Collection<DepartmentAdmin> getAllAdminsOfDepartment(Department department);

    @Query("start location=node({0}),politicalBodyType=node({1})  match (location)<-[:BELONGS_TO]-(PoliticalAdmin)-[:OF_TYPE]->politicalBodyType return PoliticalAdmin")
    List<DepartmentAdmin> getAllExecutiveAdminByLocationAndPoliticalBodyType(Location location, PoliticalBodyType politicalBodyType);

    @Query("start department=node({0}), person=node({1}) match (department)-[:WORKS_FOR]-(departmentAdmin)-[:IS]-(person) where departmentAdmin.__type__='DepartmentAdmin' return departmentAdmin")
    DepartmentAdmin getDepartmentAdminByDepartmentAndPerson(Department department, Person person);

}
