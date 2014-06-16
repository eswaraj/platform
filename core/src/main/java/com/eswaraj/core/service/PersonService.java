package com.eswaraj.core.service;

import java.util.List;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.web.dto.PersonDto;

public interface PersonService {

	/**
	 * This will create or update the person in database
	 * @param personDto
	 * @return
	 * @throws ApplicationException
	 */
	PersonDto savePerson(PersonDto personDto) throws ApplicationException;
	
	PersonDto getPersonById(Long personId) throws ApplicationException;
	
	List<PersonDto> searchPersonStartWithName(String name) throws ApplicationException;
	
	List<PersonDto> searchPersonWithName(String name) throws ApplicationException;
	
}
