package com.eswaraj.core.service;

import java.util.List;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.web.dto.CategoryDto;

public interface AppService {

	CategoryDto saveCategory(CategoryDto categoryDto) throws ApplicationException;
	
	CategoryDto getCategoryById(long categoryId) throws ApplicationException;
	
	List<CategoryDto> getAllRootCategories() throws ApplicationException;
	
	List<CategoryDto> getAllChildCategoryOfParentCategory(long parentCategoryId) throws ApplicationException;
	
}
