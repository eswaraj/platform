package com.eswaraj.core.service;

import com.eswaraj.core.exceptions.ApplicationException;

public interface LocationKeyService {

	String buildLocationKey(double longitude, double lattitude) throws ApplicationException;
}
