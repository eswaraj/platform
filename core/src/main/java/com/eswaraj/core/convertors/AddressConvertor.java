package com.eswaraj.core.convertors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.domain.nodes.Address;
import com.eswaraj.domain.nodes.Location;
import com.eswaraj.domain.nodes.LocationType;
import com.eswaraj.domain.repo.AddressRepository;
import com.eswaraj.domain.repo.LocationRepository;
import com.eswaraj.domain.repo.LocationTypeRepository;
import com.eswaraj.web.dto.AddressDto;

@Component
public class AddressConvertor extends BaseConvertor<Address, AddressDto> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    @Autowired
	private AddressRepository addressRepository;
	@Autowired
	private LocationRepository locationRepository; 
    @Autowired
    private LocationConvertor locationConvertor;
    @Autowired
    private LocationTypeRepository locationTypeRepository;

	
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
    protected AddressDto convertBeanInternal(Address dbDto) throws ApplicationException {
		AddressDto addressDto = new AddressDto();
		BeanUtils.copyProperties(dbDto, addressDto);
        if (dbDto.getLocations() != null && !dbDto.getLocations().isEmpty()) {
            // Flaten all Locations
            LocationType locationType;
            Location location;
            for (Location oneLocation : dbDto.getLocations()) {
                // location = locationRepository.findOne(oneLocation.getId());
                locationType = locationTypeRepository.findOne(oneLocation.getLocationType().getId());
                if (locationType.getName().equalsIgnoreCase("Country")) {
                    addressDto.setCountry(locationConvertor.convertBean(oneLocation));
                }
                if (locationType.getName().equalsIgnoreCase("State")) {
                    addressDto.setState(locationConvertor.convertBean(oneLocation));
                }
                if (locationType.getName().equalsIgnoreCase("District")) {
                    addressDto.setDistrict(locationConvertor.convertBean(oneLocation));
                }
                if (locationType.getName().equalsIgnoreCase("Assembly Constituency")) {
                    addressDto.setAc(locationConvertor.convertBean(oneLocation));
                }
                if (locationType.getName().equalsIgnoreCase("Parliament Constituency")) {
                    addressDto.setPc(locationConvertor.convertBean(oneLocation));
                }
                if (locationType.getName().equalsIgnoreCase("Ward")) {
                    addressDto.setWard(locationConvertor.convertBean(oneLocation));
                }
                System.out.println("oneLocation : " + oneLocation);
            }
        }
		return addressDto;
	}

}
