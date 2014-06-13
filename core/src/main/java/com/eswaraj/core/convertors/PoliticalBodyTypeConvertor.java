package com.eswaraj.core.convertors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.domain.nodes.LocationType;
import com.eswaraj.domain.nodes.PoliticalBodyType;
import com.eswaraj.domain.repo.LocationTypeRepository;
import com.eswaraj.domain.repo.PoliticalBodyTypeRepository;
import com.eswaraj.web.dto.PoliticalBodyTypeDto;

@Component
public class PoliticalBodyTypeConvertor extends BaseConvertor<PoliticalBodyType, PoliticalBodyTypeDto> {

	@Autowired
	private PoliticalBodyTypeRepository politicalBodyTypeRepository;
	@Autowired
	private LocationTypeRepository locationTypeRepository;
	

	@Override
	protected PoliticalBodyType convertInternal(PoliticalBodyTypeDto politicalBodyTypeDto) throws ApplicationException {
		PoliticalBodyType politicalBodyType = getObjectIfExists(politicalBodyTypeDto, "PoliticalBodyType", politicalBodyTypeRepository) ;
		if(politicalBodyType == null){
			politicalBodyType = new PoliticalBodyType();
		}
		BeanUtils.copyProperties(politicalBodyTypeDto, politicalBodyType);
		if(politicalBodyTypeDto.getLocationTypeId() != null){
			LocationType locationType = locationTypeRepository.findOne(politicalBodyTypeDto.getLocationTypeId());
			if(locationType == null){
				throw new ApplicationException("No such Location type[id="+politicalBodyTypeDto.getLocationTypeId()+"] found as location type");
			}
			politicalBodyType.setLocationType(locationType);
		}
		return politicalBodyType;
	}

	@Override
	protected PoliticalBodyTypeDto convertBeanInternal(PoliticalBodyType dbPoliticalBodyType) {
		PoliticalBodyTypeDto politicalBodyTypeDto = new PoliticalBodyTypeDto();
		BeanUtils.copyProperties(dbPoliticalBodyType, politicalBodyTypeDto);
		if(dbPoliticalBodyType.getLocationType() != null){
			politicalBodyTypeDto.setLocationTypeId(dbPoliticalBodyType.getLocationType().getId());
		}
		return politicalBodyTypeDto;
	}

}
