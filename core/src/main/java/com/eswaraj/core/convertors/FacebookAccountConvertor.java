package com.eswaraj.core.convertors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.domain.nodes.FacebookAccount;
import com.eswaraj.domain.repo.FacebookAccountRepository;
import com.eswaraj.web.dto.FacebookAccountDto;

@Component
public class FacebookAccountConvertor extends BaseConvertor<FacebookAccount, FacebookAccountDto> {

    private static final long serialVersionUID = 1L;

    @Autowired
    private FacebookAccountRepository facebookAccountRepository;
	
	private Logger Logger = LoggerFactory.getLogger(this.getClass());
	

	@Override
    protected FacebookAccount convertInternal(FacebookAccountDto facebookAccountDto) throws ApplicationException {
        FacebookAccount facebookAccount = getObjectIfExists(facebookAccountDto, "FacebookAccount", facebookAccountRepository);
        if (facebookAccount == null) {
            facebookAccount = new FacebookAccount();
		}
        BeanUtils.copyProperties(facebookAccountDto, facebookAccount);
        return facebookAccount;
	}

	@Override
    protected FacebookAccountDto convertBeanInternal(FacebookAccount dbDto) {
        FacebookAccountDto facebookAccountDto = new FacebookAccountDto();
        convertBeanInternal(dbDto, facebookAccountDto);
        return facebookAccountDto;
	}
	
    private void convertBeanInternal(FacebookAccount dbDto, FacebookAccountDto facebookAccountDto) {
        BeanUtils.copyProperties(dbDto, facebookAccountDto);
	}

}
