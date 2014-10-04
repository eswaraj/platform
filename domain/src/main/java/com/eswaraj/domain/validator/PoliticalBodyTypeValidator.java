package com.eswaraj.domain.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eswaraj.domain.nodes.PoliticalBodyType;
import com.eswaraj.domain.validator.exception.ValidationException;

@Component
public class PoliticalBodyTypeValidator extends BaseValidator<PoliticalBodyType>{

	@Autowired
	public PoliticalBodyTypeValidator(ValidationManager validationManager) {
		super(PoliticalBodyType.class, validationManager);
	}

	@Override
    public void validateBeforeSave(PoliticalBodyType politicalBodyType) throws ValidationException {
		checkIfEmpty("Name", politicalBodyType.getName(), "Political Body Type name can not be null or empty");
		checkIfEmpty("ShortName", politicalBodyType.getShortName(), "Political Body Type ShortName can not be null or empty");
        if (politicalBodyType.getShortName().contains(" ")) {
            throw new ValidationException("A political Body Type shortname can not contain space");
        }
		if(politicalBodyType.getLocationType() == null){
			throw new ValidationException("A political Body Type must be associated with a LocationType");
		}
		
	}

	@Override
    public void validateBeforeDelete(PoliticalBodyType politicalBodyType) throws ValidationException {
	}
}
