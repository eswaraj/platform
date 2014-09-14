package com.eswaraj.core.convertors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.domain.nodes.FacebookApp;
import com.eswaraj.domain.nodes.relationships.FacebookAppPermission;
import com.eswaraj.domain.repo.FacebookAppPermissionRepository;
import com.eswaraj.domain.repo.FacebookAppRepository;
import com.eswaraj.web.dto.FacebookAppPermissionDto;

@Component
public class FacebookAppPermissionConvertor extends BaseConvertor<FacebookAppPermission, FacebookAppPermissionDto> {

    private static final long serialVersionUID = 1L;

    @Autowired
    private FacebookAppPermissionRepository facebookAppPermissionRepository;
    @Autowired
    private FacebookAppRepository facebookAppRepository;
	
	private Logger Logger = LoggerFactory.getLogger(this.getClass());
	

	@Override
    protected FacebookAppPermission convertInternal(FacebookAppPermissionDto facebookAppPermissionDto) throws ApplicationException {
        FacebookAppPermission facebookAppPermission = getObjectIfExists(facebookAppPermissionDto, "FacebookAppPermission", facebookAppPermissionRepository);
        if (facebookAppPermission == null) {
            facebookAppPermission = new FacebookAppPermission();
		}
        BeanUtils.copyProperties(facebookAppPermissionDto, facebookAppPermission);
        if (!StringUtils.isEmpty(facebookAppPermissionDto.getFacebookAppId())) {
            FacebookApp facebookApp = facebookAppRepository.findByPropertyValue("appId", facebookAppPermissionDto.getFacebookAppId());
            if (facebookApp == null) {
                facebookApp = new FacebookApp();
                facebookApp.setAppId(facebookAppPermissionDto.getFacebookAppId());
                facebookApp = facebookAppRepository.save(facebookApp);
            }
            facebookAppPermission.setFacebookApp(facebookApp);
        }
        return facebookAppPermission;
	}

	@Override
    protected FacebookAppPermissionDto convertBeanInternal(FacebookAppPermission dbDto) {
        FacebookAppPermissionDto facebookAppPermissionDto = new FacebookAppPermissionDto();
        convertBeanInternal(dbDto, facebookAppPermissionDto);
        return facebookAppPermissionDto;
	}
	
    private void convertBeanInternal(FacebookAppPermission dbDto, FacebookAppPermissionDto facebookAppPermissionDto) {
        BeanUtils.copyProperties(dbDto, facebookAppPermissionDto);
	}

}
