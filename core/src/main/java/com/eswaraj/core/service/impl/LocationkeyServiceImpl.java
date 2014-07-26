package com.eswaraj.core.service.impl;

import org.springframework.stereotype.Component;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.LocationKeyService;

@Component
public class LocationkeyServiceImpl implements LocationKeyService {

	
	@Override
	public String buildLocationKey(double longitude, double lattitude) throws ApplicationException {
		
		return null;
	}

}
