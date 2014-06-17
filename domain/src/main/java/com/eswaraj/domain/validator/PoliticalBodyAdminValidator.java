package com.eswaraj.domain.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eswaraj.domain.nodes.PoliticalBodyAdmin;
import com.eswaraj.domain.validator.exception.ValidationException;

@Component
public class PoliticalBodyAdminValidator extends BaseValidator<PoliticalBodyAdmin>{

	@Autowired
	public PoliticalBodyAdminValidator(ValidationManager validationManager) {
		super(PoliticalBodyAdmin.class, validationManager);
	}

	public void validateBeforeSave(PoliticalBodyAdmin politicalBodyAdmin) throws ValidationException {
		checkIfNull("Location", politicalBodyAdmin.getLocation(), "Can not create a political Admin without a Location attached to it");
		checkIfNull("Party", politicalBodyAdmin.getParty(), "Can not create a political Admin without a Party attached to it");
		checkIfNull("PoliticalBodyType", politicalBodyAdmin.getPoliticalBodyType(), "Can not create a political Admin without a PoliticalBodyType attached to it");
		checkIfNull("StartDate", politicalBodyAdmin.getStartDate(), "You must provide a start Date for political Admin");
		if(politicalBodyAdmin.getEndDate() != null){
			if(politicalBodyAdmin.getEndDate().before(politicalBodyAdmin.getStartDate())){
				throw new ValidationException("End Date can not be before Start Date");
			}
		}
	}

	public void validateBeforeDelete(PoliticalBodyAdmin politicalBodyAdmin) throws ValidationException {
		
	}
}
