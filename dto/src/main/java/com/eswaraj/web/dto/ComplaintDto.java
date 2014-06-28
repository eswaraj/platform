package com.eswaraj.web.dto;



/**
 * DTO for a complaint that has been made by a user.
 * @author anuj
 * @data Jun 22, 2014
 */

public class ComplaintDto extends BaseDto {

	private static final long serialVersionUID = 1L;
	
	private String title;
	private String description;
	private Long categoryId;
	private Long personId;
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
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	public Long getPersonId() {
		return personId;
	}
	public void setPersonId(Long personId) {
		this.personId = personId;
	}
	
}
