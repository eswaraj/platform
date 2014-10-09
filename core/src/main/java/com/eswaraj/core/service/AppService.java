package com.eswaraj.core.service;

import java.util.List;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.web.dto.CategoryDto;
import com.eswaraj.web.dto.CategoryWithChildCategoryDto;
import com.eswaraj.web.dto.DepartmentDto;
import com.eswaraj.web.dto.ExecutiveBodyAdminDto;
import com.eswaraj.web.dto.ExecutiveBodyDto;
import com.eswaraj.web.dto.ExecutivePostDto;
import com.eswaraj.web.dto.PartyDto;
import com.eswaraj.web.dto.PoliticalBodyAdminDto;
import com.eswaraj.web.dto.PoliticalBodyAdminStaffDto;
import com.eswaraj.web.dto.PoliticalBodyTypeDto;
import com.eswaraj.web.dto.PoliticalPositionDto;
import com.eswaraj.web.dto.SavePoliticalAdminStaffRequestDto;

public interface AppService {

	//Category APIs
	CategoryDto saveCategory(CategoryDto categoryDto) throws ApplicationException;
	
	CategoryDto getCategoryById(long categoryId) throws ApplicationException;
	
	List<CategoryDto> getAllRootCategories() throws ApplicationException;
	
	/**
	 * Service will be used from mobile
	 * @return
	 * @throws ApplicationException
	 */
	List<CategoryWithChildCategoryDto> getAllCategories() throws ApplicationException;
	
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
	
    List<PoliticalBodyAdminDto> getAllCurrentPoliticalBodyAdminByLocationId(Long locationId) throws ApplicationException;

	List<PoliticalBodyAdminDto> getAllPoliticalBodyAdminByLocationId(Long locationId, Long pbTypeId) throws ApplicationException;
	
    List<PoliticalBodyAdminDto> getAllPoliticalBodyAdminHistoryByPersonId(Long personId) throws ApplicationException;

    List<PoliticalPositionDto> getAllPoliticalPositionsOfPerson(Long personId, boolean activeOnly) throws ApplicationException;

	//Executive Body APIs
	ExecutiveBodyDto saveExecutiveBody(ExecutiveBodyDto executiveBodyDto) throws ApplicationException;
	
	ExecutiveBodyDto getExecutiveBodyById(Long executiveBodyId) throws ApplicationException;
	
	List<ExecutiveBodyDto> getAllChildExecutiveBodyOfParent(Long parentExecutiveBodyId) throws ApplicationException;
	
	List<ExecutiveBodyDto> getAllRootExecutiveBodyOfDepartment(Long departmentId) throws ApplicationException;
	
	
	//Executive Post Admin APIs
	
	ExecutivePostDto saveExecutivePost(ExecutivePostDto executivePostDto) throws ApplicationException;
	
	ExecutivePostDto getExecutivePostById(Long executivePostId) throws ApplicationException;
	
	//Executive Body Admin APIs
	
	ExecutiveBodyAdminDto saveExecutiveBodyAdmin(ExecutiveBodyAdminDto executiveBodyAdminDto) throws ApplicationException;
	
	ExecutiveBodyAdminDto getExecutiveBodyAdminById(Long executiveBodyAdminId) throws ApplicationException;
	
	List<ExecutiveBodyAdminDto> getAllExecutiveBodyAdminOfExecutiveBody(Long executiveBodyId) throws ApplicationException;
	
	// Department Admin APIs
	
	DepartmentDto saveDepartment(DepartmentDto departmentDto) throws ApplicationException;
	
	DepartmentDto getDepartmentById(Long departmentId) throws ApplicationException;
	
	List<DepartmentDto> getAllDepartmentsOfCategory(long categoryId) throws ApplicationException;

    void initializeData() throws ApplicationException;

    void updateAllUrls() throws ApplicationException;

    void savePoliticalBodyAdminStaff(SavePoliticalAdminStaffRequestDto savePoliticalAdminStaffRequestDto) throws ApplicationException;

    List<PoliticalBodyAdminStaffDto> getAllStaffOfPoliticalAdmin(Long politicalAdminId) throws ApplicationException;

    PoliticalBodyAdminStaffDto deletePoliticalAdminStaff(Long politicalAdminStaffId) throws ApplicationException;
}
