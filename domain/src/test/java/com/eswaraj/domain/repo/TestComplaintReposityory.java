package com.eswaraj.domain.repo;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.eswaraj.domain.nodes.Category;
import com.eswaraj.domain.nodes.Complaint;
import com.eswaraj.domain.nodes.Person;
import com.eswaraj.domain.validator.exception.ValidationException;


/**
 * Test for complaint repo
 * @author anuj
 * @data Jan 22, 2014
 */

@ContextConfiguration(locations = { "classpath:eswaraj-domain-test.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class TestComplaintReposityory {

	@Autowired ComplaintRepository complaintRepository;
	@Autowired LocationRepository locationRepository;
	@Autowired CategoryRepository categoryRepository;
	@Autowired DepartmentRepository departmentRepository;
	@Autowired PersonRepository personRepository;
	
	@Test
	public void shouldSaveComplaint() {
		Complaint complaint = new Complaint("test complaint");
		Category category = new Category("cat1");
		complaint.setCategory(category);
		
		Person person = new Person();
		person.setName("Foo Bar");
		person.setEmail("foo@bar.com");
		person = personRepository.save(person);
		complaint.setPerson(person);
		
		complaint = complaintRepository.save(complaint);
		Complaint expectedComplaint = complaintRepository.getById(complaint.getId());
		assertEquals(expectedComplaint.getTitle(), complaint.getTitle());
	}
	
	@Test
	public void shouldGetComplaint_ByLocation() {
		Complaint complaint = new Complaint("test complaint");
		Category category = new Category("cat1");
		complaint.setCategory(category);
		
		Person person = new Person();
		person.setName("Foo Bar");
		person.setEmail("foo@bar.com");
		person = personRepository.save(person);
		complaint.setPerson(person);
		
		complaint = complaintRepository.save(complaint);
	}
	
	@Test
	public void shouldGetComplaint_ByCategory() {
		Complaint complaint = new Complaint("test complaint");
		Category category = new Category("cat1");
		category = categoryRepository.save(category);
		complaint.setCategory(category);
		
		Person person = new Person();
		person.setName("Foo Bar");
		person.setEmail("foo@bar.com");
		person = personRepository.save(person);
		complaint.setPerson(person);

		complaint = complaintRepository.save(complaint);
		
		Category category1 = categoryRepository.getById(complaint.getCategory().getId());
		Complaint expectedComplaint = complaintRepository.getByCategory(category1);
		assertEquals(expectedComplaint.getCategory().getName(), complaint.getCategory().getName());
		assertEquals(expectedComplaint.getCategory().getId(), complaint.getCategory().getId());
	}
	
	@Test(expected=ValidationException.class)
	public void shouldCheckEmpty_Title() {
		Complaint complaint = new Complaint(null);
		complaint = complaintRepository.save(complaint);
	}
	
	@Test(expected=ValidationException.class)
	public void shouldCheck_NoCatergory() {
		Complaint complaint = new Complaint("test complaint");
		complaint = complaintRepository.save(complaint);
	}
	
	@Test(expected=ValidationException.class)
	public void shouldCheck_NoLocation() {
		Complaint complaint = new Complaint("test complaint");
		Category category = new Category("cat1");
		complaint.setCategory(category);
		complaint = complaintRepository.save(complaint);
	}
	
	@Test(expected=ValidationException.class)
	public void shouldCheck_NoPerson() {
		Complaint complaint = new Complaint("test complaint");
		Category category = new Category("cat1");
		complaint.setCategory(category);
		complaint = complaintRepository.save(complaint);
	}
	
	@Test
	public void shouldLodgeComplaint_AsPending() {
		Complaint complaint = new Complaint("Test Complaint");
		Category category = new Category("cat1");
		complaint.setCategory(category);
		
		Person person = new Person();
		person.setName("Foo Bar");
		person.setEmail("foo@bar.com");
		person = personRepository.save(person);
		complaint.setPerson(person);

		complaint = complaintRepository.save(complaint);
		Complaint expectedComplaint = complaintRepository.getById(complaint.getId());
		assertEquals(expectedComplaint.getStatus(), Complaint.Status.PENDING);
	}
	
	@Test
	public void shouldGetAllComplaintByPerson() {
		Category category = new Category("cat1");
		category.setRoot(true);
		
		Complaint complaint1 = new Complaint("Test Complaint1");
		complaint1.setDateCreated(new Date(1403823364816L));
		complaint1.setCategory(category);
		
		Complaint complaint2 = new Complaint("Test Complaint2");
		complaint2.setDateCreated(new Date(1403823264816L));
		complaint2.setCategory(category);
		
		Complaint complaint3 = new Complaint("Test Complaint3");
		complaint3.setDateCreated(new Date(1403823164816L));
		complaint3.setCategory(category);
		
		Person person = new Person();
		person.setName("Foo Bar");
		person.setEmail("foo@bar.com");
		person = personRepository.save(person);
		
		complaint1.setPerson(person);
		complaint2.setPerson(person);
		complaint3.setPerson(person);

		complaint1 = complaintRepository.save(complaint1);
		complaint2 = complaintRepository.save(complaint2);
		complaint3 = complaintRepository.save(complaint3);
		
		List<Complaint> allComplaints = complaintRepository.getAllComplaintsLodgedByPerson(person);
		assertEquals(allComplaints.size(), 3);
	}
	
	@Test
	public void shouldNotGetAllComplaintByWrongPerson() {
		Category category = new Category("cat1");
		category.setRoot(true);
		
		Complaint complaint1 = new Complaint("Test Complaint1");
		complaint1.setDateCreated(new Date());
		complaint1.setCategory(category);
		
		Complaint complaint2 = new Complaint("Test Complaint2");
		complaint2.setDateCreated(new Date());
		complaint2.setCategory(category);
		
		Complaint complaint3 = new Complaint("Test Complaint3");
		complaint3.setDateCreated(new Date());
		complaint3.setCategory(category);
		
		Person person = new Person();
		person.setName("Foo Bar");
		person.setEmail("foo@bar.com");
		person = personRepository.save(person);
		
		Person person2 = new Person();
		person2.setName("Foo Bar Bar");
		person2.setEmail("foo1@bar.com");
		person2 = personRepository.save(person2);
		
		complaint1.setPerson(person);
		complaint2.setPerson(person);
		complaint3.setPerson(person);
		
		complaint1 = complaintRepository.save(complaint1);
		complaint2 = complaintRepository.save(complaint2);
		complaint3 = complaintRepository.save(complaint3);
		
		List<Complaint> allComplaints = complaintRepository.getAllComplaintsLodgedByPerson(person2);
		assertEquals(allComplaints.size(), 0);
	}
	
	
	@Test
	public void shouldGetPagedComplaintByPerson() {
		Category category = new Category("cat1");
		category.setRoot(true);
		
		Complaint complaint1 = new Complaint("Test Complaint1");
		complaint1.setDateCreated(new Date(1403023364816L));
		complaint1.setCategory(category);
		
		Complaint complaint2 = new Complaint("Test Complaint2");
		complaint2.setDateCreated(new Date(1403123364816L));
		complaint2.setCategory(category);
		
		Complaint complaint3 = new Complaint("Test Complaint3");
		complaint3.setDateCreated(new Date(1403223364816L));
		complaint3.setCategory(category);
		
		Complaint complaint4 = new Complaint("Test Complaint4");
		complaint4.setDateCreated(new Date(1403323364816L));
		complaint4.setCategory(category);
		
		Complaint complaint5 = new Complaint("Test Complaint5");
		complaint5.setDateCreated(new Date(1403423364816L));
		complaint5.setCategory(category);
		
		Person person = new Person();
		person.setName("Foo Bar");
		person.setEmail("foo@bar.com");
		person = personRepository.save(person);
		
		complaint1.setPerson(person);
		complaint2.setPerson(person);
		complaint3.setPerson(person);
		complaint4.setPerson(person);
		complaint5.setPerson(person);
		
		complaint1 = complaintRepository.save(complaint1);
		complaint2 = complaintRepository.save(complaint2);
		complaint3 = complaintRepository.save(complaint3);
		complaint4 = complaintRepository.save(complaint4);
		complaint5 = complaintRepository.save(complaint5);
		
		List<Complaint> allComplaints = complaintRepository.getPagedComplaintsLodgedByPerson(person, 0, 3);
		assertEquals(allComplaints.size(), 3);
	}
}
