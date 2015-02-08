package com.eswaraj.domain.nodes;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

import com.eswaraj.domain.base.BaseNode;


/**
 * A executive department 
 * @author ravi
 * @date Jan 18, 2014
 *
 */
@NodeEntity
@TypeAlias("Department")
public class Department extends BaseNode {

	
	private String name;
	private String description;
	@RelatedTo(type="UNDER")
    private Category category;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return "Department [Name=" + name + ", description=" + description + "]";
	}
}
