package com.eswaraj.domain.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eswaraj.domain.nodes.ExecutiveBody;
import com.eswaraj.domain.validator.exception.ValidationException;

@Component
public class ExecutiveBodyValidator extends BaseValidator<ExecutiveBody>{

	@Autowired
	public ExecutiveBodyValidator(ValidationManager validationManager) {
		super(ExecutiveBody.class, validationManager);
	}

	public void validateBeforeSave(ExecutiveBody executiveBody) throws ValidationException {
		if(executiveBody.isRoot() && executiveBody.getParentExecutiveBody() != null){
			throw new ValidationException("You can not have a parent Executive Body for a ROOT executive body");
		}
		if(!executiveBody.isRoot() && executiveBody.getParentExecutiveBody() == null){
			throw new ValidationException("You must have a parent Executive Body for a NON-ROOT executive body");
		}
		if(executiveBody.getCategory() == null){
			throw new ValidationException("You must have a category attached to Executive Body");
		}
	}

	public void validateBeforeDelete(ExecutiveBody executiveBody) throws ValidationException {
		
	}
}
