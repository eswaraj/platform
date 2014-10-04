package com.eswaraj.core.service;

import java.awt.geom.Point2D;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.eswaraj.core.exceptions.ApplicationException;

public interface AppKeyService {

    String getAllCategoriesKey();

    String getComplaintObjectKey(Long complaintId);

    String getComplaintObjectKey(String complaintId);

    String getPoliticalBodyAdminObjectKey(String politicalBodyAdminId);

    String getExecutiveBodyAdminObjectKey(String executiveBodyAdminId);

    String buildLocationKey(double lattitude, double longitude) throws ApplicationException;

    String buildLocationKeyForNearByComplaints(double lattitude, double longitude) throws ApplicationException;

    List<Point2D> getAllPointsBetweenRectangle(BigDecimal topLeftLat, BigDecimal topLeftLong, BigDecimal bottomRightLat, BigDecimal bottomRightLong) throws ApplicationException;

    String getNearByKey(double lattitude, double longitude) throws ApplicationException;

    String getNearByKeyPrefix(double lattitude, double longitude) throws ApplicationException;

    String getEnityInformationHashKey();

    String getLocationComplaintsKey(Long locationId);

    String getLocationCategoryComplaintsKey(Long locationId, long categoryId);

    String getGlobalComplaintCounterKey();

    String getGlobalKeyPrefix();

    String getCategoryHourComplaintCounterKey(Date date, Long categoryId);

    String getCategoryKey(Long categoryId);

    String getLocationHourComplaintCounterKey(Date date, Long locationId);

    String getLocationKey(Long locationId);

    String getLocationCategoryHourComplaintCounterKey(Date date, Long locationId, Long categoryId);

    String getLocationCategoryKeyPrefix(Long locationId, Long categoryId);

    String getHourKey(Date date);

    String getPoliticalAdminKey(Long politicalAdminId);

    String getDayComplaintCounterKey(String prefix, Date date);

    String getLast24HourComplaintCounterKey(String prefix, Date date);

    String getMonthComplaintCounterKey(String prefix, Date date);

    String getYearComplaintCounterKey(String prefix, Date date);

    String getTotalComplaintCounterKey(String prefix);

    String getHourComplaintCounterKey(String prefix, Date date);

    List<String> getHourComplaintKeysForTheDay(String prefix, Date date);

    List<String> getHourComplaintKeysForLast24Hours(String prefix, Date date);

    List<String> getDayComplaintKeysForTheMonth(String prefix, Date date);

    List<String> getHourComplaintKeysForLast30Days(String prefix, Date date);

    List<String> getMonthComplaintKeysForTheYear(String prefix, Date date);

    List<String> getYearComplaintKeysForEternitySinceStart(String prefix);

    String getPoliticalBodyAdminTypeHashKey(Long pbTypeId);

    String getPoliticalBodyAdminUrlsKey();
}
