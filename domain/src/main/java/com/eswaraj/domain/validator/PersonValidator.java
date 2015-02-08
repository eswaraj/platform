package com.eswaraj.domain.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eswaraj.domain.nodes.Person;
import com.eswaraj.domain.util.ValidCharacters;
import com.eswaraj.domain.validator.exception.ValidationException;

@Component
public class PersonValidator extends BaseValidator<Person>{

	@Autowired
	public PersonValidator(ValidationManager validationManager) {
		super(Person.class, validationManager);
	}

	@Override
    public void validateBeforeSave(Person person) throws ValidationException {
		checkIfEmpty("Name", person.getName(),"Persons name can not be Null or Empty");
		//checkIfEmpty("Email", person.getEmail(),"Person's email cannot be empty or null");
		checkLength(person.getName(), "Person name should be of length between 2 and 64", 2, 64);
		checkAcceptedCharacters(person.getName(), "Person's name can only contain alphabets", ValidCharacters.NAME);
		if(person.getEmail() != null && !person.getEmail().trim().equals("")){
			checkAcceptedCharacters(person.getEmail(), "Invalid email", ValidCharacters.EMAIL);	
		}
		
	}

	@Override
    public void validateBeforeDelete(Person person) throws ValidationException {
		
	}
}