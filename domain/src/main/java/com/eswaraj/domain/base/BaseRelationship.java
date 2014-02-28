package com.eswaraj.domain.base;

import java.util.Date;

import org.springframework.data.neo4j.annotation.GraphId;

public class BaseRelationship {

	@GraphId
	protected Long id;
	protected Date dateCreated;
	protected Date dateModified;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	public Date getDateModified() {
		return dateModified;
	}
	public void setDateModified(Date dateModified) {
		this.dateModified = dateModified;
	}
	@Override
	public String toString() {
		return "BaseRelationship [id=" + id + ", dateCreated=" + dateCreated
				+ ", dateModified=" + dateModified + "]";
	}
}
