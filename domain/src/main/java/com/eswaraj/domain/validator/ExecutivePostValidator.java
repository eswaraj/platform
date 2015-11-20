package com.eswaraj.domain.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eswaraj.domain.nodes.DepartmentPost;
import com.eswaraj.domain.validator.exception.ValidationException;

@Component
public class ExecutivePostValidator extends BaseValidator<DepartmentPost>{

	@Autowired
	public ExecutivePostValidator(ValidationManager validationManager) {
		super(DepartmentPost.class, validationManager);
	}

	@Override
	public void validateBeforeSave(DepartmentPost executivePost) throws ValidationException {
		checkIfEmpty("Title", executivePost.getTitle(),"ExecutivePost Title can not be Null or Empty");
		checkIfNull("Department", executivePost.getDepartment(),"ExecutivePost Department can not be Null");
	}

	@Override
	public void validateBeforeDelete(DepartmentPost category) throws ValidationException {
	}

	

}
