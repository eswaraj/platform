package com.eswaraj.web.dto;




/**
 * Post of the administrator
 * @author ravi
 * @date Jun 25, 2014
 *
 */
public class ExecutivePostDto extends BaseDto{

	private static final long serialVersionUID = 1L;
	private String shortTitle;
	private String title;
	private String description;
	private Long departmentId;
	
	public String getShortTitle() {
		return shortTitle;
	}
	public void setShortTitle(String shortTitle) {
		this.shortTitle = shortTitle;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Long getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	

}
