package com.eswaraj.domain.nodes.relationships;

import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;

import com.eswaraj.domain.base.BaseRelationship;
import com.eswaraj.domain.nodes.PoliticalAdministrator;
import com.eswaraj.domain.nodes.PoliticalBody;
import com.eswaraj.domain.nodes.ExecutivePost;

/**
 * Administrators working in a political body
 * @author anuj
 * @data Jan 28, 2014
 */

@RelationshipEntity(type="MEMBER_OF")
public class PoliticalBodyAdministrator extends BaseRelationship {

	@StartNode PoliticalBody politicalBody;
	@EndNode PoliticalAdministrator politicalAdministrator;
	private ExecutivePost post;
	public PoliticalBodyAdministrator() {}
	
	public PoliticalBodyAdministrator(PoliticalBody executiveBody,PoliticalAdministrator politicalAdministrator, ExecutivePost post) {
		this.politicalBody = executiveBody;
		this.politicalAdministrator = politicalAdministrator;
		this.post = post;
	}

	public PoliticalBody getPoliticalBody() {
		return politicalBody;
	}

	public void setPoliticalBody(PoliticalBody politicalBody) {
		this.politicalBody = politicalBody;
	}

	public PoliticalAdministrator getPoliticalAdministrator() {
		return politicalAdministrator;
	}

	public void setPoliticalAdministrator(
			PoliticalAdministrator politicalAdministrator) {
		this.politicalAdministrator = politicalAdministrator;
	}

	public ExecutivePost getPost() {
		return post;
	}

	public void setPost(ExecutivePost post) {
		this.post = post;
	}
}
