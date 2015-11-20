package com.eswaraj.domain.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eswaraj.domain.nodes.ElectionManifestoPromise;
import com.eswaraj.domain.validator.exception.ValidationException;

@Component
public class ElectionManifestoPromiseValidator extends BaseValidator<ElectionManifestoPromise> {

	@Autowired
	public ElectionManifestoPromiseValidator(ValidationManager validationManager) {
        super(ElectionManifestoPromise.class, validationManager);
	}

    @Override
    public void validateBeforeSave(ElectionManifestoPromise election) throws ValidationException {
        checkIfNull("Name", election.getTitle(), "Title can not be null or empty.");
        checkIfNull("Election Type", election.getElectionManifesto(), "Election Manifesto can not be null or empty.");
	}

    @Override
    public void validateBeforeDelete(ElectionManifestoPromise election) throws ValidationException {
	}
}
