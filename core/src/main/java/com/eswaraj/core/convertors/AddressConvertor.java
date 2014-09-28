package com.eswaraj.core.convertors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.domain.nodes.Address;
import com.eswaraj.domain.nodes.Location;
import com.eswaraj.domain.repo.AddressRepository;
import com.eswaraj.domain.repo.LocationRepository;
import com.eswaraj.web.dto.AddressDto;

@Component
public class AddressConvertor extends BaseConvertor<Address, AddressDto> {

	@Autowired
	private AddressRepository addressRepository;
	@Autowired
	private LocationRepository locationRepository; 

	
	@Override
	protected Address convertInternal(AddressDto addressDto) throws ApplicationException {
		Address address = getObjectIfExists(addressDto, "Address", addressRepository) ;
		if(address == null){
			address = new Address();
		}

		BeanUtils.copyProperties(addressDto, address);
		/*
		address.setCountry(getLocation(addressDto.getCountryId(), "Country"));
		address.setState(getLocation(addressDto.getStateId(), "State"));
		address.setDistrict(getLocation(addressDto.getDistrictId(), "District"));
		address.setCity(getLocation(addressDto.getCityId(), "City"));
		address.setVillage(getLocation(addressDto.getVillageId(), "Village"));
		address.setWard(getLocation(addressDto.getWardId(), "Ward"));
		*/
		return address;
	}
	private Location getLocation(Long locationId, String locationName) throws ApplicationException{
		Location location = null;
		if(locationId != null){
			location = locationRepository.findOne(locationId);
			if(location == null){
				throw new ApplicationException("No such "+locationName+ " [id="+locationId+"] found");
			}
		}
		return location;
		
	}
	@Override
	protected AddressDto convertBeanInternal(Address dbDto) {
		AddressDto addressDto = new AddressDto();
		BeanUtils.copyProperties(dbDto, addressDto);
        if (dbDto.getLocations() != null && !dbDto.getLocations().isEmpty()) {
            // Flaten all Locations
            for (Location oneLocation : dbDto.getLocations()) {
                System.out.println("oneLocation : " + oneLocation);
            }
        }
		return addressDto;
	}

}
