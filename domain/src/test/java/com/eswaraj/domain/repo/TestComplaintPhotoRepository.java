package com.eswaraj.domain.repo;

import java.util.Collection;
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
import com.eswaraj.domain.nodes.Photo;
import com.eswaraj.domain.nodes.relationships.ComplaintPhoto;

@ContextConfiguration(locations = { "classpath:eswaraj-domain-test.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class TestComplaintPhotoRepository extends BaseNeo4jEswarajTest {

    @Autowired
    ComplaintRepository complaintRepository;
    @Autowired
    LocationRepository locationRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    DepartmentRepository departmentRepository;
    @Autowired
    PhotoRepository photoRepository;
    @Autowired
    ComplaintPhotoRepository complaintPhotoRepository;

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
        Photo photo = new Photo();
        photo.setOrgUrl("http://some.com/photp.jpg");
        photo = photoRepository.save(photo);

        ComplaintPhoto complaintPhoto = new ComplaintPhoto();
        complaintPhoto.setComplaint(complaint);
        complaintPhoto.setPhoto(photo);
        complaintPhoto = complaintPhotoRepository.save(complaintPhoto);

        Collection<Photo> dbPhotos = photoRepository.getComplaintPhotos(complaint);
        System.out.println("dbPhotos=" + dbPhotos);

    }

}
