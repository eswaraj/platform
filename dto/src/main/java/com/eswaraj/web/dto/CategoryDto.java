package com.eswaraj.web.dto;


/**
 * Category of a complaint 
 * @author ravi
 * @date Jan 18, 2014
 *
 */
public class CategoryDto extends BaseDto{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String description;
    private Long parentCategoryId;
    private boolean root;
    
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
	public Long getParentCategoryId() {
		return parentCategoryId;
	}
	public void setParentCategoryId(Long parentCategoryId) {
		this.parentCategoryId = parentCategoryId;
	}
	public boolean isRoot() {
		return root;
	}
	public void setRoot(boolean root) {
		this.root = root;
	}
	@Override
	public String toString() {
		return "CategoryDto [name=" + name + ", description=" + description + ", parentCategoryId=" + parentCategoryId + ", root=" + root + ", id=" + id + "]";
	}
	    
    
	
}
