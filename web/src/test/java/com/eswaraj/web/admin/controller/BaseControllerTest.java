package com.eswaraj.web.admin.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.eswaraj.base.BaseEswarajMockitoTest;
import com.eswaraj.web.dto.AddressDto;
import com.eswaraj.web.dto.ComplaintDto;
import com.eswaraj.web.dto.PoliticalBodyAdminDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BaseControllerTest extends BaseEswarajMockitoTest{

    @Autowired
    private ApplicationContext applicationContext;
	public BaseControllerTest() {
		// TODO Auto-generated constructor stub
	}

    @Before
    public void init() {
        String[] profiles = applicationContext.getEnvironment().getActiveProfiles();
        System.out.println("All Active Spring Profiles are for the test are: ");
        if (profiles != null) {
            for (String oneProfile : profiles) {
                System.out.println("        " + oneProfile);
            }
        }
    }

	protected AddressDto createAddress(String line1, String line2, String line3, String postalCode){
		AddressDto address = new AddressDto();
		address.setLine1(line1);
		address.setLine2(line2);
		address.setLine3(line3);
		address.setPostalCode(postalCode);
		return address;
	}
	protected AddressDto createRandomAddress(){
		String line1 = randomAlphaString(16);
		String line2 = randomAlphaString(16);
		String line3 = randomAlphaString(16);
		String postalCode = randomNumericString(6);
		return createAddress(line1, line2, line3, postalCode);
	}
	protected PoliticalBodyAdminDto createOnePoliticalBodyAdminDto(){
		boolean active = randomBoolean();
		String email = randomEmailAddress();
		Date startDate = randomDateInPast();
		Date endDate = randomDateInFuture();
		AddressDto homeAddressDto = createRandomAddress();
		AddressDto officeAddressDto = createRandomAddress();
		String landLine1 = randomNumericString(10); 
		String landLine2 = randomNumericString(10);
		String mobile1 = randomNumericString(10);
		String mobile2 = randomNumericString(10);
		Long locationId = randomPositiveLong(); 
		Long partyId = randomPositiveLong();
		Long personId = randomPositiveLong(); 
		Long politicalBodyTypeId = randomPositiveLong();
		
		PoliticalBodyAdminDto politicalBodyAdminDto = new PoliticalBodyAdminDto();
		politicalBodyAdminDto.setActive(active);
		politicalBodyAdminDto.setEmail(email);
		politicalBodyAdminDto.setEndDate(endDate);
		politicalBodyAdminDto.setStartDate(startDate);
		politicalBodyAdminDto.setHomeAddressDto(homeAddressDto);
		politicalBodyAdminDto.setOfficeAddressDto(officeAddressDto);
		politicalBodyAdminDto.setLandLine1(landLine1);
		politicalBodyAdminDto.setLandLine2(landLine2);
		politicalBodyAdminDto.setLocationId(locationId);	
		politicalBodyAdminDto.setMobile1(mobile1);
		politicalBodyAdminDto.setMobile2(mobile2);
		politicalBodyAdminDto.setPartyId(partyId);	
		politicalBodyAdminDto.setPersonId(personId);	
		politicalBodyAdminDto.setPoliticalBodyTypeId(politicalBodyTypeId);	
		return politicalBodyAdminDto;
	}
	protected ComplaintDto createComplaintDto() {
		ComplaintDto complaintDto = new ComplaintDto();
		complaintDto.setDescription(randomAlphaString(256));
		complaintDto.setTitle(randomAlphaString(30));
		return complaintDto;
	}
	
	protected List<ComplaintDto> createComplaints(int count){
		List<ComplaintDto> complaints = new ArrayList<>(count);
		for(int i = 0; i < count; i++) {
			complaints.add(createComplaintDto());
		}
		return complaints;
	}
	
	public byte[] convertObjectToJsonBytes(Object object) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		return mapper.writeValueAsBytes(object);
	}

}
