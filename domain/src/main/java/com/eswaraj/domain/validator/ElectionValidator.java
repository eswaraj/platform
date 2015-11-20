package com.eswaraj.domain.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eswaraj.domain.nodes.Election;
import com.eswaraj.domain.validator.exception.ValidationException;

@Component
public class ElectionValidator extends BaseValidator<Election> {

	@Autowired
	public ElectionValidator(ValidationManager validationManager) {
        super(Election.class, validationManager);
	}

    @Override
    public void validateBeforeSave(Election election) throws ValidationException {
        checkIfNull("Name", election.getName(), "Election name can not be null or empty.");
        checkIfNull("Election Type", election.getElectionType(), "Election type can not be null or empty.");
	}

    @Override
    public void validateBeforeDelete(Election election) throws ValidationException {
	}
}
