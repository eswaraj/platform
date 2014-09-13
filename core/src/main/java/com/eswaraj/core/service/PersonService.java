package com.eswaraj.core.service;

import java.util.List;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.web.dto.DeviceDto;
import com.eswaraj.web.dto.PersonDto;
import com.eswaraj.web.dto.RegisterFacebookAccountRequest;
import com.eswaraj.web.dto.RegisterFacebookAccountWebRequest;
import com.eswaraj.web.dto.UserDto;

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
	
    UserDto saveUser(UserDto userDto) throws ApplicationException;

    UserDto registerDevice(DeviceDto deviceDto, String userExternalId) throws ApplicationException;

    UserDto registerFacebookAccount(RegisterFacebookAccountRequest registerFacebookAccountRequest) throws ApplicationException;

    UserDto registerFacebookAccountWebUser(RegisterFacebookAccountWebRequest registerFacebookAccountWebRequest) throws ApplicationException;

}
