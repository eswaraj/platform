package com.eswaraj.domain.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eswaraj.domain.nodes.ElectionType;
import com.eswaraj.domain.validator.exception.ValidationException;

@Component
public class ElectionTypeValidator extends BaseValidator<ElectionType> {

	@Autowired
	public ElectionTypeValidator(ValidationManager validationManager) {
        super(ElectionType.class, validationManager);
	}

    @Override
    public void validateBeforeSave(ElectionType electionType) throws ValidationException {
        checkIfNull("Name", electionType.getName(), "ElectionType name can not be null or empty.");
	}

    @Override
    public void validateBeforeDelete(ElectionType electionType) throws ValidationException {
	}
}
