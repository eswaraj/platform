package com.eswaraj.web.dto;

import java.util.List;


/**
 * Category of a complaint 
 * @author ravi
 * @date Jan 18, 2014
 *
 */
public class CategoryWithChildCategoryDto extends CategoryDto{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private List<CategoryWithChildCategoryDto> childCategories;
	public List<CategoryWithChildCategoryDto> getChildCategories() {
		return childCategories;
	}
	public void setChildCategories(
			List<CategoryWithChildCategoryDto> childCategories) {
		this.childCategories = childCategories;
	}
    
	
}
