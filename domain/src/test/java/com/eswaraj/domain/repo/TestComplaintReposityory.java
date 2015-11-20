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
import com.eswaraj.domain.nodes.Complaint.Status;
import com.eswaraj.domain.nodes.DataClient;
import com.eswaraj.domain.nodes.Location;
import com.eswaraj.domain.nodes.LocationType;
import com.eswaraj.domain.nodes.Party;
import com.eswaraj.domain.nodes.Person;
import com.eswaraj.domain.nodes.PoliticalAdminComplaintStatus;
import com.eswaraj.domain.nodes.PoliticalBodyAdmin;
import com.eswaraj.domain.nodes.PoliticalBodyType;
import com.eswaraj.domain.nodes.extended.ComplaintSearchResult;
import com.eswaraj.domain.nodes.relationships.ComplaintLoggedByPerson;
import com.eswaraj.domain.nodes.relationships.ComplaintPoliticalAdmin;
import com.eswaraj.domain.validator.exception.ValidationException;


/**
 * Test for complaint repo
 * @author anuj
 * @data Jan 22, 2014
 */

@ContextConfiguration(locations = { "classpath:eswaraj-domain-test.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class TestComplaintReposityory extends BaseNeo4jEswarajTest{

	@Autowired ComplaintRepository complaintRepository;
	@Autowired LocationRepository locationRepository;
	@Autowired CategoryRepository categoryRepository;
	@Autowired DepartmentRepository departmentRepository;
	@Autowired PersonRepository personRepository;
	@Autowired ComplaintLoggedByPersonRepository complaintLoggedByPersonRepository;
    @Autowired
    PoliticalBodyAdminRepository politicalBodyAdminRepository;
    @Autowired
    PoliticalBodyTypeRepository politicalBodyTypeRepository;
    @Autowired
    LocationTypeRepository locationTypeRepository;
    @Autowired
    PartyRepository partyRepository;
    @Autowired
    ComplaintPoliticalAdminRepository complaintPoliticalAdminRepository;
	
	@Test
	public void shouldSaveComplaint() {
		Complaint complaint = new Complaint("test complaint");
		Category category = new Category("cat1");
		category.setRoot(true);
        Set<Category> categories = new HashSet<>();
        categories.add(category);
        complaint.setCategories(categories);
		
		complaint = complaintRepository.save(complaint);
		Complaint expectedComplaint = complaintRepository.findOne(complaint.getId());
		assertEquals(expectedComplaint.getTitle(), complaint.getTitle());
	}
	
	@Test
	public void shouldGetComplaint_ByLocation() {
		Complaint complaint = new Complaint("test complaint");
		Category category = new Category("cat1");
		category.setRoot(true);
        Set<Category> categories = new HashSet<>();
        categories.add(category);
        complaint.setCategories(categories);
		
		complaint = complaintRepository.save(complaint);
	}
	
	@Test
	public void shouldGetComplaint_ByCategory() {
		Complaint complaint = new Complaint(randomAlphaString(16));
		Category category = new Category(randomAlphaString(16));
		category.setRoot(true);
		category = categoryRepository.save(category);
        Set<Category> categories = new HashSet<>();
        categories.add(category);
        complaint.setCategories(categories);
		
		complaint = complaintRepository.save(complaint);
		
        Category category1 = categoryRepository.getById(complaint.getCategories().iterator().next().getId());
		List<Complaint> expectedComplaint = complaintRepository.getByCategory(category1);
		System.out.println("expectedComplaint="+expectedComplaint);
		System.out.println("complaint="+complaint);
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
        complaint.setCategories(null);
		complaint = complaintRepository.save(complaint);
	}
	
	@Test(expected=ValidationException.class)
	public void shouldCheck_NoPerson() {
		Complaint complaint = new Complaint("test complaint");
		Category category = new Category("cat1");
        complaint.setCategories(null);
		complaint = complaintRepository.save(complaint);
	}
	
	@Test
	public void shouldLodgeComplaint_AsPending() {
		Complaint complaint = new Complaint("Test Complaint");
		Category category = new Category("cat1");
		category.setRoot(true);
        Set<Category> categories = new HashSet<>();
        categories.add(category);
        complaint.setCategories(categories);
		
		complaint = complaintRepository.save(complaint);
		Complaint expectedComplaint = complaintRepository.findOne(complaint.getId());
		assertEquals(expectedComplaint.getStatus(), Complaint.Status.Pending);
	}
	
	@Test
	public void shouldGetAllComplaintByPerson() {
		Category category = new Category("cat1");
		category.setRoot(true);
		
		Complaint complaint1 = new Complaint("Test Complaint1");
		complaint1.setDateCreated(new Date(1403823364816L));
        Set<Category> categories = new HashSet<>();
        categories.add(category);
        complaint1.setCategories(categories);
		
		Complaint complaint2 = new Complaint("Test Complaint2");
		complaint2.setDateCreated(new Date(1403823264816L));
        Set<Category> categories2 = new HashSet<>();
        categories2.add(category);
        complaint2.setCategories(categories2);
		
		Complaint complaint3 = new Complaint("Test Complaint3");
		complaint3.setDateCreated(new Date(1403823164816L));
        Set<Category> categories3 = new HashSet<>();
        categories3.add(category);
        complaint3.setCategories(categories3);
		
		Person person = new Person();
		person.setName("Foo Bar");
		person.setEmail("foo@bar.com");
		person = personRepository.save(person);
		
        complaint1 = complaintRepository.save(complaint1);
        complaint2 = complaintRepository.save(complaint2);
        complaint3 = complaintRepository.save(complaint3);

        ComplaintLoggedByPerson complaintLoggedByPerson1 = new ComplaintLoggedByPerson();
        complaintLoggedByPerson1.setComplaint(complaint1);
        complaintLoggedByPerson1.setPerson(person);
        complaintLoggedByPerson1 = complaintLoggedByPersonRepository.save(complaintLoggedByPerson1);

        ComplaintLoggedByPerson complaintLoggedByPerson2 = new ComplaintLoggedByPerson();
        complaintLoggedByPerson2.setComplaint(complaint2);
        complaintLoggedByPerson2.setPerson(person);
        complaintLoggedByPerson2 = complaintLoggedByPersonRepository.save(complaintLoggedByPerson2);

        ComplaintLoggedByPerson complaintLoggedByPerson3 = new ComplaintLoggedByPerson();
        complaintLoggedByPerson3.setComplaint(complaint3);
        complaintLoggedByPerson3.setPerson(person);
        complaintLoggedByPerson3 = complaintLoggedByPersonRepository.save(complaintLoggedByPerson3);

		List<Complaint> allComplaints = complaintRepository.getAllComplaintsLodgedByPerson(person);
		assertEquals(allComplaints.size(), 3);
	}
	
	@Test
	public void shouldNotGetAllComplaintByWrongPerson() {
		Category category = new Category("cat1");
		category.setRoot(true);
		
		Complaint complaint1 = new Complaint("Test Complaint1");
		complaint1.setDateCreated(new Date());
        Set<Category> categories = new HashSet<>();
        categories.add(category);
        complaint1.setCategories(categories);
		
		Complaint complaint2 = new Complaint("Test Complaint2");
		complaint2.setDateCreated(new Date());
        Set<Category> categories2 = new HashSet<>();
        categories2.add(category);
        complaint2.setCategories(categories2);
		
		Complaint complaint3 = new Complaint("Test Complaint3");
		complaint3.setDateCreated(new Date());
        Set<Category> categories3 = new HashSet<>();
        categories3.add(category);
        complaint3.setCategories(categories3);
		
		Person person = new Person();
		person.setName("Foo Bar");
		person.setEmail("foo@bar.com");
		person = personRepository.save(person);
		
		Person person2 = new Person();
		person2.setName("Foo Bar Bar");
		person2.setEmail("foo1@bar.com");
		person2 = personRepository.save(person2);
		
        complaint1 = complaintRepository.save(complaint1);
        complaint2 = complaintRepository.save(complaint2);
        complaint3 = complaintRepository.save(complaint3);

        ComplaintLoggedByPerson complaintLoggedByPerson1 = new ComplaintLoggedByPerson();
        complaintLoggedByPerson1.setComplaint(complaint1);
        complaintLoggedByPerson1.setPerson(person);
        complaintLoggedByPerson1 = complaintLoggedByPersonRepository.save(complaintLoggedByPerson1);

        ComplaintLoggedByPerson complaintLoggedByPerson2 = new ComplaintLoggedByPerson();
        complaintLoggedByPerson2.setComplaint(complaint2);
        complaintLoggedByPerson2.setPerson(person);
        complaintLoggedByPerson2 = complaintLoggedByPersonRepository.save(complaintLoggedByPerson2);

        ComplaintLoggedByPerson complaintLoggedByPerson3 = new ComplaintLoggedByPerson();
        complaintLoggedByPerson3.setComplaint(complaint3);
        complaintLoggedByPerson3.setPerson(person);
        complaintLoggedByPerson3 = complaintLoggedByPersonRepository.save(complaintLoggedByPerson3);
		
		List<Complaint> allComplaints = complaintRepository.getAllComplaintsLodgedByPerson(person2);
		assertEquals(allComplaints.size(), 0);
	}
	
	
	@Test
	public void shouldGetPagedComplaintByPerson() {
		Category category = new Category("cat1");
		category.setRoot(true);
        Set<Category> categories = new HashSet<>();
        categories.add(category);
		
		Complaint complaint1 = new Complaint("Test Complaint1");
		complaint1.setDateCreated(new Date(1403023364816L));
        complaint1.setCategories(categories);
		
		Complaint complaint2 = new Complaint("Test Complaint2");
		complaint2.setDateCreated(new Date(1403123364816L));
        complaint2.setCategories(categories);
		
		Complaint complaint3 = new Complaint("Test Complaint3");
		complaint3.setDateCreated(new Date(1403223364816L));
        complaint3.setCategories(categories);
		
		Complaint complaint4 = new Complaint("Test Complaint4");
		complaint4.setDateCreated(new Date(1403323364816L));
        complaint4.setCategories(categories);
		
		Complaint complaint5 = new Complaint("Test Complaint5");
		complaint5.setDateCreated(new Date(1403423364816L));
        complaint5.setCategories(categories);
		
		Person person = new Person();
		person.setName("Foo Bar");
		person.setEmail("foo@bar.com");
		person = personRepository.save(person);
		
        complaint1 = complaintRepository.save(complaint1);
        complaint2 = complaintRepository.save(complaint2);
        complaint3 = complaintRepository.save(complaint3);
        complaint4 = complaintRepository.save(complaint4);
        complaint5 = complaintRepository.save(complaint5);

        ComplaintLoggedByPerson complaintLoggedByPerson1 = new ComplaintLoggedByPerson();
        complaintLoggedByPerson1.setComplaint(complaint1);
        complaintLoggedByPerson1.setPerson(person);
        complaintLoggedByPerson1 = complaintLoggedByPersonRepository.save(complaintLoggedByPerson1);

        ComplaintLoggedByPerson complaintLoggedByPerson2 = new ComplaintLoggedByPerson();
        complaintLoggedByPerson2.setComplaint(complaint2);
        complaintLoggedByPerson2.setPerson(person);
        complaintLoggedByPerson2 = complaintLoggedByPersonRepository.save(complaintLoggedByPerson2);

        ComplaintLoggedByPerson complaintLoggedByPerson3 = new ComplaintLoggedByPerson();
        complaintLoggedByPerson3.setComplaint(complaint3);
        complaintLoggedByPerson3.setPerson(person);
        complaintLoggedByPerson3 = complaintLoggedByPersonRepository.save(complaintLoggedByPerson3);

        ComplaintLoggedByPerson complaintLoggedByPerson4 = new ComplaintLoggedByPerson();
        complaintLoggedByPerson4.setComplaint(complaint4);
        complaintLoggedByPerson4.setPerson(person);
        complaintLoggedByPerson4 = complaintLoggedByPersonRepository.save(complaintLoggedByPerson4);

        ComplaintLoggedByPerson complaintLoggedByPerson5 = new ComplaintLoggedByPerson();
        complaintLoggedByPerson5.setComplaint(complaint5);
        complaintLoggedByPerson5.setPerson(person);
        complaintLoggedByPerson5 = complaintLoggedByPersonRepository.save(complaintLoggedByPerson5);

		List<Complaint> allComplaints = complaintRepository.getPagedComplaintsLodgedByPerson(person, 0, 3);
		assertEquals(allComplaints.size(), 3);
        allComplaints = complaintRepository.getPagedComplaintsLodgedByPerson(person, 3, 3);
        assertEquals(allComplaints.size(), 2);
	}

    @Test
    public void createTwoRelationsOfSameComplaintAndPerson() {
        Category category = new Category("cat1");
        category.setRoot(true);
        Set<Category> categories = new HashSet<>();
        categories.add(category);

        Complaint complaint = new Complaint("Test Complaint1");
        complaint.setDateCreated(new Date(1403023364816L));
        complaint.setCategories(categories);

        Person person = new Person();
        person.setName("Foo Bar");
        person.setEmail("foo@bar.com");
        person = personRepository.save(person);

        complaint = complaintRepository.save(complaint);

        ComplaintLoggedByPerson complaintLoggedByPerson1 = new ComplaintLoggedByPerson();
        complaintLoggedByPerson1.setComplaint(complaint);
        complaintLoggedByPerson1.setPerson(person);
        complaintLoggedByPerson1 = complaintLoggedByPersonRepository.save(complaintLoggedByPerson1);

        ComplaintLoggedByPerson complaintLoggedByPerson2 = new ComplaintLoggedByPerson();
        complaintLoggedByPerson2.setComplaint(complaint);
        complaintLoggedByPerson2.setPerson(person);
        complaintLoggedByPerson2 = complaintLoggedByPersonRepository.save(complaintLoggedByPerson2);
        
        System.out.println("complaintLoggedByPerson1=" + complaintLoggedByPerson1.getId());
        System.out.println("complaintLoggedByPerson2=" + complaintLoggedByPerson2.getId());
    }

    @Test
    public void queryDataUsingSearchComplaint(){
        Person person = createPerson(personRepository, "Ravi Sharma");
        DataClient dataClient = new DataClient();
        dataClient.setName("India");
        LocationType locationType = createLocationType(locationTypeRepository, "AC", null, dataClient, true);

        PoliticalBodyType politicalBodyType = createPoliticalBodyType(politicalBodyTypeRepository, "MLA", "MLA", "MLA", locationType);

        Location location = createLocation(locationRepository, "XYZ", locationType, null);
        Party party = createParty(partyRepository, "AAP", "AAP");
        PoliticalBodyAdmin politicalBodyAdmin = createPoliticalBodyAdmin(politicalBodyAdminRepository, true, "abc@gef.com", new Date(), new Date(), null, null, "", "", location, "", "", party,
                person,
                politicalBodyType, "s");

        Category category1 = new Category("cat1");
        category1.setRoot(true);
        category1 = categoryRepository.save(category1);
        Category category2 = new Category("cat2");
        category2.setRoot(false);
        category2.setParentCategory(category1);
        category2 = categoryRepository.save(category2);
        Set<Category> categories = new HashSet<>();
        categories.add(category1);
        categories.add(category2);

        Complaint complaint = new Complaint();
        complaint.setCategories(categories);
        complaint.setDescription("Test Com");
        complaint.setStatus(Status.Pending);
        complaint.setTitle("Test Complaint");
        complaint.setDateCreated(new Date());
        complaint = complaintRepository.save(complaint);

        ComplaintLoggedByPerson complaintLoggedByPerson = new ComplaintLoggedByPerson();
        complaintLoggedByPerson.setComplaint(complaint);
        complaintLoggedByPerson.setPerson(person);
        complaintLoggedByPerson = complaintLoggedByPersonRepository.save(complaintLoggedByPerson);

        ComplaintPoliticalAdmin complaintPoliticalAdmin = new ComplaintPoliticalAdmin();
        complaintPoliticalAdmin.setComplaint(complaint);
        complaintPoliticalAdmin.setPoliticalBodyAdmin(politicalBodyAdmin);
        complaintPoliticalAdmin.setStatus(PoliticalAdminComplaintStatus.Pending);
        complaintPoliticalAdmin = complaintPoliticalAdminRepository.save(complaintPoliticalAdmin);

        List<ComplaintSearchResult> result = complaintRepository.getAllPagedComplaintsOfPoliticalAdmin(politicalBodyAdmin.getId(), 0, 10);
        for (ComplaintSearchResult oneComplaintSearchResult : result) {
            System.out.println(oneComplaintSearchResult.getComplaint());
            System.out.println(oneComplaintSearchResult.getComplaintPoliticalAdmin());

            for (Category oneCategory : oneComplaintSearchResult.getComplaint().getCategories()) {
                System.out.println("oneCategory : " + oneCategory);
            }
        }

    }
}
