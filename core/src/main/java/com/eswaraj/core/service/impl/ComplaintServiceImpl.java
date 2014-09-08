package com.eswaraj.core.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.neo4j.cypher.MissingIndexException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.eswaraj.core.convertors.ComplaintConvertor;
import com.eswaraj.core.convertors.PhotoConvertor;
import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.AppKeyService;
import com.eswaraj.core.service.ComplaintService;
import com.eswaraj.domain.nodes.Category;
import com.eswaraj.domain.nodes.Complaint;
import com.eswaraj.domain.nodes.Complaint.Status;
import com.eswaraj.domain.nodes.Device;
import com.eswaraj.domain.nodes.Device.DeviceType;
import com.eswaraj.domain.nodes.Location;
import com.eswaraj.domain.nodes.Person;
import com.eswaraj.domain.nodes.Photo;
import com.eswaraj.domain.nodes.PoliticalBodyAdmin;
import com.eswaraj.domain.nodes.User;
import com.eswaraj.domain.repo.CategoryRepository;
import com.eswaraj.domain.repo.ComplaintRepository;
import com.eswaraj.domain.repo.DeviceRepository;
import com.eswaraj.domain.repo.LocationRepository;
import com.eswaraj.domain.repo.PersonRepository;
import com.eswaraj.domain.repo.PhotoRepository;
import com.eswaraj.domain.repo.PoliticalBodyAdminRepository;
import com.eswaraj.domain.repo.UserRepository;
import com.eswaraj.messaging.dto.ComplaintMessage;
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
	
    private static final long serialVersionUID = 1L;
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
    @Autowired
    private AppKeyService appKeyService;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private PoliticalBodyAdminRepository politicalBodyAdminRepository;

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
            complaint.setDateModified(new Date());
        } else {
            complaint.setStatus(Status.PENDING);
            complaint.setDateCreated(new Date());
            complaint.setDateModified(new Date());
        }


        ComplaintMessage complaintMessage = updateLocationAndAdmins(complaint);
		complaint = complaintRepository.save(complaint);
        complaintMessage.setId(complaint.getId());

        if (newComplaint) {
            queueService.sendComplaintCreatedMessage(complaintMessage);
        }

		return complaintConvertor.convertBean(complaint);
	}

    private ComplaintMessage buildComplaintMessage(Complaint complaint) {
        ComplaintMessage complaintCreatedMessage = new ComplaintMessage();
        complaintCreatedMessage.setId(complaint.getId());
        if (complaint.getAdministrator() != null) {
            complaintCreatedMessage.setAdminId(complaint.getAdministrator().getId());
        }
        complaintCreatedMessage.setCategoryIds(getAllCategories(complaint.getCategories()));

        complaintCreatedMessage.setDescription(complaint.getDescription());

        // Get All Devices
        User user = userRepository.getUserByPerson(complaint.getPerson());
        complaintCreatedMessage.setUserId(user.getId());
        Collection<Device> allDevices = deviceRepository.getAllDevicesOfUser(user);
        List<String> deviceIds = new ArrayList<>();
        for (Device oneDevice : allDevices) {
            deviceIds.add(oneDevice.getDeviceType().toString() + "." + oneDevice.getDeviceId());
        }
        complaintCreatedMessage.setComplaintTime(complaint.getComplaintTime());
        complaintCreatedMessage.setDeviceIds(deviceIds);


        complaintCreatedMessage.setLattitude(complaint.getLattitude());
        complaintCreatedMessage.setLongitude(complaint.getLongitude());

        if (complaint.getLocations() != null && !complaint.getLocations().isEmpty()) {
            List<Long> locationIds = new ArrayList<>();
            for (Location oneLocation : complaint.getLocations()) {
                locationIds.add(oneLocation.getId());
            }
            complaintCreatedMessage.setLocationIds(locationIds);

        }

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

    private List<Long> getAllCategories(Set<Category> categories) {
        List<Long> categoryIds = new ArrayList<>();
        if (categories == null) {
            return categoryIds;
        }
        for (Category oneCatgeory : categories) {
            categoryIds.add(oneCatgeory.getId());
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

    @Override
    public ComplaintMessage updateLocationAndAdmins(Long complaintId) throws ApplicationException {
        Complaint complaint = complaintRepository.findOne(complaintId);
        return updateLocationAndAdmins(complaint);
    }

    private ComplaintMessage updateLocationAndAdmins(Complaint complaint) throws ApplicationException {
        // Get all Locations and attach to it.
        String rediskey = appKeyService.buildLocationKey(complaint.getLattitude(), complaint.getLongitude());
        System.out.println("Get Locations for Key : " + rediskey);
        Set<String> complaintLocations = stringRedisTemplate.opsForSet().members(rediskey);
        System.out.println("Founds Locations for Key : " + complaintLocations);
        if (complaintLocations.isEmpty()) {
            complaintLocations.add("78340");
        }
        if (complaintLocations != null && !complaintLocations.isEmpty()) {

            Set<Location> locations = new HashSet<>();

            Set<PoliticalBodyAdmin> politicalBodyAdmins = new HashSet<>();
            Collection<PoliticalBodyAdmin> oneLocationPoliticalBodyAdmins;

            Long locationId;
            for (String oneLocationId : complaintLocations) {
                locationId = Long.parseLong(oneLocationId);
                Location oneLocation = locationRepository.findOne(locationId);
                locations.add(oneLocation);
                addAllParentLocationsToComplaint(locations, oneLocation, complaintLocations);

                oneLocationPoliticalBodyAdmins = politicalBodyAdminRepository.getCurrentPoliticalAdminByLocation(oneLocation);
                if (oneLocationPoliticalBodyAdmins != null && !oneLocationPoliticalBodyAdmins.isEmpty()) {
                    politicalBodyAdmins.addAll(oneLocationPoliticalBodyAdmins);
                }
            }
            complaint.setLocations(locations);
            complaint.setServants(politicalBodyAdmins);

            // TODO find Executive Admin based on Location and Category and
            // attach it to complaint
        }
        complaint.setNearByKey(appKeyService.buildLocationKeyForNearByComplaints(complaint.getLattitude(), complaint.getLongitude()));
        complaint = complaintRepository.save(complaint);
        return buildComplaintMessage(complaint);
    }

    private void addAllParentLocationsToComplaint(Set<Location> locations, Location location, Set<String> complaintLocations) {
        if (location.getParentLocation() == null) {
            System.out.println("No Parent for location " + location.getId());
            return;
        }
        System.out.println("Parent for location " + location.getId() + " is " + location.getParentLocation());
        if (complaintLocations.contains(String.valueOf(location.getParentLocation().getId()))) {
            System.out.println("Not processing Parent location " + location.getParentLocation().getId());
            return;// No need to do naything
        }
        System.out.println("Find location with Id " + location.getParentLocation().getId());
        Location location2 = locationRepository.findOne(location.getParentLocation().getId());
        System.out.println("Found " + location2);
        locations.add(location2);
        addAllParentLocationsToComplaint(locations, location2, complaintLocations);

    }

    @Override
    public List<ComplaintDto> getAllComplaints(Long start, Long totalComplaints) throws ApplicationException {
        return complaintConvertor.convertBeanList(complaintRepository.getAllPagedComplaints(start, totalComplaints));
    }

    @Override
    public List<ComplaintDto> getAllComplaintsOfLocation(Long locationId, Long start, Long totalComplaints) throws ApplicationException {
        return complaintConvertor.convertBeanList(complaintRepository.getAllPagedComplaintsOfLocation(locationId, start, totalComplaints));
    }
}
