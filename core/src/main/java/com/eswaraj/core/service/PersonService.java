package com.eswaraj.core.service;

import java.util.List;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.domain.nodes.extended.PoliticalBodyAdminSearchResult;
import com.eswaraj.web.dto.DeviceDto;
import com.eswaraj.web.dto.PersonDto;
import com.eswaraj.web.dto.RegisterFacebookAccountRequest;
import com.eswaraj.web.dto.RegisterFacebookAccountWebRequest;
import com.eswaraj.web.dto.UpdateMobileUserRequestDto;
import com.eswaraj.web.dto.UpdateUserRequestWebDto;
import com.eswaraj.web.dto.UserDto;
import com.eswaraj.web.dto.device.RegisterGcmDeviceId;

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
	
    List<PersonDto> searchPersonWithEmail(String email) throws ApplicationException;

    UserDto saveUser(UserDto userDto) throws ApplicationException;

    UserDto registerDevice(DeviceDto deviceDto, String userExternalId) throws ApplicationException;

    void registerAndroidDeviceGcmId(RegisterGcmDeviceId registerGcmDeviceId) throws ApplicationException;

    UserDto registerFacebookAccount(RegisterFacebookAccountRequest registerFacebookAccountRequest) throws ApplicationException;

    UserDto registerFacebookAccountWebUser(RegisterFacebookAccountWebRequest registerFacebookAccountWebRequest) throws ApplicationException;

    UserDto updateUserInfo(UpdateUserRequestWebDto updateUserRequestWebDto) throws ApplicationException;

    UserDto updateMobileUserInfo(UpdateMobileUserRequestDto updateMobileUserRequestDto) throws ApplicationException;

    UserDto getUserByFacebookToken(String facebookTokenId) throws ApplicationException;

    List<PersonDto> getPersons(int page, int count) throws ApplicationException;

    List<PoliticalBodyAdminSearchResult> searchPoliticalBodyAdminByName(String text) throws ApplicationException;

}
