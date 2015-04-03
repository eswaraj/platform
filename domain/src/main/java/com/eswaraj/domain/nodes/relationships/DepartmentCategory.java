package com.eswaraj.domain.nodes.relationships;

import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;

import com.eswaraj.domain.base.BaseRelationship;
import com.eswaraj.domain.nodes.Category;
import com.eswaraj.domain.nodes.Department;

@RelationshipEntity(type = "BELONGS")
public class DepartmentCategory extends BaseRelationship {

    @StartNode
    private Department department;
    @EndNode
    @Fetch
    private Category category;

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public DepartmentCategory() {
    }

    public DepartmentCategory(Department department, Category category) {
        this.department = department;
        this.category = category;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }


	
}
