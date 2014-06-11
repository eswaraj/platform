package com.eswaraj.core.service.impl;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.eswaraj.core.convertors.CategoryConvertor;
import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.AppService;
import com.eswaraj.domain.nodes.Category;
import com.eswaraj.domain.repo.CategoryRepository;
import com.eswaraj.web.dto.CategoryDto;

@Component
@Transactional
public class AppServiceImpl implements AppService {

	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private CategoryConvertor categoryConvertor;
	
	@Override
	public CategoryDto saveCategory(CategoryDto categoryDto) throws ApplicationException {
		Category category = categoryConvertor.convert(categoryDto);
		category = categoryRepository.save(category);
		return categoryConvertor.convertBean(category);
	}

	@Override
	public CategoryDto getCategoryById(long categoryId) throws ApplicationException {
		Category category = categoryRepository.findOne(categoryId);
		return categoryConvertor.convertBean(category);
	}

	@Override
	public List<CategoryDto> getAllRootCategories() throws ApplicationException {
		Collection<Category> rootCategories = categoryRepository.getAllRootCategories();
		return categoryConvertor.convertBeanList(rootCategories);
	}

	@Override
	public List<CategoryDto> getAllChildCategoryOfParentCategory(long parentCategoryId) throws ApplicationException {
		Category parentCategory = categoryRepository.findOne(parentCategoryId);
		if(parentCategory == null){
			throw new ApplicationException("No such location exists [id="+parentCategoryId+"]");
		}
		Collection<Category> rootCategories = categoryRepository.findAllChildCategoryOfParentCategory(parentCategory);
		return categoryConvertor.convertBeanList(rootCategories);
	}

}
