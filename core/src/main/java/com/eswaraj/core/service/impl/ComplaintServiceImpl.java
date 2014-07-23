package com.eswaraj.core.service.impl;

import java.util.LinkedHashSet;
import java.util.List;
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
import com.eswaraj.domain.nodes.Complaint;
import com.eswaraj.domain.nodes.Device;
import com.eswaraj.domain.nodes.Device.DeviceType;
import com.eswaraj.domain.nodes.Person;
import com.eswaraj.domain.nodes.Photo;
import com.eswaraj.domain.nodes.User;
import com.eswaraj.domain.repo.ComplaintRepository;
import com.eswaraj.domain.repo.DeviceRepository;
import com.eswaraj.domain.repo.PersonRepository;
import com.eswaraj.domain.repo.PhotoRepository;
import com.eswaraj.domain.repo.UserRepository;
import com.eswaraj.web.dto.ComplaintDto;
import com.eswaraj.web.dto.PhotoDto;
import com.eswaraj.web.dto.SaveComplaintRequestDto;
import com.eswaraj.web.dto.SaveComplaintResponseDto;

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
		Person person = getPerson(saveComplaintRequestDto);
		complaint.setPerson(person);
		
		complaint = complaintRepository.save(complaint);
		return complaintConvertor.convertBean(complaint);
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
			
			
			User user = new User();
			user.setExternalId(UUID.randomUUID().toString());
			user.setPerson(person);
			user = userRepository.save(user);
			
			device = new Device();
			device.setDeviceId(deviceId);
			device.setDeviceType(DeviceType.valueOf(deviceTypeRef));
			device.setUser(user);
			return person;
		}else{
			User user = userRepository.findOne(device.getUser().getId());
			Person person = personRepository.findOne(user.getPerson().getId());
			return person;
		}
	}
	private Device getDevice(String deviceId){
		try{
			System.out.println("Searching Device For "+ deviceId);
			Device device = deviceRepository.getDeviceByDeviceId(deviceId);
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
	public List<ComplaintDto> getPagedUserComplaints(String deviceId,
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
