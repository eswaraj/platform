package com.eswaraj.core.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.eswaraj.core.convertors.CategoryConvertor;
import com.eswaraj.core.convertors.ComplaintConvertor;
import com.eswaraj.core.convertors.PhotoConvertor;
import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.AppKeyService;
import com.eswaraj.core.service.ComplaintService;
import com.eswaraj.core.service.SettingService;
import com.eswaraj.core.service.UrlShortenService;
import com.eswaraj.domain.nodes.Category;
import com.eswaraj.domain.nodes.Comment;
import com.eswaraj.domain.nodes.Complaint;
import com.eswaraj.domain.nodes.Complaint.Status;
import com.eswaraj.domain.nodes.Department;
import com.eswaraj.domain.nodes.Location;
import com.eswaraj.domain.nodes.Person;
import com.eswaraj.domain.nodes.Photo;
import com.eswaraj.domain.nodes.PoliticalAdminComplaintStatus;
import com.eswaraj.domain.nodes.PoliticalBodyAdmin;
import com.eswaraj.domain.nodes.Setting;
import com.eswaraj.domain.nodes.Setting.SettingNames;
import com.eswaraj.domain.nodes.User;
import com.eswaraj.domain.nodes.relationships.ComplaintComment;
import com.eswaraj.domain.nodes.relationships.ComplaintLoggedByPerson;
import com.eswaraj.domain.nodes.relationships.ComplaintPhoto;
import com.eswaraj.domain.nodes.relationships.ComplaintPoliticalAdmin;
import com.eswaraj.domain.nodes.relationships.DepartmentComplaint;
import com.eswaraj.domain.repo.CategoryRepository;
import com.eswaraj.domain.repo.CommentRepository;
import com.eswaraj.domain.repo.ComplaintCommentRepository;
import com.eswaraj.domain.repo.ComplaintLoggedByPersonRepository;
import com.eswaraj.domain.repo.ComplaintPhotoRepository;
import com.eswaraj.domain.repo.ComplaintPoliticalAdminRepository;
import com.eswaraj.domain.repo.ComplaintRepository;
import com.eswaraj.domain.repo.DepartmentComplaintRepository;
import com.eswaraj.domain.repo.DepartmentLocationRepository;
import com.eswaraj.domain.repo.DeviceRepository;
import com.eswaraj.domain.repo.LocationRepository;
import com.eswaraj.domain.repo.PersonRepository;
import com.eswaraj.domain.repo.PhotoRepository;
import com.eswaraj.domain.repo.PoliticalBodyAdminRepository;
import com.eswaraj.domain.repo.UserRepository;
import com.eswaraj.messaging.dto.ComplaintMessage;
import com.eswaraj.queue.service.QueueService;
import com.eswaraj.web.dto.CommentComplaintDto;
import com.eswaraj.web.dto.ComplaintDto;
import com.eswaraj.web.dto.ComplaintStatusChangeByPersonRequestDto;
import com.eswaraj.web.dto.ComplaintStatusChangeByPoliticalAdminRequestDto;
import com.eswaraj.web.dto.ComplaintViewdByPoliticalAdminRequestDto;
import com.eswaraj.web.dto.PersonDto;
import com.eswaraj.web.dto.PhotoDto;
import com.eswaraj.web.dto.PoliticalAdminComplaintDto;
import com.eswaraj.web.dto.SaveComplaintRequestDto;
import com.eswaraj.web.dto.comment.CommentSaveRequestDto;
import com.eswaraj.web.dto.comment.CommentSaveResponseDto;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Implementation for complaint service
 * 
 * @author ravi
 * @data Jun 22, 2014
 */

@Component
@Transactional
public class ComplaintServiceImpl extends BaseService implements ComplaintService {
	
    private static final long serialVersionUID = 1L;

    @Autowired
    private SettingService settingService;
    @Autowired
	private ComplaintRepository complaintRepository;
    @Autowired
    private ComplaintPhotoRepository complaintPhotoRepository;
    @Autowired
    private ComplaintPoliticalAdminRepository complaintPoliticalAdminRepository;
	@Autowired
	private ComplaintConvertor complaintConvertor;
	@Autowired
    private ComplaintLoggedByPersonRepository complaintLoggedByPersonRepository;
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
    private CategoryConvertor categoryConvertor;
    @Autowired
    private AppKeyService appKeyService;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    @Qualifier("stringRedisTemplate")
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private PoliticalBodyAdminRepository politicalBodyAdminRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ComplaintCommentRepository complaintCommentRepository;
    @Autowired
    private DepartmentComplaintRepository departmentComplaintRepository;
    @Autowired
    private DepartmentLocationRepository departmentLocationRepository;

    @Autowired
    private UrlShortenService urlShortenService;
    @Value("${server_domain_and_context}")
    private String serverUrl;

    private JsonParser jsonParser = new JsonParser();

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
    public ComplaintDto saveComplaint(SaveComplaintRequestDto saveComplaintRequestDto, Long dailyUserTotalComplaints) throws ApplicationException {
        Setting setting = settingService.getSetting(SettingNames.ALLOW_COMPLAINT.getName());
        if (setting != null && setting.getValue().equalsIgnoreCase("false")) {
            throw new ApplicationException("Complaint Creation is disabled");
        }
        logger.info("Saving Complaint : {}", saveComplaintRequestDto);
		Complaint complaint = complaintConvertor.convert(saveComplaintRequestDto);
        logger.info("Converted  Complaint : {}", complaint);

        complaint.setComplaintTime(Calendar.getInstance().getTimeInMillis());
        logger.info("Searching person by User external Id : {}", saveComplaintRequestDto.getUserExternalid());
        User user = userRepository.findByPropertyValue("externalId", saveComplaintRequestDto.getUserExternalid());
        logger.info("User : {}", saveComplaintRequestDto.getUserExternalid());
        Person person = personRepository.getPersonByUser(user);

        int maxComplaintPerDayPerPerson = settingService.getMaxDailyComplaintPerUser();
        if (person.getComplaintLimit() != null) {
            maxComplaintPerDayPerPerson = person.getComplaintLimit();
        }
        if (maxComplaintPerDayPerPerson >= 0 && dailyUserTotalComplaints >= maxComplaintPerDayPerPerson) {
            throw new ApplicationException("You have reached maximum total complaint quota for the day. We have placed Maximum quota to avoid abuse of the system.");
        }

		boolean newComplaint = true;
        if (complaint.getId() != null && complaint.getId() > 0) {
            newComplaint = false;
            complaint.setDateModified(new Date());
        } else {
            complaint.setStatus(Status.Pending);
            complaint.setDateCreated(new Date());
            complaint.setDateModified(new Date());
        }
        

        updateLocationAddress(saveComplaintRequestDto, complaint);

        complaint = complaintRepository.save(complaint);
        ComplaintMessage complaintMessage = updateLocationAndAdmins(complaint);


        if (complaint.getShortUrl() == null || complaint.getShortUrl().trim().equals("")) {
            String complaintUrl = serverUrl + "/complaint/" + complaint.getId() + ".html";
            complaint.setShortUrl(urlShortenService.getShortUrl(complaintUrl));
            complaint = complaintRepository.save(complaint);
        }

        creatComplaintPersonRelation(complaint, person, saveComplaintRequestDto.isAnonymous());

        complaintMessage.setId(complaint.getId());

        if (newComplaint) {
            queueService.sendComplaintCreatedMessage(complaintMessage);
        }
		return complaintConvertor.convertBean(complaint);
	}

    private void updateLocationAddress(SaveComplaintRequestDto saveComplaintRequestDto, Complaint complaint) {
        try {
            String googleAddress = saveComplaintRequestDto.getGoogleLocationJson();
            if (!StringUtils.isEmpty(googleAddress)) {
                JsonObject jsonObject = (JsonObject) jsonParser.parse(googleAddress);
                JsonObject mAddressLinesJsonObject = jsonObject.get("mAddressLines").getAsJsonObject();
                StringBuilder sb = new StringBuilder();
                addAddress(sb, mAddressLinesJsonObject, 0);
                addAddress(sb, mAddressLinesJsonObject, 1);
                addAddress(sb, mAddressLinesJsonObject, 2);
                addAddress(sb, mAddressLinesJsonObject, 3);
                addAddress(sb, mAddressLinesJsonObject, 4);
                String locationAddress = sb.toString();
                logger.info("Complaint Address : " + locationAddress);
                complaint.setLocationAddress(locationAddress);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void addAddress(StringBuilder sb, JsonObject mAddressLinesJsonObject, int count) {
        String field = String.valueOf(count);
        if (mAddressLinesJsonObject.get(field) != null) {
            if (count > 0) {
                sb.append(", ");
            }
            sb.append(mAddressLinesJsonObject.get(field).getAsString());
        }

    }

    private void creatComplaintPersonRelation(Complaint complaint, Person person, boolean anonymous) {
        logger.info("comlaint = {}", complaint);
        logger.info("person = {}", person);
        ComplaintLoggedByPerson complaintLoggedByPerson = complaintLoggedByPersonRepository.getComplaintLoggedByPersonRelation(complaint, person);
        logger.info("complaintLoggedByPerson = {}", complaintLoggedByPerson);
        if (complaintLoggedByPerson == null) {
            complaintLoggedByPerson = new ComplaintLoggedByPerson();
            complaintLoggedByPerson.setComplaint(complaint);
            complaintLoggedByPerson.setPerson(person);
            complaintLoggedByPerson.setAnonymous(anonymous);
            complaintLoggedByPerson = complaintLoggedByPersonRepository.save(complaintLoggedByPerson);
        }
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
        complaintCreatedMessage.setComplaintTime(complaint.getComplaintTime());

        complaintCreatedMessage.setLattitude(complaint.getLattitude());
        complaintCreatedMessage.setLongitude(complaint.getLongitude());

        if (complaint.getLocations() != null && !complaint.getLocations().isEmpty()) {
            List<Long> locationIds = new ArrayList<>();
            for (Location oneLocation : complaint.getLocations()) {
                locationIds.add(oneLocation.getId());
            }
            complaintCreatedMessage.setLocationIds(locationIds);

        }


        Collection<PoliticalBodyAdmin> politicalAdmins = politicalBodyAdminRepository.getAllPoliticalAdminOfComplaint(complaint);
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

        ComplaintPhoto complaintPhoto = new ComplaintPhoto();
        complaintPhoto.setComplaint(complaint);
        complaintPhoto.setPhoto(photo);
        complaintPhoto = complaintPhotoRepository.save(complaintPhoto);
        logger.info("complaintPhoto saved : {}", complaintPhoto);

		return photoConvertor.convertBean(photo);
	}

	@Override
    public List<ComplaintDto> getPagedDeviceComplaints(String userExternalId,
			int start, int end) throws ApplicationException {
        Person person = personRepository.findByPropertyValue("externalId", userExternalId);
		List<Complaint> personComplaints = complaintRepository.getPagedComplaintsLodgedByPerson(person, start, end); 
		return complaintConvertor.convertBeanList(personComplaints);
	}

	@Override
    public List<ComplaintDto> getAllDeviceComplaints(String userExternalId)
			throws ApplicationException {
        Person person = personRepository.findByPropertyValue("externalId", userExternalId);
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
        logger.info("Get Locations for Key : " + rediskey);
        Set<String> complaintLocations = stringRedisTemplate.opsForSet().members(rediskey);
        logger.info("Founds Locations for Key : " + complaintLocations);
        if (!settingService.isAllowComplaintForUnknownLocation() && (complaintLocations == null || complaintLocations.isEmpty())) {
            throw new ApplicationException(settingService.getMessageComplaintForUnknownLocation());
        }
        Set<PoliticalBodyAdmin> politicalBodyAdmins = new HashSet<>();
        if (complaintLocations != null && !complaintLocations.isEmpty()) {

            Set<Location> locations = new HashSet<>();

            Collection<PoliticalBodyAdmin> oneLocationPoliticalBodyAdmins;

            Long locationId;
            for (String oneLocationId : complaintLocations) {
                locationId = Long.parseLong(oneLocationId);
                Location oneLocation = locationRepository.findOne(locationId);
                locations.add(oneLocation);
                addAllParentLocationsToComplaint(locations, oneLocation, complaintLocations);

            }
            Category subCategoryForComplaint = getSubCategoryOfComplaint(complaint);
            List<Department> departments = new ArrayList<Department>();
            for (Location oneLocation : locations) {
                logger.info("Searching Active Political Admin For : " + oneLocation.getId() + ", " + oneLocation.getName());
                oneLocationPoliticalBodyAdmins = politicalBodyAdminRepository.getCurrentPoliticalAdminByLocation(oneLocation);
                if (oneLocationPoliticalBodyAdmins != null && !oneLocationPoliticalBodyAdmins.isEmpty()) {
                    politicalBodyAdmins.addAll(oneLocationPoliticalBodyAdmins);
                }
                logger.info("subCategoryForComplaint  : {}" + subCategoryForComplaint);
                if (subCategoryForComplaint != null) {
                    logger.info("Searching Departments For : {}, {}" + oneLocation, subCategoryForComplaint);
                    List<Department> locationDpartments = departmentLocationRepository.getAllDepartmentOfLocationAndCategory(oneLocation, subCategoryForComplaint);
                    if (locationDpartments != null && !locationDpartments.isEmpty()) {
                        departments.addAll(locationDpartments);
                    }
                }

            }
            complaint.setLocations(locations);
            
            // find Executive Admin based on Location and Category and attach it to complaint
            addDeparmentToComplaint(departments, complaint);

        }
        complaint.setNearByKey(appKeyService.buildLocationKeyForNearByComplaints(complaint.getLattitude(), complaint.getLongitude()));
        complaint = complaintRepository.save(complaint);
        addPoliticalAdmins(complaint, politicalBodyAdmins);
        return buildComplaintMessage(complaint);
    }

    private Category getSubCategoryOfComplaint(Complaint complaint) {
        for (Category oneCategory : complaint.getCategories()) {
            if (!oneCategory.isRoot()) {
                return oneCategory;
            }
        }
        return null;
    }

    private void addDeparmentToComplaint(List<Department> departments, Complaint complaint) {
        Department lowestLevelDepartment = null;
        int lowest = 0;
        for (Department oneDepartment : departments) {
            logger.info("oneDepartment : {}", oneDepartment);
            if (oneDepartment.getLevel() > lowest) {
                lowest = oneDepartment.getLevel();
                lowestLevelDepartment = oneDepartment;
            }
        }
        if (lowestLevelDepartment != null) {
            logger.info("Creating DepartmentComplaint for Department {} and Complaint {}", lowestLevelDepartment, complaint);
            DepartmentComplaint departmentComplaint = new DepartmentComplaint(lowestLevelDepartment, complaint);
            DepartmentComplaint existingDepartmentComplaint = departmentComplaintRepository.getDepartmentComplaintRelation(complaint, lowestLevelDepartment);
            if (existingDepartmentComplaint == null) {
                // Somehow We dont need to save it as relations getting created automatically
                // and if we save manually then two relations gets created
                departmentComplaint = departmentComplaintRepository.save(departmentComplaint);
                logger.info("DepartmentComplaint Created : {}", departmentComplaint);
            }
        }
    }

    private void addPoliticalAdmins(Complaint complaint, Set<PoliticalBodyAdmin> politicalBodyAdmins) {
        if (politicalBodyAdmins.isEmpty()) {
            return;
        }
        ComplaintPoliticalAdmin complaintPoliticalAdmin;
        for (PoliticalBodyAdmin onePoliticalBodyAdmin : politicalBodyAdmins) {
            logger.info("Adding Admin {}  to complaint {}", onePoliticalBodyAdmin.getId(), complaint.getId());
            complaintPoliticalAdmin = complaintPoliticalAdminRepository.getComplaintPoliticalAdminRelation(complaint, onePoliticalBodyAdmin);
            if (complaintPoliticalAdmin == null) {
                complaintPoliticalAdmin = new ComplaintPoliticalAdmin();
                complaintPoliticalAdmin.setComplaint(complaint);
                complaintPoliticalAdmin.setPoliticalBodyAdmin(onePoliticalBodyAdmin);
                complaintPoliticalAdmin.setStatus(PoliticalAdminComplaintStatus.Pending);
                complaintPoliticalAdmin.setViewed(false);
                complaintPoliticalAdmin = complaintPoliticalAdminRepository.save(complaintPoliticalAdmin);
            }
        }
    }

    private void addAllParentLocationsToComplaint(Set<Location> locations, Location location, Set<String> complaintLocations) {
        if (location.getParentLocation() == null) {
            logger.debug("No Parent for location {}", location.getId());
            return;
        }
        logger.debug("Parent for location {} is {} ", location.getId(), location.getParentLocation());
        if (complaintLocations.contains(String.valueOf(location.getParentLocation().getId()))) {
            logger.debug("Not processing Parent location {}", location.getParentLocation().getId());
            return;// No need to do naything
        }
        logger.debug("Find location with Id {}", location.getParentLocation().getId());
        Location location2 = locationRepository.findOne(location.getParentLocation().getId());
        logger.debug("Found : {}", location2);
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

    @Override
    public List<PoliticalAdminComplaintDto> getAllComplaintsOfPoliticalAdmin(Long politicalAdminId, Long start, Long totalComplaints) throws ApplicationException {
        PoliticalBodyAdmin politicalBodyAdmin = new PoliticalBodyAdmin();
        politicalBodyAdmin.setId(politicalAdminId);
        List<Complaint> complaints = complaintRepository.getPagedComplaintsOfPoliticalAdmin(politicalAdminId, start, totalComplaints);
        return convertComplaintList(complaints, politicalBodyAdmin);
    }

    private List<PoliticalAdminComplaintDto> convertComplaintList(List<Complaint> complaints, PoliticalBodyAdmin politicalBodyAdmin) throws ApplicationException {
        List<PoliticalAdminComplaintDto> politlcalAdminComplaintDtos = new ArrayList<>();
        for (Complaint oneComplaint : complaints) {
            ComplaintPoliticalAdmin complaintPoliticalAdmin = complaintPoliticalAdminRepository.getComplaintPoliticalAdminRelation(oneComplaint, politicalBodyAdmin);
            PoliticalAdminComplaintDto onePolitlcalAdminComplaintDto = buildPoliticalAdminComplaint(oneComplaint, complaintPoliticalAdmin);
            addPersons(oneComplaint, onePolitlcalAdminComplaintDto);
            addImages(oneComplaint, onePolitlcalAdminComplaintDto);
            politlcalAdminComplaintDtos.add(onePolitlcalAdminComplaintDto);
        }
        return politlcalAdminComplaintDtos;

    }

    @Override
    public List<PoliticalAdminComplaintDto> getAllComplaintsOfPoliticalAdminAndCategory(Long politicalAdminId, Long categoryId, Long start, Long totalComplaints) throws ApplicationException {
        PoliticalBodyAdmin politicalBodyAdmin = new PoliticalBodyAdmin();
        politicalBodyAdmin.setId(politicalAdminId);
        List<Complaint> complaints = complaintRepository.getAllPagedComplaintsOfPoliticalAdminAndCategry(politicalAdminId, categoryId, start, totalComplaints);
        return convertComplaintList(complaints, politicalBodyAdmin);
    }

    private void addPersons(Complaint complaint, PoliticalAdminComplaintDto politicalAdminComplaintDto) throws ApplicationException {
        Collection<Person> persons = personRepository.getPersonsLoggedComplaint(complaint);
        List<PersonDto> personDtos = personConvertor.convertBeanList(persons);
        politicalAdminComplaintDto.setCreatedByPersons(personDtos);
    }

    private void addImages(Complaint complaint, PoliticalAdminComplaintDto politicalAdminComplaintDto) throws ApplicationException {
        Collection<Photo> photos = photoRepository.getComplaintPhotos(complaint);
        List<PhotoDto> images = photoConvertor.convertBeanList(photos);
        politicalAdminComplaintDto.setImages(images);
    }

    private PoliticalAdminComplaintDto buildPoliticalAdminComplaint(Complaint complaint, ComplaintPoliticalAdmin complaintPoliticalAdmin) throws ApplicationException {
        ComplaintDto complaintDto = complaintConvertor.convertBean(complaint);
        PoliticalAdminComplaintDto onePolitlcalAdminComplaintDto = new PoliticalAdminComplaintDto();
        BeanUtils.copyProperties(complaintDto, onePolitlcalAdminComplaintDto);

        onePolitlcalAdminComplaintDto.setCategories(categoryConvertor.convertBeanList(categoryRepository.getCategoriesOfComplaints(complaint)));
        onePolitlcalAdminComplaintDto.setViewed(complaintPoliticalAdmin.isViewed());
        onePolitlcalAdminComplaintDto.setViewDate(complaintPoliticalAdmin.getViewDate());
        onePolitlcalAdminComplaintDto.setPoliticalAdminComplaintStatus(complaintPoliticalAdmin.getStatus().name());
        return onePolitlcalAdminComplaintDto;
    }

    @Override
    public PoliticalAdminComplaintDto updateComplaintViewStatus(ComplaintViewdByPoliticalAdminRequestDto complaintViewdByPoliticalAdminRequestDto) throws ApplicationException {
        PoliticalBodyAdmin politicalBodyAdmin = politicalBodyAdminRepository.findOne(complaintViewdByPoliticalAdminRequestDto.getPoliticalAdminId());
        Complaint complaint = complaintRepository.findOne(complaintViewdByPoliticalAdminRequestDto.getComplaintId());
        ComplaintPoliticalAdmin complaintPoliticalAdmin = complaintPoliticalAdminRepository.getComplaintPoliticalAdminRelation(complaint, politicalBodyAdmin);
        if (!complaintPoliticalAdmin.isViewed()) {
            complaintPoliticalAdmin.setViewed(true);
            complaintPoliticalAdmin.setViewDate(new Date());
            complaintPoliticalAdmin.setStatus(PoliticalAdminComplaintStatus.Viewed);
            complaintPoliticalAdmin = complaintPoliticalAdminRepository.save(complaintPoliticalAdmin);
        }

        return buildPoliticalAdminComplaint(complaint, complaintPoliticalAdmin);
    }

    @Override
    public void mergeComplaints(List<Long> complaintIds) throws ApplicationException {
        if (complaintIds == null || complaintIds.isEmpty() || complaintIds.size() == 1) {
            return;
        }
        Iterable<Complaint> complaints = complaintRepository.findAll(complaintIds);
        
        //Now find a complaint which already have merged status and use that as base complaint.
        Complaint mainComplaint = null;
        for (Complaint oneComplaint : complaints) {
            if (oneComplaint.getStatus().equals(Status.Merged)) {
                mainComplaint = oneComplaint;
                break;
            }
        }
        if (mainComplaint == null) {
            mainComplaint = complaints.iterator().next();
        }

        // Now start merging all complaints into main complaint
        for (Complaint oneComplaint : complaints) {
            mergeComplaint(mainComplaint, oneComplaint);
        }

    }

    private void mergeComplaint(Complaint mainComplaint, Complaint complaintToMerge) {
        if (mainComplaint.getId().equals(complaintToMerge.getId())) {
            return;
        }
        throw new RuntimeException("Merge is not implemented yet");
        // Merge
    }

    @Override
    public CommentSaveResponseDto commentOnComplaint(CommentSaveRequestDto commentRequestDto) throws ApplicationException {

        Person person = personRepository.findOne(commentRequestDto.getPersonId());

        Comment comment = new Comment();
        comment.setText(commentRequestDto.getCommentText());
        comment.setDateCreated(new Date());
        comment.setDateModified(new Date());
        comment.setCreationTime(new Date());

        comment.setCreatedBy(person);
        comment.setCreationTime(new Date());
        if (commentRequestDto.getPoliticalAdminId() != null && commentRequestDto.getPoliticalAdminId() > 0) {
            PoliticalBodyAdmin politicalBodyAdmin = politicalBodyAdminRepository.findOne(commentRequestDto.getPoliticalAdminId());
            comment.setPoliticalBodyAdmin(politicalBodyAdmin);
        }
        comment = commentRepository.save(comment);

        // Create relation between comment and complaint
        Complaint complaint = complaintRepository.findOne(commentRequestDto.getComplaintId());
        ComplaintComment complaintComment = new ComplaintComment();
        complaintComment.setComment(comment);
        complaintComment.setComplaint(complaint);
        complaintComment = complaintCommentRepository.save(complaintComment);

        CommentSaveResponseDto commentSaveResponseDto = new CommentSaveResponseDto();
        BeanUtils.copyProperties(commentRequestDto, commentSaveResponseDto);
        commentSaveResponseDto.setId(comment.getId());
        return commentSaveResponseDto;
    }

    @Override
    public PoliticalAdminComplaintDto updateComplaintPoliticalAdminStatus(ComplaintStatusChangeByPoliticalAdminRequestDto complaintStatusChangeByPoliticalAdminRequestDto) throws ApplicationException {
        Complaint complaint = complaintRepository.findOne(complaintStatusChangeByPoliticalAdminRequestDto.getComplaintId());
        PoliticalBodyAdmin politicalBodyAdmin = politicalBodyAdminRepository.findOne(complaintStatusChangeByPoliticalAdminRequestDto.getPoliticalAdminId());

        ComplaintPoliticalAdmin complaintPoliticalAdmin = complaintPoliticalAdminRepository.getComplaintPoliticalAdminRelation(complaint, politicalBodyAdmin);
        if (complaintPoliticalAdmin == null) {
            throw new ApplicationException("You can not update this complaint status, as its not assigned to you");
        }

        complaintPoliticalAdmin.setStatus(PoliticalAdminComplaintStatus.valueOf(complaintStatusChangeByPoliticalAdminRequestDto.getStatus()));
        complaintPoliticalAdmin = complaintPoliticalAdminRepository.save(complaintPoliticalAdmin);

        return buildPoliticalAdminComplaint(complaint, complaintPoliticalAdmin);
    }

    @Override
    public ComplaintDto updateComplaintPersonStatus(ComplaintStatusChangeByPersonRequestDto complaintStatusChangeByPersonRequestDto) throws ApplicationException {
        Complaint complaint = complaintRepository.findOne(complaintStatusChangeByPersonRequestDto.getComplaintId());
        Person person = personRepository.findOne(complaintStatusChangeByPersonRequestDto.getPersonId());

        ComplaintLoggedByPerson complaintPoliticalAdmin = complaintLoggedByPersonRepository.getComplaintLoggedByPersonRelation(complaint, person);
        if (complaintPoliticalAdmin == null) {
            throw new ApplicationException("You can not update this complaint status");
        }

        complaint.setStatus(Complaint.Status.valueOf(complaintStatusChangeByPersonRequestDto.getStatus()));
        complaint = complaintRepository.save(complaint);
        ComplaintMessage complaintMessage = buildComplaintMessage(complaint);
        queueService.sendComplaintCreatedMessage(complaintMessage);
        return complaintConvertor.convertBean(complaint);
    }

    @Override
    public List<PhotoDto> getComplaintPhotos(Long complaintId) throws ApplicationException {
        List<Photo> photos = photoRepository.getComplaintPhotos(complaintId);
        logger.info("complaintPhotos : {}", photos);
        return photoConvertor.convertBeanList(photos);
    }

    @Override
    public List<CommentComplaintDto> getCodmplaintComments(int page, int count) throws ApplicationException {
        Pageable pageable = new PageRequest(page, count, Sort.Direction.DESC, "id");
        Page<ComplaintComment> complaintComments = complaintCommentRepository.findAll(pageable);
        
        List<CommentComplaintDto> list = new ArrayList<>();
        if (complaintComments.getContent() != null && !complaintComments.getContent().isEmpty()) {
            for (ComplaintComment oneCommentComplaint : complaintComments.getContent()) {
                CommentComplaintDto oneCommentComplaintDto = new CommentComplaintDto();
                oneCommentComplaintDto.setCommentId(oneCommentComplaint.getComment().getId());
                oneCommentComplaintDto.setComplaintId(oneCommentComplaint.getComplaint().getId());
                list.add(oneCommentComplaintDto);
            }
        }
        logger.info("list : {}", list);
        return list;
    }

}
