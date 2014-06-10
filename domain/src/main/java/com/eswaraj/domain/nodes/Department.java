package com.eswaraj.domain.nodes;


/**
 * A executive department 
 * @author ravi
 * @date Jan 18, 2014
 *
 */
public class Department {

	
	private String name;
	private String description;
	
	public Department(){}
	
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
	@Override
	public String toString() {
		return "Department [Name=" + name + ", description=" + description + "]";
	}
}
