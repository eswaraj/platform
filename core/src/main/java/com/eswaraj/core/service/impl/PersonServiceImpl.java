/**
 * 
 */
package com.eswaraj.core.service.impl;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eswaraj.core.convertors.PersonConvertor;
import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.PersonService;
import com.eswaraj.domain.nodes.Person;
import com.eswaraj.domain.repo.PersonRepository;
import com.eswaraj.web.dto.PersonDto;

/**
 * @author ravi
 *
 */
@Component
public class PersonServiceImpl implements PersonService {

	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private PersonConvertor personConvertor;
	
	@Override
	public PersonDto savePerson(PersonDto personDto) throws ApplicationException {
		Person person = personConvertor.convert(personDto);
		person = personRepository.save(person);
		return personConvertor.convertBean(person);
	}

	@Override
	public PersonDto getPersonById(Long personId) throws ApplicationException {
		Person person = personRepository.findOne(personId);
		return personConvertor.convertBean(person);
	}

	@Override
	public List<PersonDto> searchPersonStartWithName(String name) throws ApplicationException {
		Collection<Person> searchResult = personRepository.searchPersonByName("name:"+name+"*");
		return personConvertor.convertBeanList(searchResult);
	}

	@Override
	public List<PersonDto> searchPersonWithName(String name) throws ApplicationException {
		Collection<Person> searchResult = personRepository.searchPersonByName("name:*"+name+"*");
		return personConvertor.convertBeanList(searchResult);
	}

}
