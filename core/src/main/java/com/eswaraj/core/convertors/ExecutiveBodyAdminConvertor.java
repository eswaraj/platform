package com.eswaraj.core.convertors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.domain.nodes.DepartmentAdmin;
import com.eswaraj.domain.repo.AddressRepository;
import com.eswaraj.domain.repo.DepartmentAdminRepository;
import com.eswaraj.domain.repo.DepartmentRepository;
import com.eswaraj.domain.repo.PartyRepository;
import com.eswaraj.domain.repo.PersonRepository;
import com.eswaraj.domain.repo.PostRepository;
import com.eswaraj.web.dto.ExecutiveBodyAdminDto;

@Component
public class ExecutiveBodyAdminConvertor extends BaseConvertor<DepartmentAdmin, ExecutiveBodyAdminDto> {

	@Autowired
	private DepartmentAdminRepository executiveBodyAdminRepository;
	@Autowired
    private DepartmentRepository departmentRepository;
	@Autowired
	private PartyRepository partyRepository;
	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private PostRepository postRepository;
	@Autowired
	private AddressConvertor addressConvertor;
	@Autowired
	private AddressRepository addressRepository;

	@Override
	protected DepartmentAdmin convertInternal(ExecutiveBodyAdminDto webDto) throws ApplicationException {
		DepartmentAdmin executiveBodyAdmin = getObjectIfExists(webDto, "ExecutiveBodyAdmin", executiveBodyAdminRepository) ;
		if(executiveBodyAdmin == null){
			executiveBodyAdmin = new DepartmentAdmin();
		}
		BeanUtils.copyProperties(webDto, executiveBodyAdmin);
        executiveBodyAdmin.setDepartment(getObjectIfExists(webDto.getExecutiveBodyId(), "Executive Body", departmentRepository));
		executiveBodyAdmin.setManager(getObjectIfExists(webDto.getManagerId(), "Manager", executiveBodyAdminRepository));
		executiveBodyAdmin.setPerson(getObjectIfExists(webDto.getPersonId(), "Person", personRepository));
		executiveBodyAdmin.setPost(getObjectIfExists(webDto.getPostId(), "Post", postRepository));
		return executiveBodyAdmin;
	}
	
	@Override
	protected ExecutiveBodyAdminDto convertBeanInternal(DepartmentAdmin dbDto) throws ApplicationException {
		ExecutiveBodyAdminDto executiveBodyAdminDto = new ExecutiveBodyAdminDto();
		BeanUtils.copyProperties(dbDto, executiveBodyAdminDto);
		
        executiveBodyAdminDto.setExecutiveBodyId(getNodeId(dbDto.getDepartment()));
		executiveBodyAdminDto.setManagerId(getNodeId(dbDto.getManager()));
		executiveBodyAdminDto.setPersonId(getNodeId(dbDto.getPerson()));
		executiveBodyAdminDto.setPostId(getNodeId(dbDto.getPost()));
		return executiveBodyAdminDto;
	}
	
}
