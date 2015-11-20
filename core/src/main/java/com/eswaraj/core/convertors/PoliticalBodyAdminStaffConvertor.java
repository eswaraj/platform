package com.eswaraj.core.convertors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.domain.nodes.Person;
import com.eswaraj.domain.nodes.PoliticalBodyAdminStaff;
import com.eswaraj.domain.repo.PersonRepository;
import com.eswaraj.domain.repo.PoliticalBodyAdminRepository;
import com.eswaraj.domain.repo.PoliticalBodyAdminStaffRepository;
import com.eswaraj.web.dto.PoliticalBodyAdminStaffDto;

@Component
public class PoliticalBodyAdminStaffConvertor extends BaseConvertor<PoliticalBodyAdminStaff, PoliticalBodyAdminStaffDto> {

    private static final long serialVersionUID = 1L;
    @Autowired
    private PoliticalBodyAdminStaffRepository politicalBodyAdminStaffRepository;
	@Autowired
	private PersonRepository personRepository;
	@Autowired
    private PoliticalBodyAdminRepository politicalBodyAdminRepository;
    @Autowired
    private PersonConvertor personConvertor;

	@Override
    protected PoliticalBodyAdminStaff convertInternal(PoliticalBodyAdminStaffDto webDto) throws ApplicationException {
        PoliticalBodyAdminStaff politicalBodyAdmin = getObjectIfExists(webDto, "PoliticalBodyAdminStaff", politicalBodyAdminStaffRepository);
		if(politicalBodyAdmin == null){
            politicalBodyAdmin = new PoliticalBodyAdminStaff();
		}
		BeanUtils.copyProperties(webDto, politicalBodyAdmin);
		return politicalBodyAdmin;
	}

	
	@Override
    protected PoliticalBodyAdminStaffDto convertBeanInternal(PoliticalBodyAdminStaff dbDto) throws ApplicationException {
        PoliticalBodyAdminStaffDto politicalBodyAdminDto = new PoliticalBodyAdminStaffDto();
		BeanUtils.copyProperties(dbDto, politicalBodyAdminDto);
        if (dbDto.getPerson() != null) {
            Person person = personRepository.findOne(dbDto.getPerson().getId());
            politicalBodyAdminDto.setPersonDto(personConvertor.convertBean(person));
		}
        if (dbDto.getPoliticalBodyAdmin() != null) {
            politicalBodyAdminDto.setPoliticalBodyAdminId(dbDto.getPoliticalBodyAdmin().getId());
		}
		return politicalBodyAdminDto;
	}
	
}
