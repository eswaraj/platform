package com.eswaraj.core.service;

import java.util.List;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.web.dto.CategoryDto;
import com.eswaraj.web.dto.PoliticalBodyTypeDto;

public interface AppService {

	//Category APIs
	CategoryDto saveCategory(CategoryDto categoryDto) throws ApplicationException;
	
	CategoryDto getCategoryById(long categoryId) throws ApplicationException;
	
	List<CategoryDto> getAllRootCategories() throws ApplicationException;
	
	List<CategoryDto> getAllChildCategoryOfParentCategory(long parentCategoryId) throws ApplicationException;

	//Political Body Type APIs
	PoliticalBodyTypeDto savePoliticalBodyType(PoliticalBodyTypeDto politicalBodyTypeDto) throws ApplicationException;
	
	PoliticalBodyTypeDto getPoliticalBodyTypeById(Long politicalBodyTypeId) throws ApplicationException;
	
	List<PoliticalBodyTypeDto> getAllPoliticalBodyTypes() throws ApplicationException;
}
