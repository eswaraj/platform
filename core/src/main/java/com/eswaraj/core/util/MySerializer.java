package com.eswaraj.core.util;

import org.apache.commons.lang.SerializationUtils;

import com.eswaraj.web.dto.LocationBoundaryFileDto;

import kafka.serializer.Encoder;
import kafka.utils.VerifiableProperties;

public class MySerializer implements Encoder<LocationBoundaryFileDto>{

	public MySerializer(VerifiableProperties vp){
		
	}
	@Override
	public byte[] toBytes(LocationBoundaryFileDto locationBoundaryFileDto) {
		return SerializationUtils.serialize(locationBoundaryFileDto);
	}


}
