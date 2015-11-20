package com.eswaraj.web.dto;



/**
 * A executive department 
 * @author ravi
 * @date Jan 18, 2014
 *
 */
public class DepartmentDto extends BaseDto{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String description;
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
        return "DepartmentDto [name=" + name + ", description=" + description + ", id=" + id + "]";
	}

	
}
