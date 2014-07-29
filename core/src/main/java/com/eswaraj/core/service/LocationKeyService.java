package com.eswaraj.core.service;

import java.awt.geom.Point2D;
import java.math.BigDecimal;
import java.util.List;

import com.eswaraj.core.exceptions.ApplicationException;

public interface LocationKeyService {

	String buildLocationKey(double longitude, double lattitude) throws ApplicationException;

    List<Point2D> getAllPointsBetweenRectangle(BigDecimal topLeftLat, BigDecimal topLeftLong, BigDecimal bottomRightLat, BigDecimal bottomRightLong) throws ApplicationException;
}
