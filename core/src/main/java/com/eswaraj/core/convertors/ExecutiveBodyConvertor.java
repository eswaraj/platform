package com.eswaraj.core.convertors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.domain.nodes.Address;
import com.eswaraj.domain.nodes.Category;
import com.eswaraj.domain.nodes.ExecutiveBody;
import com.eswaraj.domain.repo.AddressRepository;
import com.eswaraj.domain.repo.CategoryRepository;
import com.eswaraj.domain.repo.ExecutiveBodyRepository;
import com.eswaraj.web.dto.ExecutiveBodyDto;

@Component
public class ExecutiveBodyConvertor extends BaseConvertor<ExecutiveBody, ExecutiveBodyDto> {

	@Autowired
	private ExecutiveBodyRepository executiveBodyRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private AddressConvertor addressConvertor;
	@Autowired
	private AddressRepository addressRepository;
	

	@Override
	protected ExecutiveBody convertInternal(ExecutiveBodyDto executiveBodyDto) throws ApplicationException {
		ExecutiveBody executiveBody = getObjectIfExists(executiveBodyDto, "ExecutiveBody", executiveBodyRepository) ;
		if(executiveBody == null){
			executiveBody = new ExecutiveBody();
		}
		BeanUtils.copyProperties(executiveBodyDto, executiveBody);
		if(executiveBodyDto.getCategoryId() != null){
			Category category = categoryRepository.findOne(executiveBodyDto.getCategoryId());
			if(category == null){
				throw new ApplicationException("No such Category exists[id="+executiveBodyDto.getCategoryId()+"]");
			}
			executiveBody.setCategory(category);
		}
		if(executiveBodyDto.getParentExecutiveBodyId() != null){
			ExecutiveBody parentExecutiveBody = executiveBodyRepository.findOne(executiveBodyDto.getParentExecutiveBodyId());
			if(parentExecutiveBody == null){
				throw new ApplicationException("No such Executive Body exists[id="+executiveBodyDto.getParentExecutiveBodyId()+"]");
			}
			executiveBody.setParentExecutiveBody(parentExecutiveBody);
		}
		if(executiveBodyDto.getAddressDto() != null){
			Address address = executiveBody.getAddress();
			if(address != null){
				executiveBodyDto.getAddressDto().setId(executiveBody.getAddress().getId());
			}
			address = addressConvertor.convert(executiveBodyDto.getAddressDto());
			executiveBody.setAddress(address);
		}
		return executiveBody;
	}

	@Override
	protected ExecutiveBodyDto convertBeanInternal(ExecutiveBody dbDto) throws ApplicationException {
		ExecutiveBodyDto executiveBodyDto = new ExecutiveBodyDto();
		BeanUtils.copyProperties(dbDto, executiveBodyDto);
		if(dbDto.getBoundary() != null){
			executiveBodyDto.setBoundaryId(dbDto.getBoundary().getId());	
		}
		if(dbDto.getAddress() != null){
			Address address = addressRepository.findOne(dbDto.getAddress().getId());
			executiveBodyDto.setAddressDto(addressConvertor.convertBean(address));	
		}
		if(dbDto.getCategory() != null){
			executiveBodyDto.setCategoryId(dbDto.getCategory().getId());	
		}
		if(dbDto.getParentExecutiveBody() != null){
			executiveBodyDto.setParentExecutiveBodyId(dbDto.getParentExecutiveBody().getId());	
		}

		return executiveBodyDto;
	}

}
