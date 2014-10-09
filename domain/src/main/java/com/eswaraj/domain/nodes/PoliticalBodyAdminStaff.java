package com.eswaraj.domain.nodes;

import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

import com.eswaraj.domain.base.BaseNode;

/**
 * A political elected representative
 * 
 * @author ravi
 * @date Oct 09, 2014
 *
 */
@NodeEntity
public class PoliticalBodyAdminStaff extends BaseNode {
	
    @RelatedTo(type = "WORKING_FOR")
    private PoliticalBodyAdmin politicalBodyAdmin;

	@RelatedTo(type="IS")
	private Person person;

    private String post;

    public PoliticalBodyAdmin getPoliticalBodyAdmin() {
        return politicalBodyAdmin;
    }

    public void setPoliticalBodyAdmin(PoliticalBodyAdmin politicalBodyAdmin) {
        this.politicalBodyAdmin = politicalBodyAdmin;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }
	
}
