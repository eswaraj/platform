package com.eswaraj.domain.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eswaraj.domain.nodes.Setting;
import com.eswaraj.domain.validator.exception.ValidationException;

@Component
public class SettingValidator extends BaseValidator<Setting> {

	@Autowired
	public SettingValidator(ValidationManager validationManager) {
        super(Setting.class, validationManager);
	}

    @Override
    public void validateBeforeSave(Setting setting) throws ValidationException {
        checkIfEmpty("Name", setting.getName(), "Setting name can't be empty");
        checkIfEmpty("Value", setting.getValue(), "Setting Value can't be empty");
	}

    @Override
    public void validateBeforeDelete(Setting setting) throws ValidationException {
		
	}
}
