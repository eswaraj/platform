package com.eswaraj.domain.nodes.relationships;

import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;

import com.eswaraj.domain.base.BaseRelationship;
import com.eswaraj.domain.nodes.Complaint;
import com.eswaraj.domain.nodes.Department;

@RelationshipEntity(type = "DEPT_COMPLAINT_REL")
public class DepartmentComplaint extends BaseRelationship {

    @StartNode
    private Department department;
    @EndNode
    private Complaint complaint;

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Complaint getComplaint() {
        return complaint;
    }

    public void setComplaint(Complaint complaint) {
        this.complaint = complaint;
    }

    public DepartmentComplaint() {
    }

    public DepartmentComplaint(Department department, Complaint complaint) {
        this.department = department;
        this.complaint = complaint;
    }


	
}
