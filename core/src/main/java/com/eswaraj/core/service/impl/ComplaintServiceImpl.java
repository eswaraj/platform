package com.eswaraj.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.eswaraj.core.convertors.ComplaintConvertor;
import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.ComplaintService;
import com.eswaraj.domain.nodes.Complaint;
import com.eswaraj.domain.nodes.Person;
import com.eswaraj.domain.repo.ComplaintRepository;
import com.eswaraj.domain.repo.PersonRepository;
import com.eswaraj.web.dto.ComplaintDto;

/**
 * Implementation for complaint service
 * @author anuj
 * @data Jun 22, 2014
 */

@Component
@Transactional
public class ComplaintServiceImpl extends BaseService implements ComplaintService {
	
	@Autowired
	private ComplaintRepository complaintRepository;
	@Autowired
	private ComplaintConvertor complaintConvertor;
	@Autowired
	private PersonRepository personRepository;

	@Override
	public List<ComplaintDto> getPagedUserComplaints(Long personId, int start, int end) throws ApplicationException{
		Person person = getObjectIfExistsElseThrowExcetpion(personId, "Person", personRepository);
		List<Complaint> personComplaints = complaintRepository.getPagedComplaintsLodgedByPerson(person, start, end); 
		return complaintConvertor.convertBeanList(personComplaints);
	}

	@Override
	public ComplaintDto getComplaintById(Long complaintId) throws ApplicationException {
		Complaint complaint = complaintRepository.findOne(complaintId);
		return complaintConvertor.convertBean(complaint);
	}

	@Override
	public ComplaintDto saveComplaint(ComplaintDto complaintDto) throws ApplicationException {
		System.out.println("Saving Complaint "+ complaintDto);
		Complaint complaint = complaintConvertor.convert(complaintDto);
		complaint = complaintRepository.save(complaint);
		return complaintConvertor.convertBean(complaint);
	}

	@Override
	public List<ComplaintDto> getAllUserComplaints(Long personId) throws ApplicationException {
		Person person = getObjectIfExistsElseThrowExcetpion(personId, "Person", personRepository);
		List<Complaint> personComplaints = complaintRepository.getAllComplaintsLodgedByPerson(person); 
		return complaintConvertor.convertBeanList(personComplaints);
	}
}
