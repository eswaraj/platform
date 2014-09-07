package com.eswaraj.core.service;

import java.util.Date;
import java.util.List;

public interface CounterKeyService {


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


}
