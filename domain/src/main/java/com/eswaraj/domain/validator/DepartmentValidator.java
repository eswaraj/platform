package com.eswaraj.domain.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eswaraj.domain.nodes.Department;
import com.eswaraj.domain.validator.exception.ValidationException;

@Component
public class DepartmentValidator extends BaseValidator<Department>{

	@Autowired
	public DepartmentValidator(ValidationManager validationManager) {
		super(Department.class, validationManager);
	}

	@Override
	public void validateBeforeSave(Department department) throws ValidationException {
		checkIfEmpty("Name", department.getName(),"Department Name can not be Null or Empty");
		checkIfNull("Category", department.getCategory(),"Department Category can not be Null or Empty");
        checkIfNull("Address", department.getAddress(), "Address can not be Null or Empty");
        checkIfNull("Date Created", department.getDateCreated(), "Date Created can not be Null or Empty");
        checkIfNull("Date Modified", department.getDateModified(), "Date Modified can not be Null or Empty");
        if (department.isRoot()) {
            if (department.getParentDepartment() != null) {
                throw new ValidationException("Root Department can not have parent department");
            }
        } else {
            if (department.getParentDepartment() == null) {
                throw new ValidationException("Non Root Department must have a Parent Department");
            }
        }
	}

	@Override
	public void validateBeforeDelete(Department category) throws ValidationException {
		// TODO Auto-generated method stub
		
	}

	

}
