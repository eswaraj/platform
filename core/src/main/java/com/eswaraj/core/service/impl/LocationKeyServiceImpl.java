package com.eswaraj.core.service.impl;

import java.awt.geom.Point2D;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.LocationKeyService;

@Component
public class LocationKeyServiceImpl implements LocationKeyService, Serializable {

    private static final long serialVersionUID = 1L;

    private DecimalFormat decimalFormat;
    private DecimalFormat decimalFormatUpto2DecimalPoints;
    protected DateFormat hourFormat = new SimpleDateFormat("yyyyMMddkk");

    private final String LOCATION_PREFIX = "LC.";
    private final String CATEGORY_PREFIX = "CG.";

    public LocationKeyServiceImpl() {
        decimalFormat = new DecimalFormat("#.000");
        decimalFormat.setRoundingMode(RoundingMode.DOWN);
        decimalFormatUpto2DecimalPoints = new DecimalFormat("#.00");
        decimalFormatUpto2DecimalPoints.setRoundingMode(RoundingMode.DOWN);

    }
	@Override
    public String buildLocationKey(double lattitude, double longitude) throws ApplicationException {
        String key = "L" + decimalFormat.format(lattitude) + "-" + decimalFormat.format(longitude);
        return key;
	}

    @Override
    public List<Point2D> getAllPointsBetweenRectangle(BigDecimal topLeftLat, BigDecimal topLeftLong, BigDecimal bottomRightLat, BigDecimal bottomRightLong) throws ApplicationException {
        List<Point2D> allPoints = new ArrayList<>(1000);
        MathContext topLeftMc = new MathContext(3, RoundingMode.DOWN);
        topLeftLat.round(topLeftMc);
        topLeftLong.round(topLeftMc);
        MathContext bottomRightMc = new MathContext(3, RoundingMode.UP);
        bottomRightLat.round(bottomRightMc);
        bottomRightLong.round(bottomRightMc);
        BigDecimal addedValue = new BigDecimal(.001);
        Point2D onePoint;
        int i=0;
        for (BigDecimal latitude = topLeftLat; latitude.compareTo(bottomRightLat) <= 0; latitude.add(addedValue)) {
            for (BigDecimal longitude = topLeftLong; longitude.compareTo(bottomRightLong) <= 0; longitude.add(addedValue)) {
                onePoint = new Point2D.Double(latitude.doubleValue(), longitude.doubleValue());
                allPoints.add(onePoint);
                i++;
            }
        }
        return allPoints;
    }

    @Override
    public String buildLocationKeyForNearByComplaints(double lattitude, double longitude) throws ApplicationException {
        String key = "L" + decimalFormatUpto2DecimalPoints.format(lattitude) + "-" + decimalFormatUpto2DecimalPoints.format(longitude);
        return key;
    }

    @Override
    public String getNearByKey(double lattitude, double longitude) throws ApplicationException {
        String key = buildLocationKeyForNearByComplaints(lattitude, longitude);
        return key;
    }

    @Override
    public String getNearByKeyPrefix(double lattitude, double longitude) throws ApplicationException {
        return buildLocationKeyForNearByComplaints(lattitude, longitude);
    }

    @Override
    public String getLocationInformationKey(Long locationId) {
        return LOCATION_PREFIX + locationId + ".info";
    }

    @Override
    public String getLocationComplaintsKey(Long locationId) {
        return LOCATION_PREFIX + locationId + ".complaints";
    }

    @Override
    public String getLocationCategoryComplaintsKey(Long locationId, long categoryId) {
        return LOCATION_PREFIX + locationId + "." + CATEGORY_PREFIX + categoryId + ".complaints";
    }

}
