package com.eswaraj.core.service.impl;

import java.io.Serializable;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import org.springframework.stereotype.Component;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.LocationKeyService;

@Component
public class LocationkeyServiceImpl implements LocationKeyService, Serializable {

    private static final long serialVersionUID = 1L;

    private DecimalFormat decimalFormat;

    public LocationkeyServiceImpl() {
        decimalFormat = new DecimalFormat("#.###");
        decimalFormat.setRoundingMode(RoundingMode.DOWN);

    }
	@Override
	public String buildLocationKey(double longitude, double lattitude) throws ApplicationException {
        String key = "LOC-" + decimalFormat.format(longitude) + "-" + decimalFormat.format(lattitude);
        return key;
	}

}
