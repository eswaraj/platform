package com.eswaraj.domain.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eswaraj.domain.nodes.ExecutivePost;
import com.eswaraj.domain.validator.exception.ValidationException;

@Component
public class ExecutivePostValidator extends BaseValidator<ExecutivePost>{

	@Autowired
	public ExecutivePostValidator(ValidationManager validationManager) {
		super(ExecutivePost.class, validationManager);
	}

	@Override
	public void validateBeforeSave(ExecutivePost executivePost) throws ValidationException {
		System.out.println("ExecutivePostValidator.validateBeforeSave");
		checkIfEmpty("Title", executivePost.getTitle(),"ExecutivePost Title can not be Null or Empty");
		checkIfNull("Department", executivePost.getDepartment(),"ExecutivePost Department can not be Null");
	}

	@Override
	public void validateBeforeDelete(ExecutivePost category) throws ValidationException {
	}

	

}
