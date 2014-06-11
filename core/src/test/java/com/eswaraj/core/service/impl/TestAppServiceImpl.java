package com.eswaraj.core.service.impl;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.eswaraj.core.BaseNeo4jEswarajTest;
import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.AppService;
import com.eswaraj.domain.validator.exception.ValidationException;
import com.eswaraj.web.dto.CategoryDto;

@ContextConfiguration(locations = { "classpath:eswaraj-core-test.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class TestAppServiceImpl extends BaseNeo4jEswarajTest{

	@Autowired private AppService appService;

	/**
	 * Create a category and then get it by getcategory Service
	 * @throws ApplicationException
	 */
	@Test
	public void test01_saveCategory() throws ApplicationException{
		final String categoryName = randomAlphaString(16);
		final String categoryDescription = randomAlphaString(128);
		CategoryDto categoryDto = createCategory(categoryName, categoryDescription, true, null);
		CategoryDto savedCategory = appService.saveCategory(categoryDto);
		assertEqualCategories(categoryDto, savedCategory, false);
		
		CategoryDto dbCategory = appService.getCategoryById(savedCategory.getId());
		assertEqualCategories(categoryDto, dbCategory, false);
		assertEqualCategories(savedCategory, dbCategory, true);
		
	}
	
	/**
	 * Create a category with name as null, it should throw exception
	 * @throws ApplicationException
	 */
	@Test(expected=ValidationException.class)
	public void test02_saveCategory() throws ApplicationException{
		final String categoryName = null;
		final String categoryDescription = randomAlphaString(128);
		CategoryDto categoryDto = createCategory(categoryName, categoryDescription, true, null);
		appService.saveCategory(categoryDto);
		
	}
	
	/**
	 * Create a non root category and pass parent category as null
	 * it shud throw validation exception
	 * @throws ApplicationException
	 */
	@Test(expected=ValidationException.class)
	public void test03_saveCategory() throws ApplicationException{
		final String categoryName = null;
		final String categoryDescription = randomAlphaString(128);
		final boolean isRoot = false;
		final Long parentCategoryId = null;
		CategoryDto categoryDto = createCategory(categoryName, categoryDescription, isRoot, parentCategoryId);
		appService.saveCategory(categoryDto);
		
	}
	
	/**
	 * Create a root category and also try to create under it another category
	 * it shud throw validation exception
	 * @throws ApplicationException
	 */
	@Test(expected=ValidationException.class)
	public void test04_saveCategory() throws ApplicationException{
		CategoryDto parentRootCategoryDto = createCategory(randomAlphaString(16), randomAlphaString(128), true, null);
		parentRootCategoryDto = appService.saveCategory(parentRootCategoryDto);
		
		final String categoryName = null;
		final String categoryDescription = randomAlphaString(128);
		final boolean isRoot = true;
		final Long parentCategoryId = parentRootCategoryDto.getId();
		CategoryDto categoryDto = createCategory(categoryName, categoryDescription, isRoot, parentCategoryId);
		//This shud throw validation exception
		appService.saveCategory(categoryDto);
		
	}
	
	/**
	 * find all root categories from DB when no category exists
	 * @throws ApplicationException
	 */
	@Test
	public void test05_getRootCategories() throws ApplicationException{
		List<CategoryDto> allRootCategories = appService.getAllRootCategories();
		assertEquals(0, allRootCategories.size());
		
	}
	
	/**
	 * create 1 Root category and then find all root categories from DB
	 * it must return 1 category
	 * @throws ApplicationException
	 */
	@Test
	public void test06_getRootCategories() throws ApplicationException{
		final String categoryName = randomAlphaString(16);
		final String categoryDescription = randomAlphaString(128);
		CategoryDto categoryDto = createCategory(categoryName, categoryDescription, true, null);
		CategoryDto savedCategory = appService.saveCategory(categoryDto);
		List<CategoryDto> allRootCategories = appService.getAllRootCategories();
		assertEquals(1, allRootCategories.size());
		assertEqualCategories(savedCategory, allRootCategories.get(0), true);
	}
	
	/**
	 * create n Root category and then find all root categories from DB
	 * it must return n category
	 * @throws ApplicationException
	 */
	@Test
	public void test07_getRootCategories() throws ApplicationException{
		int totalRootNodes = randomInteger(100);
		for(int i=0;i<totalRootNodes;i++){
			final String categoryName = randomAlphaString(16);
			final String categoryDescription = randomAlphaString(128);
			CategoryDto categoryDto = createCategory(categoryName, categoryDescription, true, null);
			appService.saveCategory(categoryDto);
		}
		
		List<CategoryDto> allRootCategories = appService.getAllRootCategories();
		assertEquals(totalRootNodes , allRootCategories.size());
	}
	
	/**
	 * create n Root category and create mXn root categories then find all root categories from DB
	 * it must return n category
	 * also find child categories of each root node and we shud get m child under each
	 * @throws ApplicationException
	 */
	@Test
	public void test08_getRootCategories() throws ApplicationException{
		int totalRootNodes = randomInteger(100);
		int totalChildPerRootnode = 2;
		CategoryDto rootCategory;
		List<CategoryDto> allRootCategories = new ArrayList<>(totalRootNodes);
		for(int i=0;i<totalRootNodes;i++){
			final String categoryName = uniqueAlphaNumericString(16, "Category");
			final String categoryDescription = uniqueAlphaNumericString(128, "CategoryDescription");
			CategoryDto categoryDto = createCategory(categoryName, categoryDescription, true, null);
			rootCategory = appService.saveCategory(categoryDto);
			allRootCategories.add(rootCategory);
			//create child nodes
			for(int j=0;j<totalChildPerRootnode;j++){
				final String childCategoryName = uniqueAlphaNumericString(16, "Category");
				final String childCategoryDescription = uniqueAlphaNumericString(128, "CategoryDescription");
				CategoryDto childCategoryDto = createCategory(childCategoryName, childCategoryDescription, false, rootCategory.getId());
				appService.saveCategory(childCategoryDto);
				
			}
		}
		
		List<CategoryDto> allDbRootCategories = appService.getAllRootCategories();
		assertEquals(totalRootNodes , allDbRootCategories.size());
		
		for(CategoryDto oneRootCategory:allRootCategories){
			allDbRootCategories = appService.getAllChildCategoryOfParentCategory(oneRootCategory.getId());
			assertEquals(totalChildPerRootnode , allDbRootCategories.size());
		}
	}
	
}
