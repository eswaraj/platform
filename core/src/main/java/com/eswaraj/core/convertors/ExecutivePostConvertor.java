package com.eswaraj.core.convertors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.domain.nodes.Department;
import com.eswaraj.domain.nodes.ExecutivePost;
import com.eswaraj.domain.repo.DepartmentRepository;
import com.eswaraj.domain.repo.ExecutivePostRepository;
import com.eswaraj.web.dto.ExecutivePostDto;

@Component
public class ExecutivePostConvertor extends BaseConvertor<ExecutivePost, ExecutivePostDto> {

	@Autowired
	private ExecutivePostRepository executivePostRepository;
	@Autowired
	private DepartmentRepository departmentRepository;

	@Override
	protected ExecutivePost convertInternal(ExecutivePostDto webDto) throws ApplicationException {
		ExecutivePost executivePost = getObjectIfExists(webDto, "ExecutivePost", executivePostRepository) ;
		if(executivePost == null){
			executivePost = new ExecutivePost();
		}
		BeanUtils.copyProperties(webDto, executivePost);
		if(webDto.getDepartmentId() != null){
			Department department = departmentRepository.findOne(webDto.getDepartmentId());
			if(department == null){
				throw new ApplicationException("No such ExecutiveBody exists[id="+webDto.getDepartmentId()+"]");
			}
			executivePost.setDepartment(department);
		}
		return executivePost;
	}

	@Override
	protected ExecutivePostDto convertBeanInternal(ExecutivePost dbDto) {
		ExecutivePostDto executivePostDto = new ExecutivePostDto();
		BeanUtils.copyProperties(dbDto, executivePostDto);
		if(dbDto.getDepartment() != null){
			executivePostDto.setDepartmentId(dbDto.getDepartment().getId());	
		}
		return executivePostDto;
	}

}
