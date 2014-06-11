package com.eswaraj.core.convertors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.domain.nodes.Category;
import com.eswaraj.domain.repo.CategoryRepository;
import com.eswaraj.web.dto.CategoryDto;

@Component
public class CategoryConvertor extends BaseConvertor<Category, CategoryDto> {

	@Autowired
	private CategoryRepository categoryRepository;
	

	@Override
	protected Category convertInternal(CategoryDto categoryDto) throws ApplicationException {
		Category category = getObjectIfExists(categoryDto, "Category", categoryRepository) ;
		if(category == null){
			category = new Category();
		}
		BeanUtils.copyProperties(categoryDto, category);
		if(categoryDto.getParentCategoryId() != null){
			Category parentCategory = categoryRepository.findOne(categoryDto.getParentCategoryId());
			category.setParentCategory(parentCategory);
		}
		return category;
	}

	@Override
	protected CategoryDto convertBeanInternal(Category dbDto) {
		CategoryDto categoryDto = new CategoryDto();
		BeanUtils.copyProperties(dbDto, categoryDto);
		if(dbDto.getParentCategory() != null){
			categoryDto.setParentCategoryId(dbDto.getParentCategory().getId());
		}
		return categoryDto;
	}

}
