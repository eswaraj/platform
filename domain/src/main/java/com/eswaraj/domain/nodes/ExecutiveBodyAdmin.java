package com.eswaraj.domain.nodes;

import java.util.Date;

import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

import com.eswaraj.domain.base.BaseNode;

/**
 * Administrators working in an executive body
 * @author ravi
 * @data Jun 19, 2014
 */

@NodeEntity
public class ExecutiveBodyAdmin extends BaseNode {

	@RelatedTo(type="REPORTS_TO")
	private ExecutiveBodyAdmin manager;
	
	@RelatedTo(type="WORKS_FOR")
	private ExecutiveBody executiveBody;

	@RelatedTo(type="IS")
	private Person person;

	@RelatedTo(type="HAS")
	private Post post;
	
	private Date startDate;
	private Date endDate;
	public ExecutiveBodyAdmin getManager() {
		return manager;
	}
	public void setManager(ExecutiveBodyAdmin manager) {
		this.manager = manager;
	}
	public ExecutiveBody getExecutiveBody() {
		return executiveBody;
	}
	public void setExecutiveBody(ExecutiveBody executiveBody) {
		this.executiveBody = executiveBody;
	}
	public Person getPerson() {
		return person;
	}
	public void setPerson(Person person) {
		this.person = person;
	}
	public Post getPost() {
		return post;
	}
	public void setPost(Post post) {
		this.post = post;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}


}
