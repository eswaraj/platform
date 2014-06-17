package com.eswaraj.core.service.impl;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.conversion.EndResult;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.eswaraj.core.convertors.CategoryConvertor;
import com.eswaraj.core.convertors.PartyConvertor;
import com.eswaraj.core.convertors.PoliticalBodyAdminConvertor;
import com.eswaraj.core.convertors.PoliticalBodyTypeConvertor;
import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.AppService;
import com.eswaraj.domain.nodes.Category;
import com.eswaraj.domain.nodes.Location;
import com.eswaraj.domain.nodes.Party;
import com.eswaraj.domain.nodes.PoliticalBodyAdmin;
import com.eswaraj.domain.nodes.PoliticalBodyType;
import com.eswaraj.domain.repo.CategoryRepository;
import com.eswaraj.domain.repo.LocationRepository;
import com.eswaraj.domain.repo.PartyRepository;
import com.eswaraj.domain.repo.PoliticalBodyAdminRepository;
import com.eswaraj.domain.repo.PoliticalBodyTypeRepository;
import com.eswaraj.web.dto.CategoryDto;
import com.eswaraj.web.dto.PartyDto;
import com.eswaraj.web.dto.PoliticalBodyAdminDto;
import com.eswaraj.web.dto.PoliticalBodyTypeDto;

@Component
@Transactional
public class AppServiceImpl implements AppService {

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
		Category parentCategory = categoryRepository.findOne(parentCategoryId);
		if(parentCategory == null){
			throw new ApplicationException("No such location exists [id="+parentCategoryId+"]");
		}
		Collection<Category> rootCategories = categoryRepository.findAllChildCategoryOfParentCategory(parentCategory);
		return categoryConvertor.convertBeanList(rootCategories);
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
		validateWithExistingData(politicalBodyAdmin);
		politicalBodyAdmin = politicalBodyAdminRepository.save(politicalBodyAdmin);
		return politicalBodyAdminConvertor.convertBean(politicalBodyAdmin);	
	}
	private void validateWithExistingData(PoliticalBodyAdmin politicalBodyAdmin) throws ApplicationException{
		if(politicalBodyAdmin.getLocation() != null){
			Collection<PoliticalBodyAdmin> allPoliticalBodyAdminsForLocation = politicalBodyAdminRepository.getAllPoliticalAdminByLocation(politicalBodyAdmin.getLocation());
			adjustActivePoliticalAdminForLocation(politicalBodyAdmin, allPoliticalBodyAdminsForLocation);
			checkForDateOverlap(politicalBodyAdmin, allPoliticalBodyAdminsForLocation);
		}
	}
	private void checkForDateOverlap(PoliticalBodyAdmin politicalBodyAdmin, Collection<PoliticalBodyAdmin> allPoliticalBodyAdminsForLocation) throws ApplicationException{
		for(PoliticalBodyAdmin onePoliticalBodyAdmin : allPoliticalBodyAdminsForLocation){
			if(!onePoliticalBodyAdmin.getId().equals(politicalBodyAdmin)){
				//We need to check political admin being saved with other admins only
				if(checkIfDatesAreOverlapped(onePoliticalBodyAdmin.getStartDate(), onePoliticalBodyAdmin.getEndDate(), politicalBodyAdmin.getStartDate(), politicalBodyAdmin.getEndDate())){
					throw new ApplicationException("Start date and end dates of two Political admin for this location overallped [id1="+onePoliticalBodyAdmin.getId()+", startDate="+onePoliticalBodyAdmin.getStartDate()
							+", endDate="+onePoliticalBodyAdmin.getEndDate()+"] and [id2="+politicalBodyAdmin.getId()+", startDat="+politicalBodyAdmin.getStartDate()+", endDate="+politicalBodyAdmin.getEndDate());
				}
			}
		}
	}
	
	private boolean checkIfDatesAreOverlapped(Date startDate1, Date endDate1, Date startDate2, Date endDate2){
		System.out.println("startDate1="+startDate1);
		System.out.println("endDate1="+endDate1);
		System.out.println("startDate2="+startDate2);
		System.out.println("endDate2="+endDate2);
		System.out.println("*****");
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
			if(!onePoliticalBodyAdmin.getId().equals(politicalBodyAdmin)){
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
	public PoliticalBodyAdminDto getCurrentPoliticalBodyAdminByLocationId(Long locationId) throws ApplicationException {
		Location location = locationRepository.findOne(locationId);
		PoliticalBodyAdmin politicalBodyAdmin = politicalBodyAdminRepository.getCurrentPoliticalAdminByLocation(location);
		return politicalBodyAdminConvertor.convertBean(politicalBodyAdmin);
	}

	@Override
	public List<PoliticalBodyAdminDto> getAllPoliticalBodyAdminByLocationId(Long locationId) throws ApplicationException {
		Location location = locationRepository.findOne(locationId);
		Collection<PoliticalBodyAdmin> politicalBodyAdmins = politicalBodyAdminRepository.getAllPoliticalAdminByLocation(location);
		return politicalBodyAdminConvertor.convertBeanList(politicalBodyAdmins);
	}

}
