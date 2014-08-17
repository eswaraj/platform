package com.eswaraj.core.service;

import java.awt.geom.Point2D;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.eswaraj.core.exceptions.ApplicationException;

public interface LocationKeyService {

    String buildLocationKey(double lattitude, double longitude) throws ApplicationException;

    String buildLocationKeyForNearByComplaints(double lattitude, double longitude) throws ApplicationException;

    List<Point2D> getAllPointsBetweenRectangle(BigDecimal topLeftLat, BigDecimal topLeftLong, BigDecimal bottomRightLat, BigDecimal bottomRightLong) throws ApplicationException;

    String getNearByHourComplaintCounterKey(Date date, double lattitude, double longitude) throws ApplicationException;

    String getNearByKeyPrefix(double lattitude, double longitude) throws ApplicationException;

    String getLocationInformationKey(Long locationId);

    String getLocationComplaintsKey(Long locationId);

}
