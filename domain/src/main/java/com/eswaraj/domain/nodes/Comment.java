package com.eswaraj.domain.nodes;

import java.util.Date;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.NodeEntity;

import com.eswaraj.domain.base.BaseNode;

/**
 * Location of the complaint
 * 
 * @author ravi
 * @date Oct 10, 2014
 *
 */
@NodeEntity
@TypeAlias("Comment")
public class Comment extends BaseNode {

    private String text;
    @Fetch
    private Person createdBy;
    private PoliticalBodyAdmin politicalBodyAdmin;
    private Department department;
    private Date creationTime;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Person getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Person createdBy) {
        this.createdBy = createdBy;
    }

    public PoliticalBodyAdmin getPoliticalBodyAdmin() {
        return politicalBodyAdmin;
    }

    public void setPoliticalBodyAdmin(PoliticalBodyAdmin politicalBodyAdmin) {
        this.politicalBodyAdmin = politicalBodyAdmin;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
	
}
