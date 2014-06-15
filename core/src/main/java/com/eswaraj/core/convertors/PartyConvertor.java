package com.eswaraj.core.convertors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.domain.nodes.Party;
import com.eswaraj.domain.repo.PartyRepository;
import com.eswaraj.web.dto.PartyDto;

@Component
public class PartyConvertor extends BaseConvertor<Party, PartyDto> {

	@Autowired
	private PartyRepository partyRepository;

	
	@Override
	protected Party convertInternal(PartyDto partyDto) throws ApplicationException {
		Party party = getObjectIfExists(partyDto, "Party", partyRepository) ;
		if(party == null){
			party = new Party();
		}

		BeanUtils.copyProperties(partyDto, party);
		return party;
	}
	@Override
	protected PartyDto convertBeanInternal(Party dbDto) {
		PartyDto partyDto = new PartyDto();
		BeanUtils.copyProperties(dbDto, partyDto);
		return partyDto;
	}

}
