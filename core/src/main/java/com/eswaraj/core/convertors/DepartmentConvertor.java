package com.eswaraj.core.convertors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.domain.nodes.Department;
import com.eswaraj.domain.repo.CategoryRepository;
import com.eswaraj.domain.repo.DepartmentRepository;
import com.eswaraj.web.dto.DepartmentDto;

@Component
public class DepartmentConvertor extends BaseConvertor<Department, DepartmentDto> {

	@Autowired
	private DepartmentRepository departmentRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	

	@Override
	protected Department convertInternal(DepartmentDto departmentDto) throws ApplicationException {
		Department department = getObjectIfExists(departmentDto, "Department", departmentRepository) ;
		if(department == null){
			department = new Department();
		}
		BeanUtils.copyProperties(departmentDto, department);
		return department;
	}

	@Override
	protected DepartmentDto convertBeanInternal(Department dbDto) throws ApplicationException {
		DepartmentDto departmentDto = new DepartmentDto();
		BeanUtils.copyProperties(dbDto, departmentDto);
		return departmentDto;
	}

}
