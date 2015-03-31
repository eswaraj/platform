package com.eswaraj.domain.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eswaraj.domain.nodes.DepartmentAdmin;
import com.eswaraj.domain.validator.exception.ValidationException;

@Component
public class ExecutiveBodyAdminValidator extends BaseValidator<DepartmentAdmin>{

	@Autowired
	public ExecutiveBodyAdminValidator(ValidationManager validationManager) {
		super(DepartmentAdmin.class, validationManager);
	}

	@Override
	public void validateBeforeSave(DepartmentAdmin executiveBodyAdmin) throws ValidationException {
        checkIfNull("ExecutiveBody", executiveBodyAdmin.getDepartment(), "DepartmentAdmin's Department can not be Null");
        checkIfNull("Person", executiveBodyAdmin.getPerson(), "DepartmentAdmin must be linked to a person");
		
	}

	@Override
	public void validateBeforeDelete(DepartmentAdmin executiveBodyAdmin) throws ValidationException {
		// TODO Auto-generated method stub
		
	}

	

}
