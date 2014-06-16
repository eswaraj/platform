package com.eswaraj.core.convertors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.domain.nodes.Address;
import com.eswaraj.domain.nodes.Person;
import com.eswaraj.domain.repo.AddressRepository;
import com.eswaraj.domain.repo.PersonRepository;
import com.eswaraj.web.dto.PersonDto;

@Component
public class PersonConvertor extends BaseConvertor<Person, PersonDto> {

	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private AddressRepository addressRepository;
	@Autowired
	private AddressConvertor addressConvertor;
	
	

	@Override
	protected Person convertInternal(PersonDto personDto) throws ApplicationException {
		Person person = getObjectIfExists(personDto, "Person", personRepository) ;
		if(person == null){
			person = new Person();
		}
		
		BeanUtils.copyProperties(personDto, person);
		processAddress(person, personDto);
		
		
		return person;
	}
	private void processAddress(Person person, PersonDto personDto) throws ApplicationException{
		if(personDto.getPersonAddress() == null){
			if(person.getAddress() != null){
				//Delete the address
				addressRepository.delete(person.getAddress());
			}
		}else{
			//safe guard : even if client didn't send the id of existing address, check if already an address linked to Person
			//then use that address id to update it..... we don't want to leave an orphan address which is not linked to any person
			if(person.getAddress() != null){
				personDto.getPersonAddress().setId(person.getAddress().getId());
			}
			Address address = addressConvertor.convert(personDto.getPersonAddress());
			person.setAddress(address);
		}
	}
	
	@Override
	protected PersonDto convertBeanInternal(Person person) {
		PersonDto personDto = new PersonDto();
		BeanUtils.copyProperties(person, personDto);
		if(person.getAddress() != null){
			Address address = addressRepository.findOne(person.getAddress().getId());
			try {
				personDto.setPersonAddress(addressConvertor.convertBean(address));
			} catch (ApplicationException e) {
				e.printStackTrace();
			}
		}
		return personDto;
	}

}
