package com.eswaraj.domain.repo;

import java.util.HashSet;
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
import com.eswaraj.domain.nodes.relationships.ComplaintLoggedByPerson;

@ContextConfiguration(locations = { "classpath:eswaraj-domain-test.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class TestComplaintLoggedByPersonRepository extends BaseNeo4jEswarajTest {

    @Autowired
    ComplaintRepository complaintRepository;
    @Autowired
    LocationRepository locationRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    DepartmentRepository departmentRepository;
    @Autowired
    PersonRepository personRepository;
    @Autowired
    ComplaintLoggedByPersonRepository complaintLoggedByPersonRepository;

    @Test
    public void shouldSaveComplaint() {

        Complaint complaint = new Complaint("test complaint");
        Category category = new Category("cat1");
        category.setRoot(true);
        Set<Category> categories = new HashSet<>();
        categories.add(category);
        complaint.setCategories(categories);

        complaint = complaintRepository.save(complaint);

        // Create Person
        Person person = createPerson(personRepository, "Ravi");

        ComplaintLoggedByPerson complaintLoggedByPerson = new ComplaintLoggedByPerson();
        complaintLoggedByPerson.setComplaint(complaint);
        complaintLoggedByPerson.setPerson(person);
        complaintLoggedByPerson = complaintLoggedByPersonRepository.save(complaintLoggedByPerson);

        ComplaintLoggedByPerson dbComplaintLoggedByPerson = complaintLoggedByPersonRepository.getComplaintLoggedByPersonRelation(complaint, person);
        System.out.println("dbComplaintLoggedByPerson=" + dbComplaintLoggedByPerson);

    }

}
