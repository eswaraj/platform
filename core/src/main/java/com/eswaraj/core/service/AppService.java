package com.eswaraj.core.service;

import java.util.List;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.web.dto.CategoryDto;
import com.eswaraj.web.dto.PartyDto;
import com.eswaraj.web.dto.PoliticalBodyAdminDto;
import com.eswaraj.web.dto.PoliticalBodyTypeDto;

public interface AppService {

	//Category APIs
	CategoryDto saveCategory(CategoryDto categoryDto) throws ApplicationException;
	
	CategoryDto getCategoryById(long categoryId) throws ApplicationException;
	
	List<CategoryDto> getAllRootCategories() throws ApplicationException;
	
	List<CategoryDto> getAllChildCategoryOfParentCategory(long parentCategoryId) throws ApplicationException;

	//Political Body Type APIs
	PoliticalBodyTypeDto savePoliticalBodyType(PoliticalBodyTypeDto politicalBodyTypeDto) throws ApplicationException;
	
	PoliticalBodyTypeDto getPoliticalBodyTypeById(Long politicalBodyTypeId) throws ApplicationException;
	
	List<PoliticalBodyTypeDto> getAllPoliticalBodyTypes() throws ApplicationException;
	
	//Party APis
	PartyDto saveParty(PartyDto partyDto) throws ApplicationException;
	
	PartyDto getPartyById(Long partyId) throws ApplicationException;
	
	List<PartyDto> getAllPoliticalParties() throws ApplicationException;
	
	//Political Admin APIs
	
	PoliticalBodyAdminDto savePoliticalBodyAdmin(PoliticalBodyAdminDto politicalBodyAdminDto) throws ApplicationException;
	
	PoliticalBodyAdminDto getPoliticalBodyAdminById(Long politicalBodyAdminId) throws ApplicationException;
	
	PoliticalBodyAdminDto getCurrentPoliticalBodyAdminByLocationId(Long locationId, Long pbTypeId) throws ApplicationException;
	
	List<PoliticalBodyAdminDto> getAllPoliticalBodyAdminByLocationId(Long locationId, Long pbTypeId) throws ApplicationException;
}
