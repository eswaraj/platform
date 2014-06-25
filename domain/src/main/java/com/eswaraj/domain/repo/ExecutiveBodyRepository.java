package com.eswaraj.domain.repo;

import java.util.Collection;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.eswaraj.domain.nodes.Department;
import com.eswaraj.domain.nodes.ExecutiveBody;

/**
 * Repo for executivebody queries
 * @author anuj
 * @data Jan 28, 2014
 */
public interface ExecutiveBodyRepository extends GraphRepository<ExecutiveBody>{
	
	/*
	@Query("start executiveBody=node({0})" +
			"match (executiveBody)<-[:WORKS_FOR]-(executiveAdministrator) return executiveAdministrator")
	public Set<ExecutiveAdministrator> findAdministrators(ExecutiveBody executiveBody);
	*/
	@Query("start executiveBody=node({0}) match (executiveBody)<-[:CHILD_OF]-(childExecutiveBodies) return childExecutiveBodies")
	public Collection<ExecutiveBody> getChildExecutiveBodiesByParent(ExecutiveBody executiveBody);
	
	@Query("start category=node({0}) match (category)<-[:BELONGS_TO]-(executiveBodies) return executiveBodies")
	public Collection<ExecutiveBody> getAllRootExecutiveBodyOfDepartment(Department department);

	
}
