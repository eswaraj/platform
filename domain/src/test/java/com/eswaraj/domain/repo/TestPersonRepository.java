package com.eswaraj.domain.repo;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.eswaraj.domain.nodes.Person;

/**
 * Test for Person repository
 * 
 * @author Ravi
 * @data Jan 22, 2014
 */

@ContextConfiguration(locations = { "classpath:eswaraj-domain-test.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class TestPersonRepository extends BaseNeo4jEswarajTest{

	@Autowired PersonRepository personRespository;
	
	/**
	 * Test to save Person with minimum required field(name and Email)
	 */
	@Test
	public void test01_savePerson() {
		Person person = new Person();
		person.setName(randomAlphaString(16));
		person.setEmail(randomEmailAddress());
		person = personRespository.save(person);
		assertNotNull(person);
		assertNotNull(person.getId());
	}
	/**
	 * Test to save Person with minimum required field(name and Email) + a Phone Number
	 */
	@Test
	public void test02_savePerson() {
		Person person = new Person();
		person.setName(randomAlphaString(16));
		person.setEmail(randomEmailAddress());
		
		person.setMobileNumber1("8901550000");
		
		person = personRespository.save(person);
		assertNotNull(person);
		assertNotNull(person.getId());
		assertNotNull(person.getMobileNumber1());
	}
	
	@Test 
	public void test03_searchByName(){
		Person person = new Person();
		String name="Ravi Sharma";
		person.setName(name);
		person = personRespository.save(person);
		
		Collection<Person> searchResult = personRespository.searchPersonByName("name:*avi*");
		System.out.println("searchResult="+searchResult);
		assertEquals(1, searchResult.size());
		
		searchResult = personRespository.searchPersonByName("name:*AXCFS*");
		System.out.println("searchResult="+searchResult);
		assertEquals(0, searchResult.size());
		
		searchResult = personRespository.searchPersonByName("name:Ravi*");
		System.out.println("searchResult="+searchResult);
		assertEquals(1, searchResult.size());
		
		searchResult = personRespository.searchPersonByName("name:*ma");
		System.out.println("searchResult="+searchResult);
		assertEquals(1, searchResult.size());
		
	}

	
}
