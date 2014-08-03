package com.eswaraj.core.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.neo4j.cypher.MissingIndexException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.eswaraj.core.convertors.ComplaintConvertor;
import com.eswaraj.core.convertors.PhotoConvertor;
import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.ComplaintService;
import com.eswaraj.domain.nodes.Category;
import com.eswaraj.domain.nodes.Complaint;
import com.eswaraj.domain.nodes.Complaint.Status;
import com.eswaraj.domain.nodes.Device;
import com.eswaraj.domain.nodes.Device.DeviceType;
import com.eswaraj.domain.nodes.Person;
import com.eswaraj.domain.nodes.Photo;
import com.eswaraj.domain.nodes.PoliticalBodyAdmin;
import com.eswaraj.domain.nodes.User;
import com.eswaraj.domain.repo.CategoryRepository;
import com.eswaraj.domain.repo.ComplaintRepository;
import com.eswaraj.domain.repo.DeviceRepository;
import com.eswaraj.domain.repo.PersonRepository;
import com.eswaraj.domain.repo.PhotoRepository;
import com.eswaraj.domain.repo.UserRepository;
import com.eswaraj.messaging.dto.ComplaintCreatedMessage;
import com.eswaraj.queue.service.QueueService;
import com.eswaraj.web.dto.ComplaintDto;
import com.eswaraj.web.dto.PhotoDto;
import com.eswaraj.web.dto.SaveComplaintRequestDto;

/**
 * Implementation for complaint service
 * @author anuj
 * @data Jun 22, 2014
 */

@Component
@Transactional
public class ComplaintServiceImpl extends BaseService implements ComplaintService {
	
	@Autowired
	private ComplaintRepository complaintRepository;
	@Autowired
	private ComplaintConvertor complaintConvertor;
	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private PhotoRepository photoRepository;
	@Autowired
	private PhotoConvertor photoConvertor;
	@Autowired
	private DeviceRepository deviceRepository;
	@Autowired
	private UserRepository userRepository;
    @Autowired
    private QueueService queueService;
    @Autowired
    private CategoryRepository categoryRepository;

	@Override
	public List<ComplaintDto> getPagedUserComplaints(Long userId, int start, int end) throws ApplicationException{
		User user = getObjectIfExistsElseThrowExcetpion(userId, "User", userRepository);
		Person person = getObjectIfExistsElseThrowExcetpion(user.getPerson().getId(), "Person", personRepository);
		List<Complaint> personComplaints = complaintRepository.getPagedComplaintsLodgedByPerson(person, start, end); 
		return complaintConvertor.convertBeanList(personComplaints);
	}

	@Override
	public ComplaintDto getComplaintById(Long complaintId) throws ApplicationException {
		Complaint complaint = complaintRepository.findOne(complaintId);
		return complaintConvertor.convertBean(complaint);
	}

	@Override
	public ComplaintDto saveComplaint(SaveComplaintRequestDto saveComplaintRequestDto) throws ApplicationException {
		System.out.println("Saving Complaint "+ saveComplaintRequestDto);
		Complaint complaint = complaintConvertor.convert(saveComplaintRequestDto);

        complaint.setComplaintTime(Calendar.getInstance().getTimeInMillis());
		Person person = getPerson(saveComplaintRequestDto);
		complaint.setPerson(person);
		boolean newComplaint = true;
        if (complaint.getId() != null && complaint.getId() > 0) {
            newComplaint = false;
        } else {
            complaint.setStatus(Status.PENDING);
        }
		
		complaint = complaintRepository.save(complaint);

        if (newComplaint) {
            ComplaintCreatedMessage complaintCreatedMessage = buildComplaintCreatedMessage(complaint);
            queueService.sendComplaintCreatedMessage(complaintCreatedMessage);
        }

		return complaintConvertor.convertBean(complaint);
	}

    private ComplaintCreatedMessage buildComplaintCreatedMessage(Complaint complaint) {
        ComplaintCreatedMessage complaintCreatedMessage = new ComplaintCreatedMessage();
        if (complaint.getAdministrator() != null) {
            complaintCreatedMessage.setAdminId(complaint.getAdministrator().getId());
        }
        complaintCreatedMessage.setCategoryIds(getAllCategories(complaint.getCategory()));

        complaintCreatedMessage.setDescription(complaint.getDescription());

        // Get All Devices
        User user = userRepository.getUserByPerson(complaint.getPerson());
        complaintCreatedMessage.setUserId(user.getId());
        Collection<Device> allDevices = deviceRepository.getAllDevicesOfUser(user);
        List<String> deviceIds = new ArrayList<>();
        for (Device oneDevice : allDevices) {
            deviceIds.add(oneDevice.getDeviceType().toString() + "." + oneDevice.getDeviceId());
        }
        complaintCreatedMessage.setDeviceIds(deviceIds);

        complaintCreatedMessage.setLattitude(complaint.getLattitude());
        complaintCreatedMessage.setLongitude(complaint.getLongitude());
        complaintCreatedMessage.setPersonId(complaint.getPerson().getId());

        Set<PoliticalBodyAdmin> politicalAdmins = complaint.getServants();
        if (politicalAdmins != null) {
            List<Long> politicalAdminIds = new ArrayList<>(politicalAdmins.size());
            for (PoliticalBodyAdmin onePoliticalBodyAdmin : politicalAdmins) {
                politicalAdminIds.add(onePoliticalBodyAdmin.getId());
            }
            complaintCreatedMessage.setPoliticalAdminIds(politicalAdminIds);
        }

        complaintCreatedMessage.setStatus(complaint.getStatus().toString());
        complaintCreatedMessage.setTitle(complaint.getTitle());
        return complaintCreatedMessage;
    }

    private List<Long> getAllCategories(Category category) {
        List<Long> categoryIds = new ArrayList<>();
        if (category == null) {
            return categoryIds;
        }
        while (category != null) {
            categoryIds.add(category.getId());
            category = categoryRepository.getParentCategory(category);
        }
        return categoryIds;
    }
	
	private Person getPerson(SaveComplaintRequestDto saveComplaintRequestDto) throws ApplicationException{
		System.out.println("Get/Create Person");
		if(!StringUtils.isEmpty(saveComplaintRequestDto.getUserId())){
			User user = userRepository.getUserByUserExternalId(saveComplaintRequestDto.getUserId());
			if(user == null){
				//means mobile device has sent wrong user id and we can ignore it and continue
			}else{
				Person person = personRepository.findOne(user.getPerson().getId());
				return person;
			}
		}
		if(!StringUtils.isEmpty(saveComplaintRequestDto.getDeviceId())){
			return getPersonByDeviceId(saveComplaintRequestDto.getDeviceId(), saveComplaintRequestDto.getDeviceTypeRef());
		}
		throw new ApplicationException("Unbale to find/create a person");
	}
	private Person getPersonByDeviceId(String deviceId, String deviceTypeRef){
		Device device = getDevice(deviceId);
		if(device == null){
			//create Person , User and Device
			Person person = new Person();
			person.setName("anonymous");
			person.setExternalId(UUID.randomUUID().toString());
			person = personRepository.save(person);
            System.out.println("Person = " + person);
			
			
			User user = new User();
			user.setExternalId(UUID.randomUUID().toString());
			user.setPerson(person);
            System.out.println("user = " + user);
			user = userRepository.save(user);
            System.out.println("after user = " + user);
			
			device = new Device();
			device.setDeviceId(deviceId);
			device.setDeviceType(DeviceType.valueOf(deviceTypeRef));
			device.setUser(user);
			device = deviceRepository.save(device);
			return person;
		}else{
            System.out.println("Existing Device = " + device);
            System.out.println("Existing User Id = " + device.getUser().getId());
            User user = userRepository.getUserByDevice(device);

            System.out.println("Existing User = " + user);
            Person person = personRepository.getPersonByUser(user);
            System.out.println("Existing Person = " + person);
			return person;
		}
	}
	private Device getDevice(String deviceId){
		try{
			System.out.println("Searching Device For "+ deviceId);
			Device device = deviceRepository.getDeviceByDeviceId(deviceId);
            System.out.println("Comparing " + device.getId() + " and 66756");
            if (device.getId().equals(66756L)) {
                System.out.println("Deleting User " + device.getUser().getId());
                userRepository.delete(device.getUser().getId());
                System.out.println("Deleting Device " + device);
                deviceRepository.delete(device);
                return null;
            }
			return device;
		}catch(MissingIndexException mie){
			System.out.println("Exception occured mie");
			mie.printStackTrace();
			return null;
		}catch(Exception ex){
			System.out.println("Exception occured ex");
			ex.printStackTrace();
			return null;
		}
		
	}

	@Override
	public List<ComplaintDto> getAllUserComplaints(Long userId) throws ApplicationException {
		User user = getObjectIfExistsElseThrowExcetpion(userId, "User", userRepository);
		Person person = getObjectIfExistsElseThrowExcetpion(user.getPerson().getId(), "Person", personRepository);
		List<Complaint> personComplaints = complaintRepository.getAllComplaintsLodgedByPerson(person); 
		return complaintConvertor.convertBeanList(personComplaints);
	}

	@Override
	public PhotoDto addPhotoToComplaint(Long complaintId, PhotoDto photoDto) throws ApplicationException {
		Complaint complaint = complaintRepository.findOne(complaintId);
		Photo photo = photoConvertor.convert(photoDto);
		photo = photoRepository.save(photo);
		if(complaint.getPhotos() == null){
			complaint.setPhotos(new LinkedHashSet<Photo>());
		}
		complaint.getPhotos().add(photo);
		return photoConvertor.convertBean(photo);
	}

	@Override
	public List<ComplaintDto> getPagedDeviceComplaints(String deviceId,
			int start, int end) throws ApplicationException {
		Person person = getPersonByDeviceId(deviceId, "Android");
		List<Complaint> personComplaints = complaintRepository.getPagedComplaintsLodgedByPerson(person, start, end); 
		return complaintConvertor.convertBeanList(personComplaints);
	}

	@Override
	public List<ComplaintDto> getAllDeviceComplaints(String deviceId)
			throws ApplicationException {
		Person person = getPersonByDeviceId(deviceId, "Android");
		List<Complaint> personComplaints = complaintRepository.getAllComplaintsLodgedByPerson(person); 
		return complaintConvertor.convertBeanList(personComplaints);
	}
}
