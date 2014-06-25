package com.eswaraj.core.convertors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.domain.nodes.ExecutiveBodyAdmin;
import com.eswaraj.domain.repo.AddressRepository;
import com.eswaraj.domain.repo.ExecutiveBodyAdminRepository;
import com.eswaraj.domain.repo.ExecutiveBodyRepository;
import com.eswaraj.domain.repo.PartyRepository;
import com.eswaraj.domain.repo.PersonRepository;
import com.eswaraj.domain.repo.PostRepository;
import com.eswaraj.web.dto.ExecutiveBodyAdminDto;

@Component
public class ExecutiveBodyAdminConvertor extends BaseConvertor<ExecutiveBodyAdmin, ExecutiveBodyAdminDto> {

	@Autowired
	private ExecutiveBodyAdminRepository executiveBodyAdminRepository;
	@Autowired
	private ExecutiveBodyRepository executiveBodyRepository;
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
	protected ExecutiveBodyAdmin convertInternal(ExecutiveBodyAdminDto webDto) throws ApplicationException {
		ExecutiveBodyAdmin executiveBodyAdmin = getObjectIfExists(webDto, "ExecutiveBodyAdmin", executiveBodyAdminRepository) ;
		if(executiveBodyAdmin == null){
			executiveBodyAdmin = new ExecutiveBodyAdmin();
		}
		BeanUtils.copyProperties(webDto, executiveBodyAdmin);
		executiveBodyAdmin.setExecutiveBody(getObjectIfExists(webDto.getExecutiveBodyId(), "Executive Body", executiveBodyRepository));
		executiveBodyAdmin.setManager(getObjectIfExists(webDto.getManagerId(), "Manager", executiveBodyAdminRepository));
		executiveBodyAdmin.setPerson(getObjectIfExists(webDto.getPersonId(), "Person", personRepository));
		executiveBodyAdmin.setPost(getObjectIfExists(webDto.getPostId(), "Post", postRepository));
		return executiveBodyAdmin;
	}
	
	@Override
	protected ExecutiveBodyAdminDto convertBeanInternal(ExecutiveBodyAdmin dbDto) throws ApplicationException {
		ExecutiveBodyAdminDto executiveBodyAdminDto = new ExecutiveBodyAdminDto();
		BeanUtils.copyProperties(dbDto, executiveBodyAdminDto);
		
		executiveBodyAdminDto.setExecutiveBodyId(getNodeId(dbDto.getExecutiveBody()));
		executiveBodyAdminDto.setManagerId(getNodeId(dbDto.getManager()));
		executiveBodyAdminDto.setPersonId(getNodeId(dbDto.getPerson()));
		executiveBodyAdminDto.setPostId(getNodeId(dbDto.getPost()));
		return executiveBodyAdminDto;
	}
	
}
