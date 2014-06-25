package com.eswaraj.domain.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eswaraj.domain.nodes.ExecutiveBodyAdmin;
import com.eswaraj.domain.validator.exception.ValidationException;

@Component
public class ExecutiveBodyAdminValidator extends BaseValidator<ExecutiveBodyAdmin>{

	@Autowired
	public ExecutiveBodyAdminValidator(ValidationManager validationManager) {
		super(ExecutiveBodyAdmin.class, validationManager);
	}

	@Override
	public void validateBeforeSave(ExecutiveBodyAdmin executiveBodyAdmin) throws ValidationException {
		checkIfNull("ExecutiveBody", executiveBodyAdmin.getExecutiveBody(),"ExecutiveBodyAdmin's ExecutiveBody can not be Null");
		checkIfNull("Person", executiveBodyAdmin.getPerson(),"ExecutiveBodyAdmin must be linked to a person");
		
	}

	@Override
	public void validateBeforeDelete(ExecutiveBodyAdmin executiveBodyAdmin) throws ValidationException {
		// TODO Auto-generated method stub
		
	}

	

}
