package com.eswaraj.core.convertors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.domain.nodes.Photo;
import com.eswaraj.domain.repo.PhotoRepository;
import com.eswaraj.web.dto.PhotoDto;

@Component
public class PhotoConvertor extends BaseConvertor<Photo, PhotoDto> {

	@Autowired
	private PhotoRepository photoRepository;

	
	@Override
	protected Photo convertInternal(PhotoDto photoDto) throws ApplicationException {
		Photo photo = getObjectIfExists(photoDto, "Photo", photoRepository) ;
		if(photo == null){
			photo = new Photo();
		}

		BeanUtils.copyProperties(photoDto, photo);
		return photo;
	}
	@Override
	protected PhotoDto convertBeanInternal(Photo dbDto) {
		PhotoDto photoDto = new PhotoDto();
		BeanUtils.copyProperties(dbDto, photoDto);
		return photoDto;
	}

}
