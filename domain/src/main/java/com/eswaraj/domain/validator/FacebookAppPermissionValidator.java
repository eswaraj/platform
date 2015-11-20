package com.eswaraj.domain.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eswaraj.domain.nodes.relationships.FacebookAppPermission;
import com.eswaraj.domain.validator.exception.ValidationException;

@Component
public class FacebookAppPermissionValidator extends BaseValidator<FacebookAppPermission> {

	@Autowired
	public FacebookAppPermissionValidator(ValidationManager validationManager) {
        super(FacebookAppPermission.class, validationManager);
	}

	@Override
    public void validateBeforeSave(FacebookAppPermission facebookAppPermission) throws ValidationException {
        checkIfNull("Facebook App", facebookAppPermission.getFacebookApp(), "Facebook App can not be Null");
        checkIfNull("Facebook Account", facebookAppPermission.getFacebookAccount(), "Facebook Account can not be null");
		
	}

	@Override
    public void validateBeforeDelete(FacebookAppPermission facebookAppPermission) throws ValidationException {
		// TODO Auto-generated method stub
		
	}

	

}
