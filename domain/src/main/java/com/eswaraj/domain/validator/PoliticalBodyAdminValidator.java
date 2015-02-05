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

	@Override
    public void validateBeforeSave(PoliticalBodyAdmin politicalBodyAdmin) throws ValidationException {
		checkIfNull("Location", politicalBodyAdmin.getLocation(), "Can not create a political Admin without a Location attached to it");
		checkIfNull("Party", politicalBodyAdmin.getParty(), "Can not create a political Admin without a Party attached to it");
		checkIfNull("PoliticalBodyType", politicalBodyAdmin.getPoliticalBodyType(), "Can not create a political Admin without a PoliticalBodyType attached to it");
        checkIfNull("Person", politicalBodyAdmin.getPerson(), "Can not create a political Admin without a Person attached to it");
        checkIfNull("Election", politicalBodyAdmin.getElection(), "Can not create a political Admin without a Election attached to it");
		checkIfNull("StartDate", politicalBodyAdmin.getStartDate(), "You must provide a start Date for political Admin");
        checkIfNull("UrlIdentifier", politicalBodyAdmin.getUrlIdentifier(), "Url identifier can not be null");
        checkIfNull("Election", politicalBodyAdmin.getParty(), "Can not create a political Admin without an Election attached to it");
		if(politicalBodyAdmin.getEndDate() != null){
			if(politicalBodyAdmin.getEndDate().before(politicalBodyAdmin.getStartDate())){
				throw new ValidationException("End Date can not be before Start Date");
			}
		}
		/*
		if(politicalBodyAdmin.isActive()){
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			calendar.set(Calendar.MILLISECOND, 999);
			Date today = calendar.getTime();
			if(politicalBodyAdmin.getStartDate().after(today)){
				throw new ValidationException("End Date can not be before Start Date");
			}
		}
		*/
	}

	@Override
    public void validateBeforeDelete(PoliticalBodyAdmin politicalBodyAdmin) throws ValidationException {
		
	}
}
