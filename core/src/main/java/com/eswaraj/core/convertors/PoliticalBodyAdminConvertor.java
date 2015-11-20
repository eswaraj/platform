package com.eswaraj.core.convertors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.domain.nodes.Address;
import com.eswaraj.domain.nodes.PoliticalBodyAdmin;
import com.eswaraj.domain.repo.AddressRepository;
import com.eswaraj.domain.repo.LocationRepository;
import com.eswaraj.domain.repo.PartyRepository;
import com.eswaraj.domain.repo.PersonRepository;
import com.eswaraj.domain.repo.PoliticalBodyAdminRepository;
import com.eswaraj.domain.repo.PoliticalBodyTypeRepository;
import com.eswaraj.web.dto.AddressDto;
import com.eswaraj.web.dto.PoliticalBodyAdminDto;

@Component
public class PoliticalBodyAdminConvertor extends BaseConvertor<PoliticalBodyAdmin, PoliticalBodyAdminDto> {

	@Autowired
	private PoliticalBodyAdminRepository politicalBodyAdminRepository;
	@Autowired
	private LocationRepository locationRepository;
	@Autowired
	private PartyRepository partyRepository;
	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private PoliticalBodyTypeRepository politicalBodyTypeRepository;
	@Autowired
	private AddressConvertor addressConvertor;
	@Autowired
	private AddressRepository addressRepository;

	@Override
	protected PoliticalBodyAdmin convertInternal(PoliticalBodyAdminDto webDto) throws ApplicationException {
		PoliticalBodyAdmin politicalBodyAdmin = getObjectIfExists(webDto, "PoliticalBodyAdmin", politicalBodyAdminRepository) ;
		if(politicalBodyAdmin == null){
			politicalBodyAdmin = new PoliticalBodyAdmin();
		}
		BeanUtils.copyProperties(webDto, politicalBodyAdmin);
		politicalBodyAdmin.setLocation(getObjectIfExists(webDto.getLocationId(), "Location", locationRepository));
		politicalBodyAdmin.setParty(getObjectIfExists(webDto.getPartyId(), "Party", partyRepository));
		politicalBodyAdmin.setPerson(getObjectIfExists(webDto.getPersonId(), "Person", personRepository));
		politicalBodyAdmin.setPoliticalBodyType(getObjectIfExists(webDto.getPoliticalBodyTypeId(), "PoliticalBodyType", politicalBodyTypeRepository));
		politicalBodyAdmin.setHomeAddress(processAddress(webDto.getHomeAddressDto(), politicalBodyAdmin.getHomeAddress()));
		politicalBodyAdmin.setOfficeAddress(processAddress(webDto.getOfficeAddressDto(), politicalBodyAdmin.getOfficeAddress()));
		return politicalBodyAdmin;
	}

	
	private Address processAddress(AddressDto addressDto, Address dbAddress) throws ApplicationException{
		Address address = null;
		if(addressDto == null){
			if(dbAddress != null){
				//Delete the address
				addressRepository.delete(dbAddress);
			}
		}else{
			//safe guard : even if client didn't send the id of existing address, check if already an address linked to existing address
			//then use that address id to update it..... we don't want to leave an orphan address which is not linked to any person
			if(dbAddress != null){
				addressDto.setId(dbAddress.getId());
			}
			address = addressConvertor.convert(addressDto);
		}
		return address;
	}

	@Override
	protected PoliticalBodyAdminDto convertBeanInternal(PoliticalBodyAdmin dbDto) throws ApplicationException {
		PoliticalBodyAdminDto politicalBodyAdminDto = new PoliticalBodyAdminDto();
		BeanUtils.copyProperties(dbDto, politicalBodyAdminDto);
		if(dbDto.getHomeAddress() != null){
			Address homeAddress = addressRepository.findOne(dbDto.getHomeAddress().getId());
			politicalBodyAdminDto.setHomeAddressDto(addressConvertor.convertBean(homeAddress));	
		}
		if(dbDto.getOfficeAddress() != null){
			Address officeAddress = addressRepository.findOne(dbDto.getOfficeAddress().getId());
			politicalBodyAdminDto.setOfficeAddressDto(addressConvertor.convertBean(officeAddress));	
		}
		
		politicalBodyAdminDto.setLocationId(getNodeId(dbDto.getLocation()));
		politicalBodyAdminDto.setPartyId(getNodeId(dbDto.getParty()));
		politicalBodyAdminDto.setPersonId(getNodeId(dbDto.getPerson()));
		politicalBodyAdminDto.setPoliticalBodyTypeId(getNodeId(dbDto.getPoliticalBodyType()));
		return politicalBodyAdminDto;
	}
	
}
