package com.eswaraj.domain.nodes.relationships;

import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;

import com.eswaraj.domain.base.BaseRelationship;
import com.eswaraj.domain.nodes.Department;
import com.eswaraj.domain.nodes.Location;

@RelationshipEntity(type = "DEPT_LOCATION_REL")
public class DepartmentLocation extends BaseRelationship {

    @StartNode
    private Department department;
    @EndNode
    @Fetch
    private Location location;

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public DepartmentLocation() {
    }

    public DepartmentLocation(Department department, Location location) {
        this.department = department;
        this.location = location;
    }


	
}
