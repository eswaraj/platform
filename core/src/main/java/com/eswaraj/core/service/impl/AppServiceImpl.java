package com.eswaraj.core.service.impl;

import java.io.IOException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.conversion.EndResult;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.eswaraj.core.convertors.CategoryConvertor;
import com.eswaraj.core.convertors.DepartmentConvertor;
import com.eswaraj.core.convertors.ExecutiveBodyAdminConvertor;
import com.eswaraj.core.convertors.ExecutiveBodyConvertor;
import com.eswaraj.core.convertors.ExecutivePostConvertor;
import com.eswaraj.core.convertors.PartyConvertor;
import com.eswaraj.core.convertors.PoliticalBodyAdminConvertor;
import com.eswaraj.core.convertors.PoliticalBodyTypeConvertor;
import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.AppService;
import com.eswaraj.core.util.DateUtil;
import com.eswaraj.domain.nodes.Category;
import com.eswaraj.domain.nodes.Department;
import com.eswaraj.domain.nodes.ExecutiveBody;
import com.eswaraj.domain.nodes.ExecutiveBodyAdmin;
import com.eswaraj.domain.nodes.ExecutivePost;
import com.eswaraj.domain.nodes.Location;
import com.eswaraj.domain.nodes.Party;
import com.eswaraj.domain.nodes.Person;
import com.eswaraj.domain.nodes.PoliticalBodyAdmin;
import com.eswaraj.domain.nodes.PoliticalBodyType;
import com.eswaraj.domain.repo.CategoryRepository;
import com.eswaraj.domain.repo.DepartmentRepository;
import com.eswaraj.domain.repo.ExecutiveBodyAdminRepository;
import com.eswaraj.domain.repo.ExecutiveBodyRepository;
import com.eswaraj.domain.repo.ExecutivePostRepository;
import com.eswaraj.domain.repo.LocationRepository;
import com.eswaraj.domain.repo.PartyRepository;
import com.eswaraj.domain.repo.PersonRepository;
import com.eswaraj.domain.repo.PoliticalBodyAdminRepository;
import com.eswaraj.domain.repo.PoliticalBodyTypeRepository;
import com.eswaraj.domain.validator.exception.ValidationException;
import com.eswaraj.queue.service.QueueService;
import com.eswaraj.web.dto.CategoryDto;
import com.eswaraj.web.dto.CategoryWithChildCategoryDto;
import com.eswaraj.web.dto.DepartmentDto;
import com.eswaraj.web.dto.ExecutiveBodyAdminDto;
import com.eswaraj.web.dto.ExecutiveBodyDto;
import com.eswaraj.web.dto.ExecutivePostDto;
import com.eswaraj.web.dto.PartyDto;
import com.eswaraj.web.dto.PoliticalBodyAdminDto;
import com.eswaraj.web.dto.PoliticalBodyTypeDto;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Component
@Transactional
public class AppServiceImpl extends BaseService implements AppService {

    private static final long serialVersionUID = 1L;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private CategoryConvertor categoryConvertor;
	@Autowired
	private PoliticalBodyTypeRepository politicalBodyTypeRepository;
	@Autowired
	private PoliticalBodyTypeConvertor politicalBodyTypeConvertor;
	@Autowired
	private PartyRepository partyRepository;
	@Autowired
	private PartyConvertor partyConvertor;
	@Autowired
	private PoliticalBodyAdminRepository politicalBodyAdminRepository;
	@Autowired
	private PoliticalBodyAdminConvertor politicalBodyAdminConvertor;
	@Autowired
	private LocationRepository locationRepository;
	@Autowired
	private ExecutiveBodyRepository executiveBodyRepository;
	@Autowired
	private ExecutiveBodyConvertor executiveBodyConvertor;
	@Autowired
	private ExecutiveBodyAdminRepository executiveBodyAdminRepository;
	@Autowired
	private ExecutiveBodyAdminConvertor executiveBodyAdminConvertor;
	@Autowired
	private ExecutivePostRepository executivePostRepository;
	@Autowired
	private ExecutivePostConvertor executivePostConvertor;
	@Autowired
	private DepartmentRepository departmentRepository;
	@Autowired
	private DepartmentConvertor departmentConvertor;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private QueueService queueService;
	
	@Override
	public CategoryDto saveCategory(CategoryDto categoryDto) throws ApplicationException {
		Category category = categoryConvertor.convert(categoryDto);
		category = categoryRepository.save(category);
		return categoryConvertor.convertBean(category);
	}

	@Override
	public CategoryDto getCategoryById(long categoryId) throws ApplicationException {
		Category category = categoryRepository.findOne(categoryId);
		return categoryConvertor.convertBean(category);
	}

	@Override
	public List<CategoryDto> getAllRootCategories() throws ApplicationException {
		Collection<Category> rootCategories = categoryRepository.getAllRootCategories();
		return categoryConvertor.convertBeanList(rootCategories);
	}

	@Override
	public List<CategoryDto> getAllChildCategoryOfParentCategory(long parentCategoryId) throws ApplicationException {
		Category parentCategory = getObjectIfExistsElseThrowExcetpion(parentCategoryId, "Parent Catgeory", categoryRepository);
		Collection<Category> rootCategories = categoryRepository.findAllChildCategoryOfParentCategory(parentCategory);
		return categoryConvertor.convertBeanList(rootCategories);
	}
	
	@Override
	public List<CategoryWithChildCategoryDto> getAllCategories() throws ApplicationException {
		EndResult<Category> allCategories = categoryRepository.findAll();
		allCategories.finish();
		return categoryConvertor.convertCategoryWithChildren(allCategories);
	}

	@Override
	public PoliticalBodyTypeDto savePoliticalBodyType(PoliticalBodyTypeDto politicalBodyTypeDto) throws ApplicationException {
		PoliticalBodyType politicalBodyType = politicalBodyTypeConvertor.convert(politicalBodyTypeDto);
		politicalBodyType = politicalBodyTypeRepository.save(politicalBodyType);
		return politicalBodyTypeConvertor.convertBean(politicalBodyType);
	}

	@Override
	public PoliticalBodyTypeDto getPoliticalBodyTypeById(Long politicalBodyTypeId) throws ApplicationException {
		PoliticalBodyType politicalBodyType = politicalBodyTypeRepository.findOne(politicalBodyTypeId);
		return politicalBodyTypeConvertor.convertBean(politicalBodyType);
	}

	@Override
	public List<PoliticalBodyTypeDto> getAllPoliticalBodyTypes() throws ApplicationException {
		EndResult<PoliticalBodyType> allPoliticalBodyTypesFromDb = politicalBodyTypeRepository.findAll();
		return politicalBodyTypeConvertor.convertBeanList(allPoliticalBodyTypesFromDb);
	}

	@Override
	public PartyDto saveParty(PartyDto partyDto) throws ApplicationException {
		Party party = partyConvertor.convert(partyDto);
		party = partyRepository.save(party);
		return partyConvertor.convertBean(party);
	}

	@Override
	public PartyDto getPartyById(Long partyId) throws ApplicationException {
		Party party = partyRepository.findOne(partyId);
		return partyConvertor.convertBean(party);
	}

	@Override
	public List<PartyDto> getAllPoliticalParties() throws ApplicationException {
		EndResult<Party> result = partyRepository.findAll();
		return partyConvertor.convertBeanList(result);
	}

	@Override
	public PoliticalBodyAdminDto savePoliticalBodyAdmin(PoliticalBodyAdminDto politicalBodyAdminDto) throws ApplicationException {
		PoliticalBodyAdmin politicalBodyAdmin = politicalBodyAdminConvertor.convert(politicalBodyAdminDto);
        politicalBodyAdmin.setActive(isActive(politicalBodyAdmin));
        politicalBodyAdmin.setUrlIdentifier("leader/" + politicalBodyAdmin.getLocation().getUrlIdentifier() + "/" + politicalBodyAdmin.getPoliticalBodyType().getShortName().toLowerCase());
		validateWithExistingData(politicalBodyAdmin);
        validateLocation(politicalBodyAdmin);
		politicalBodyAdmin = politicalBodyAdminRepository.save(politicalBodyAdmin);
        // Send message to update Location Info
        queueService.sendPoliticalBodyAdminUpdateMessage(politicalBodyAdmin.getLocation().getId(), politicalBodyAdmin.getId());
		return politicalBodyAdminConvertor.convertBean(politicalBodyAdmin);	
	}

    private boolean isActive(PoliticalBodyAdmin politicalBodyAdmin) {
        if (politicalBodyAdmin.getStartDate() == null) {
            throw new ValidationException("Start date can not be null");
        }
        boolean active = false;
        Calendar startDate = Calendar.getInstance();
        startDate.setTime(politicalBodyAdmin.getStartDate());
        startDate = DateUtil.getStartOfDay(startDate);
        Calendar today = Calendar.getInstance();
        if (today.after(startDate) && (politicalBodyAdmin.getEndDate() == null || politicalBodyAdmin.getEndDate().after(today.getTime()))) {
            active = true;
        }
        return active;
    }
	private void validateWithExistingData(PoliticalBodyAdmin politicalBodyAdmin) throws ApplicationException{
		if(politicalBodyAdmin.getLocation() != null && politicalBodyAdmin.getPoliticalBodyType() != null){
			Collection<PoliticalBodyAdmin> allPoliticalBodyAdminsForLocation = politicalBodyAdminRepository.getAllPoliticalAdminByLocationAndPoliticalBodyType(politicalBodyAdmin.getLocation(), politicalBodyAdmin.getPoliticalBodyType());
			adjustActivePoliticalAdminForLocation(politicalBodyAdmin, allPoliticalBodyAdminsForLocation);
			checkForDateOverlap(politicalBodyAdmin, allPoliticalBodyAdminsForLocation);
		}
	}

    private void validateLocation(PoliticalBodyAdmin politicalBodyAdmin) throws ApplicationException {
        if (politicalBodyAdmin.getLocation() != null && politicalBodyAdmin.getPoliticalBodyType() != null) {
            logger.info("checking if LocationTuypes are same for loxation {} and admin {}", politicalBodyAdmin.getLocation(), politicalBodyAdmin);
            if (!politicalBodyAdmin.getLocation().getLocationType().getId().equals(politicalBodyAdmin.getPoliticalBodyType().getLocationType().getId())) {
                throw new ApplicationException("You can not create political Admin of type [" + politicalBodyAdmin.getPoliticalBodyType().getName() + "] at location ["
                        + politicalBodyAdmin.getLocation().getName() + "," + politicalBodyAdmin.getLocation().getId() + "]");
            }
        }
    }
	private void checkForDateOverlap(PoliticalBodyAdmin politicalBodyAdmin, Collection<PoliticalBodyAdmin> allPoliticalBodyAdminsForLocation) throws ApplicationException{
		for(PoliticalBodyAdmin onePoliticalBodyAdmin : allPoliticalBodyAdminsForLocation){
            if (!onePoliticalBodyAdmin.getId().equals(politicalBodyAdmin.getId())) {
				//We need to check political admin being saved with other admins only
				if(checkIfDatesAreOverlapped(onePoliticalBodyAdmin.getStartDate(), onePoliticalBodyAdmin.getEndDate(), politicalBodyAdmin.getStartDate(), politicalBodyAdmin.getEndDate())){
					throw new ApplicationException("Start date and end dates of two Political admin for this location overallped [id1="+onePoliticalBodyAdmin.getId()+", startDate="+onePoliticalBodyAdmin.getStartDate()
							+", endDate="+onePoliticalBodyAdmin.getEndDate()+"] and [id2="+politicalBodyAdmin.getId()+", startDat="+politicalBodyAdmin.getStartDate()+", endDate="+politicalBodyAdmin.getEndDate());
				}
			}
		}
	}
	
	private boolean checkIfDatesAreOverlapped(Date startDate1, Date endDate1, Date startDate2, Date endDate2){
		if(endDate1 == null && endDate2 == null){
			return true;
		}
		if(endDate2 == null && (startDate1.after(startDate2) ||endDate1.after(startDate2))){
			return true;
		}
		if(endDate1 == null && (startDate2.after(startDate1) ||endDate2.after(startDate1))){
			return true;
		}
		if(endDate1 != null && endDate2 != null && ((startDate1.after(startDate2) && startDate1.before(endDate2)))
				|| (endDate1.after(startDate2) && endDate1.before(endDate2)) || (endDate2.after(startDate1) && endDate2.before(endDate1))
				|| (startDate2.after(startDate1) && startDate2.before(endDate1))){
			return true;
		}

		return false;
	}
	private void adjustActivePoliticalAdminForLocation(PoliticalBodyAdmin politicalBodyAdmin, Collection<PoliticalBodyAdmin> allPoliticalBodyAdminsForLocation) throws ApplicationException{
		if(!politicalBodyAdmin.isActive()){
			//if this is not active just go back
			return;
		}
		
		for(PoliticalBodyAdmin onePoliticalBodyAdmin : allPoliticalBodyAdminsForLocation){
            if (!onePoliticalBodyAdmin.getId().equals(politicalBodyAdmin.getId())) {
				if(onePoliticalBodyAdmin.isActive()){
					//throw new ApplicationException("Another Active Political Admin exists [id="+onePoliticalBodyAdmin.getId()+"], please make him/her inactive first and then make this active");
					//instead of throwing exception we are just turning other active Admin to inactive
					onePoliticalBodyAdmin.setActive(false);
					politicalBodyAdminRepository.save(onePoliticalBodyAdmin);
				}
				
			}
		}
	}

	@Override
	public PoliticalBodyAdminDto getPoliticalBodyAdminById(Long politicalBodyAdminId) throws ApplicationException {
		PoliticalBodyAdmin politicalBodyAdmin = politicalBodyAdminRepository.findOne(politicalBodyAdminId);
		return politicalBodyAdminConvertor.convertBean(politicalBodyAdmin);
	}

	@Override
	public PoliticalBodyAdminDto getCurrentPoliticalBodyAdminByLocationId(Long locationId, Long pbTypeId) throws ApplicationException {
		Location location = getObjectIfExistsElseThrowExcetpion(locationId, "Location", locationRepository);
		PoliticalBodyType politicalBodyType = getObjectIfExistsElseThrowExcetpion(pbTypeId, "PoliticalBodyType", politicalBodyTypeRepository);
		PoliticalBodyAdmin politicalBodyAdmin = politicalBodyAdminRepository.getCurrentPoliticalAdminByLocationAndPoliticalBodyType(location, politicalBodyType);
		return politicalBodyAdminConvertor.convertBean(politicalBodyAdmin);
	}

	@Override
	public List<PoliticalBodyAdminDto> getAllPoliticalBodyAdminByLocationId(Long locationId, Long pbTypeId) throws ApplicationException {
		Location location = getObjectIfExistsElseThrowExcetpion(locationId, "Location", locationRepository);
		PoliticalBodyType politicalBodyType = getObjectIfExistsElseThrowExcetpion(pbTypeId, "PoliticalBodyType", politicalBodyTypeRepository);
		Collection<PoliticalBodyAdmin> politicalBodyAdmins = politicalBodyAdminRepository.getAllPoliticalAdminByLocationAndPoliticalBodyType(location, politicalBodyType);
		return politicalBodyAdminConvertor.convertBeanList(politicalBodyAdmins);
	}

	@Override
	public ExecutiveBodyDto saveExecutiveBody(ExecutiveBodyDto executiveBodyDto) throws ApplicationException {
		ExecutiveBody executiveBody = executiveBodyConvertor.convert(executiveBodyDto);
		executiveBody = executiveBodyRepository.save(executiveBody);
		return executiveBodyConvertor.convertBean(executiveBody);
	}

	@Override
	public ExecutiveBodyDto getExecutiveBodyById(Long executiveBodyId) throws ApplicationException {
		ExecutiveBody executiveBody = executiveBodyRepository.findOne(executiveBodyId);
		return executiveBodyConvertor.convertBean(executiveBody);
	}

	@Override
	public List<ExecutiveBodyDto> getAllChildExecutiveBodyOfParent(Long parentExecutiveBodyId) throws ApplicationException {
		ExecutiveBody parentExecutiveBody = getObjectIfExistsElseThrowExcetpion(parentExecutiveBodyId, "Parent ExecutiveBody", executiveBodyRepository);
		Collection<ExecutiveBody> allChildExecutiveBodies = executiveBodyRepository.getChildExecutiveBodiesByParent(parentExecutiveBody);
		return executiveBodyConvertor.convertBeanList(allChildExecutiveBodies);
	}

	@Override
	public List<ExecutiveBodyDto> getAllRootExecutiveBodyOfDepartment(Long departmentId) throws ApplicationException {
		Department department = getObjectIfExistsElseThrowExcetpion(departmentId, "Department", departmentRepository);
		Collection<ExecutiveBody> allChildExecutiveBodies = executiveBodyRepository.getAllRootExecutiveBodyOfDepartment(department);
		return executiveBodyConvertor.convertBeanList(allChildExecutiveBodies);
	}

	@Override
	public ExecutiveBodyAdminDto saveExecutiveBodyAdmin(ExecutiveBodyAdminDto executiveBodyAdminDto) throws ApplicationException {
		ExecutiveBodyAdmin executiveBodyAdmin = executiveBodyAdminConvertor.convert(executiveBodyAdminDto);
		executiveBodyAdmin = executiveBodyAdminRepository.save(executiveBodyAdmin);
		return executiveBodyAdminConvertor.convertBean(executiveBodyAdmin);
	}

	@Override
	public ExecutiveBodyAdminDto getExecutiveBodyAdminById(Long executiveBodyAdminId) throws ApplicationException {
		ExecutiveBodyAdmin executiveBodyAdmin = executiveBodyAdminRepository.findOne(executiveBodyAdminId);
		return executiveBodyAdminConvertor.convertBean(executiveBodyAdmin);
	}

	@Override
	public List<ExecutiveBodyAdminDto> getAllExecutiveBodyAdminOfExecutiveBody(Long executiveBodyId) throws ApplicationException {
		ExecutiveBody executiveBody = getObjectIfExistsElseThrowExcetpion(executiveBodyId, "ExecutiveBody", executiveBodyRepository);
		Collection<ExecutiveBodyAdmin> executiveBodyAdmins = executiveBodyAdminRepository.getAllAdminsOfExecutiveBody(executiveBody);
		return executiveBodyAdminConvertor.convertBeanList(executiveBodyAdmins);
	}
	
	@Override
	public ExecutivePostDto saveExecutivePost(ExecutivePostDto executivePostDto) throws ApplicationException {
		ExecutivePost executivePost = executivePostConvertor.convert(executivePostDto);
		executivePost = executivePostRepository.save(executivePost);
		return executivePostConvertor.convertBean(executivePost);
	}

	@Override
	public ExecutivePostDto getExecutivePostById(Long executivePostId) throws ApplicationException {
		ExecutivePost executivePost = executivePostRepository.findOne(executivePostId);
		return executivePostConvertor.convertBean(executivePost);
	}

	@Override
	public DepartmentDto saveDepartment(DepartmentDto departmentDto) throws ApplicationException {
		Department department = departmentConvertor.convert(departmentDto);
		department = departmentRepository.save(department);
		return departmentConvertor.convertBean(department);
	}

	@Override
	public DepartmentDto getDepartmentById(Long departmentId) throws ApplicationException {
		Department department = departmentRepository.findOne(departmentId);
		return departmentConvertor.convertBean(department);
	}

	@Override
	public List<DepartmentDto> getAllDepartmentsOfCategory(long categoryId) throws ApplicationException {
		Category category = getObjectIfExistsElseThrowExcetpion(categoryId, "Category", categoryRepository);
		Collection<Department> departments = departmentRepository.getAllDepartmentsOfCategory(category);
		return departmentConvertor.convertBeanList(departments);
	}

    @Override
    public void initializeData() throws ApplicationException {
        try {
            loadAllcategories();
        } catch (Exception ex) {
            throw new ApplicationException(ex);
        }
    }

    private void loadAllcategories() throws IOException {
        String categoryJson = readFile("/data/category.json");
        JsonArray jsonArray = (JsonArray) new JsonParser().parse(categoryJson);
        Category category;
        for(int i=0;i<jsonArray.size();i++){
            JsonObject jsonObject = (JsonObject) jsonArray.get(i);
            category = createCategory(jsonObject, null);
            JsonArray childArray = (JsonArray) jsonObject.get("childCategories");
            if (childArray != null) {
                for (int j = 0; j < childArray.size(); j++) {
                    JsonObject jsonChildObject = (JsonObject) childArray.get(j);
                    createCategory(jsonChildObject, category);
                }
            }
        }
    }

    private Category createCategory(JsonObject jsonObject, Category parentCategory) {
        Category category = new Category();
        category.setDescription(jsonObject.get("description").getAsString());
        category.setHeaderImageUrl(jsonObject.get("headerImageUrl").getAsString());
        category.setImageUrl(jsonObject.get("imageUrl").getAsString());
        category.setName(jsonObject.get("name").getAsString());
        category.setParentCategory(parentCategory);
        if (parentCategory == null) {
            category.setRoot(true);
        } else {
            category.setRoot(false);
        }
        category.setVideoUrl(jsonObject.get("videoUrl").getAsString());
        category = categoryRepository.save(category);
        return category;
    }

    @Override
    public List<PoliticalBodyAdminDto> getAllPoliticalBodyAdminHistoryByPersonId(Long personId) throws ApplicationException {
        Person person = getObjectIfExistsElseThrowExcetpion(personId, "Location", personRepository);
        Collection<PoliticalBodyAdmin> politicalBodyAdmins = politicalBodyAdminRepository.getPoliticalAdminHistoryByPerson(person);
        return politicalBodyAdminConvertor.convertBeanList(politicalBodyAdmins);
    }

    @Override
    public void updateAllUrls() throws ApplicationException {
        EndResult<Category> categoryResultSet = categoryRepository.findAll();
        try {
            String urlIdentifier;
            for (Category oneCategory : categoryResultSet) {
                urlIdentifier = oneCategory.getName().toLowerCase();
                urlIdentifier = urlIdentifier.replace("&", "");
                urlIdentifier = urlIdentifier.replace(" ", "-");
                oneCategory.setUrlIdentifier(urlIdentifier);
                logger.info("updaing Category : {}", oneCategory);
                categoryRepository.save(oneCategory);
            }
        } finally {
            try {
                categoryResultSet.finish();
            } catch (Exception ex) {

            }
        }

    }

}
