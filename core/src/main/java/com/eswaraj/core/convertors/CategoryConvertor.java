package com.eswaraj.core.convertors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.domain.nodes.Category;
import com.eswaraj.domain.repo.CategoryRepository;
import com.eswaraj.web.dto.CategoryDto;
import com.eswaraj.web.dto.CategoryWithChildCategoryDto;

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
		convertBeanInternal(dbDto, categoryDto);
		return categoryDto;
	}
	/**
	 * This function is created as DB category gets converted to 2 types of web category
	 * @param dbDto
	 * @param categoryDto
	 */
	private void convertBeanInternal(Category dbDto, CategoryDto categoryDto) {
		BeanUtils.copyProperties(dbDto, categoryDto);
		if(dbDto.getParentCategory() != null){
			categoryDto.setParentCategoryId(dbDto.getParentCategory().getId());
		}
	}
	protected CategoryWithChildCategoryDto convertBeanWithChild(Category dbDto) {
		CategoryWithChildCategoryDto categoryWithChildCategoryDto = new CategoryWithChildCategoryDto();
		convertBeanInternal(dbDto, categoryWithChildCategoryDto);
		return categoryWithChildCategoryDto;
	}
	
	public List<CategoryWithChildCategoryDto> convertCategiryWithChildren(Collection<Category> categories){
		List<CategoryWithChildCategoryDto> list = new ArrayList<>();
		List<CategoryWithChildCategoryDto> tempList = new ArrayList<>();
		Map<Long, List<CategoryWithChildCategoryDto>> childCategoryMap = new HashMap<>();
		CategoryWithChildCategoryDto oneCategoryWithChildCategoryDto;
		List<CategoryWithChildCategoryDto> childCategoryList;
		List<CategoryWithChildCategoryDto> parentCategoryList;
		
		for(Category oneCategory : categories){
			oneCategoryWithChildCategoryDto = convertBeanWithChild(oneCategory);
			tempList.add(oneCategoryWithChildCategoryDto);
			if(oneCategoryWithChildCategoryDto.isRoot()){
				//If its root add to main list too
				list.add(oneCategoryWithChildCategoryDto);
			}else{
				//add it to its parent's child list
				parentCategoryList = childCategoryMap.get(oneCategoryWithChildCategoryDto.getParentCategoryId());
				if(parentCategoryList == null){
					parentCategoryList = new ArrayList<>();
					childCategoryMap.put(oneCategoryWithChildCategoryDto.getParentCategoryId(), parentCategoryList);
				}
				parentCategoryList.add(oneCategoryWithChildCategoryDto);
			}
		}
		
		//Now set all Childrens and create the full tree
		for(CategoryWithChildCategoryDto oneCategoryWithChildCategory : tempList){
			childCategoryList = childCategoryMap.get(oneCategoryWithChildCategory.getId());
			oneCategoryWithChildCategory.setChildCategories(childCategoryList);
		}
		return list;
	}

}
