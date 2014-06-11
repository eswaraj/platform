package com.eswaraj.domain.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eswaraj.domain.nodes.Category;
import com.eswaraj.domain.validator.exception.ValidationException;

@Component
public class CategoryValidator extends BaseValidator<Category>{

	@Autowired
	public CategoryValidator(ValidationManager validationManager) {
		super(Category.class, validationManager);
	}

	public void validateBeforeSave(Category category) throws ValidationException {
		checkIfNull("Name", category.getName(), "Category name can not be null or empty.");
		if(category.isRoot() && category.getParentCategory() != null){
			throw new ValidationException("You can not assign a parent to a root category");
		}
		if(!category.isRoot() && category.getParentCategory() == null){
			throw new ValidationException("For non root category you must assign a parent category");
		}
	}

	public void validateBeforeDelete(Category category) throws ValidationException {
	}
}
