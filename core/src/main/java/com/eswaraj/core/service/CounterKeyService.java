package com.eswaraj.core.service;

import java.util.Date;
import java.util.List;

public interface CounterKeyService {


    String getGlobalHourComplaintCounterKey(Date date);

    String getGlobalKeyPrefix();

    String getCategoryHourComplaintCounterKey(Date date, Long categoryId);

    String getCategoryKeyPrefix(Long categoryId);

    String getLocationHourComplaintCounterKey(Date date, Long locationId);

    String getLocationKeyPrefix(Long locationId);

    String getLocationCategoryHourComplaintCounterKey(Date date, Long locationId, Long categoryId);

    String getLocationCategoryKeyPrefix(Long locationId, Long categoryId);

    String getPoliticalAdminHourComplaintCounterKey(Date date, Long politicalAdminId);

    String getPoliticalAdminKeyPrefix(Long politicalAdminId);

    String getDayComplaintCounterKey(String prefix, Date date);

    String getLast24HourComplaintCounterKey(String prefix, Date date);

    String getMonthComplaintCounterKey(String prefix, Date date);

    String getYearComplaintCounterKey(String prefix, Date date);

    String getTotalComplaintCounterKey(String prefix);

    
    
    String getHourComplaintCounterKey(String prefix, Date date);

    List<String> getHourComplaintKeysForTheDay(String prefix, Date date);

    List<String> getHourComplaintKeysForLast24Hours(String prefix, Date date);

    List<String> getDayComplaintKeysForTheMonth(String prefix, Date date);

    List<String> getMonthComplaintKeysForTheYear(String prefix, Date date);

    List<String> getYearComplaintKeysForEternitySinceStart(String prefix);


}
