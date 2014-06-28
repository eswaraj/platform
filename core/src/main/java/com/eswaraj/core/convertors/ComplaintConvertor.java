package com.eswaraj.core.convertors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.domain.nodes.Complaint;
import com.eswaraj.domain.repo.CategoryRepository;
import com.eswaraj.domain.repo.ComplaintRepository;
import com.eswaraj.domain.repo.PersonRepository;
import com.eswaraj.web.dto.ComplaintDto;

@Component
public class ComplaintConvertor extends BaseConvertor<Complaint, ComplaintDto> {

	@Autowired
	private ComplaintRepository complaintRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private PersonRepository personRepository;

	@Override
	protected Complaint convertInternal(ComplaintDto complaintDto) throws ApplicationException {
		Complaint complaint = getObjectIfExists(complaintDto, "Complaint", complaintRepository) ;
		if(complaint == null){
			complaint = new Complaint();
		}
		BeanUtils.copyProperties(complaintDto, complaint);
		complaint.setCategory(getObjectIfExists(complaintDto.getCategoryId(), "Category", categoryRepository));
		complaint.setPerson(getObjectIfExists(complaintDto.getPersonId(), "Person", personRepository));
		return complaint;
	}

	@Override
	protected ComplaintDto convertBeanInternal(Complaint dbDto) {
		ComplaintDto complaintDto = new ComplaintDto();
		BeanUtils.copyProperties(dbDto, complaintDto);
		complaintDto.setCategoryId(getNodeId(dbDto.getCategory()));
		complaintDto.setPersonId(getNodeId(dbDto.getPerson()));
		return complaintDto;
	}

}
