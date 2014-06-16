package com.eswaraj.core.service.impl;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.eswaraj.core.BaseNeo4jEswarajTest;
import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.PersonService;
import com.eswaraj.domain.validator.exception.ValidationException;
import com.eswaraj.web.dto.AddressDto;
import com.eswaraj.web.dto.PersonDto;

@ContextConfiguration(locations = { "classpath:eswaraj-core-test.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class TestPersonServiceImpl extends BaseNeo4jEswarajTest {

	@Autowired
	private PersonService personService;

	/**
	 * Create a person with minimum required field, i.e name
	 * @throws ApplicationException 
	 */
	@Test
	public void test01_savePerson() throws ApplicationException{
		String name = randomAlphaString(16);
		PersonDto personDto = createPerson(name, null, null, null, null, null, null, null, null, null);
		PersonDto savedPersonDto = personService.savePerson(personDto);
		assertEqualPersons(personDto, savedPersonDto, false);
		
		PersonDto dbPersonDto = personService.getPersonById(savedPersonDto.getId());
		assertEqualPersons(personDto, dbPersonDto, false);
		assertEqualPersons(savedPersonDto, dbPersonDto, true);
		
	}
	/**
	 * Create a person with minimum required field, i.e name but length less then 2
	 * Should throw VlidationException
	 * @throws ApplicationException 
	 */
	@Test(expected=ValidationException.class)
	public void test02_savePerson() throws ApplicationException{
		String name = randomAlphaString(1);
		PersonDto personDto = createPerson(name, null, null, null, null, null, null, null, null, null);
		personService.savePerson(personDto);//Shud throw validation exception
		
	}
	/**
	 * Create a person with minimum required field, i.e name but length more then 64
	 * Should throw VlidationException
	 * @throws ApplicationException 
	 */
	@Test(expected=ValidationException.class)
	public void test03_savePerson() throws ApplicationException{
		String name = randomAlphaString(65);
		PersonDto personDto = createPerson(name, null, null, null, null, null, null, null, null, null);
		personService.savePerson(personDto);//Shud throw validation exception
		
	}
	/**
	 * Create a person with name as null
	 * Should throw ValidationException
	 * @throws ApplicationException 
	 */
	@Test(expected=ValidationException.class)
	public void test04_savePerson() throws ApplicationException{
		String name = null;
		PersonDto personDto = createPerson(name, null, null, null, null, null, null, null, null, null);
		personService.savePerson(personDto);//Shud throw validation exception
		
	}
	/**
	 * Create a person with name as Empty ""
	 * Should throw ValidationException
	 * @throws ApplicationException 
	 */
	@Test(expected=ValidationException.class)
	public void test05_savePerson() throws ApplicationException{
		String name = "";
		PersonDto personDto = createPerson(name, null, null, null, null, null, null, null, null, null);
		personService.savePerson(personDto);//Shud throw validation exception
		
	}
	
	/**
	 * Create a person with name having invalid characters
	 * Should throw ValidationException
	 * @throws ApplicationException 
	 */
	@Test(expected=ValidationException.class)
	public void test06_savePerson() throws ApplicationException{
		String name = "adas$%@^%@&^09&*D";
		PersonDto personDto = createPerson(name, null, null, null, null, null, null, null, null, null);
		personService.savePerson(personDto);//Shud throw validation exception
		
	}
	
	/**
	 * Create a person with name and invalid email id
	 * Should throw ValidationException
	 * @throws ApplicationException 
	 */
	@Test(expected=ValidationException.class)
	public void test07_savePerson() throws ApplicationException{
		String name = randomAlphaString(16);
		String email = randomAlphaString(10);
		PersonDto personDto = createPerson(name, email, null, null, null, null, null, null, null, null);
		personService.savePerson(personDto);//Shud throw validation exception
		
	}
	
	/**
	 * Create a person with name and valid email id
	 * @throws ApplicationException 
	 */
	@Test
	public void test08_savePerson() throws ApplicationException{
		String name = randomAlphaString(16);
		String email = randomEmailAddress();
		PersonDto personDto = createPerson(name, email, null, null, null, null, null, null, null, null);
		PersonDto savedPersonDto = personService.savePerson(personDto);
		
		assertEqualPersons(personDto, savedPersonDto, false);
		
	}
	
	/**
	 * Create a person with all
	 * @throws ApplicationException 
	 */
	@Test
	public void test09_savePerson() throws ApplicationException{
		String name = randomAlphaString(16);
		String email = randomEmailAddress();
		String bioData = randomAlphaString(1024);
		Date dob = new Date();
		String gender = "Male";
		String landlineNumber1 = randomNumericString(10); 
		String landlineNumber2 = randomNumericString(10);
		String mobileNumber1 = randomNumericString(10); 
		String mobileNumber2 = randomNumericString(10);
		AddressDto personAddress = new AddressDto();
		personAddress.setLine1(randomAlphaString(30));
		personAddress.setPostalCode(randomNumericString(6));
		PersonDto personDto = createPerson(name, email, bioData, dob, gender, landlineNumber1, landlineNumber2, mobileNumber1, mobileNumber2, personAddress);
		PersonDto savedPersonDto = personService.savePerson(personDto);
		
		assertEqualPersons(personDto, savedPersonDto, false);
		
		PersonDto dbPersonDto = personService.getPersonById(savedPersonDto.getId());
		assertEqualPersons(personDto, dbPersonDto, false);
		assertEqualPersons(savedPersonDto, dbPersonDto, true);
		
	}
	
	/**
	 * Create a person and then search it by part of name(start with)
	 * @throws ApplicationException 
	 */
	@Test
	public void test10_searchPersonStartWithName() throws ApplicationException{
		String name = "Ravi Sharma";
		PersonDto personDto = createPerson(name, null, null, null, null, null, null, null, null, null);
		PersonDto savedPersonDto = personService.savePerson(personDto);
		
		assertEqualPersons(personDto, savedPersonDto, false);
		
		List<PersonDto> dbPersonDtos = personService.searchPersonStartWithName("Ravi");
		assertEquals(1, dbPersonDtos.size());
		assertEqualPersons(personDto, dbPersonDtos.get(0), false);
		assertEqualPersons(savedPersonDto, dbPersonDtos.get(0), true);
		
		dbPersonDtos = personService.searchPersonStartWithName("ravi");
		assertEquals(1, dbPersonDtos.size());
		assertEqualPersons(personDto, dbPersonDtos.get(0), false);
		assertEqualPersons(savedPersonDto, dbPersonDtos.get(0), true);
		
		dbPersonDtos = personService.searchPersonStartWithName("r");
		assertEquals(1, dbPersonDtos.size());
		assertEqualPersons(personDto, dbPersonDtos.get(0), false);
		assertEqualPersons(savedPersonDto, dbPersonDtos.get(0), true);

		dbPersonDtos = personService.searchPersonStartWithName("R");
		assertEquals(1, dbPersonDtos.size());
		assertEqualPersons(personDto, dbPersonDtos.get(0), false);
		assertEqualPersons(savedPersonDto, dbPersonDtos.get(0), true);

		dbPersonDtos = personService.searchPersonStartWithName("a");
		assertEquals(0, dbPersonDtos.size());
	}
	/**
	 * Create a person and then search it by part of name(any part)
	 * @throws ApplicationException 
	 */
	@Test
	public void test11_searchPersonWithName() throws ApplicationException{
		String name = "Ravi Sharma";
		PersonDto personDto = createPerson(name, null, null, null, null, null, null, null, null, null);
		PersonDto savedPersonDto = personService.savePerson(personDto);
		
		assertEqualPersons(personDto, savedPersonDto, false);
		
		List<PersonDto> dbPersonDtos = personService.searchPersonWithName("Ravi");
		assertEquals(1, dbPersonDtos.size());
		assertEqualPersons(personDto, dbPersonDtos.get(0), false);
		assertEqualPersons(savedPersonDto, dbPersonDtos.get(0), true);
		
		dbPersonDtos = personService.searchPersonWithName("ravi");
		assertEquals(1, dbPersonDtos.size());
		assertEqualPersons(personDto, dbPersonDtos.get(0), false);
		assertEqualPersons(savedPersonDto, dbPersonDtos.get(0), true);
		
		dbPersonDtos = personService.searchPersonWithName("r");
		assertEquals(1, dbPersonDtos.size());
		assertEqualPersons(personDto, dbPersonDtos.get(0), false);
		assertEqualPersons(savedPersonDto, dbPersonDtos.get(0), true);

		dbPersonDtos = personService.searchPersonWithName("i");
		assertEquals(1, dbPersonDtos.size());
		assertEqualPersons(personDto, dbPersonDtos.get(0), false);
		assertEqualPersons(savedPersonDto, dbPersonDtos.get(0), true);

		dbPersonDtos = personService.searchPersonWithName("ma");
		assertEquals(1, dbPersonDtos.size());
		assertEqualPersons(personDto, dbPersonDtos.get(0), false);
		assertEqualPersons(savedPersonDto, dbPersonDtos.get(0), true);

		dbPersonDtos = personService.searchPersonWithName("a");
		assertEquals(1, dbPersonDtos.size());
		assertEqualPersons(personDto, dbPersonDtos.get(0), false);
		assertEqualPersons(savedPersonDto, dbPersonDtos.get(0), true);
		
		dbPersonDtos = personService.searchPersonWithName("x");
		assertEquals(0, dbPersonDtos.size());
	}
}
