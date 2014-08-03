package com.eswaraj.core.service;

import java.util.Date;
import java.util.List;

public interface CounterKeyService {


    String getGlobalHourComplaintCounterKey(Date date);

    String getDayComplaintCounterKey(String prefix, Date date);

    String getLast24HourComplaintCounterKey(String prefix, Date date);

    String getMonthComplaintCounterKey(String prefix, Date date);

    String getYearComplaintCounterKey(String prefix, Date date);

    String getTotalComplaintCounterKey(String prefix);

    String getGlobalKeyPrefix();
    
    
    
    List<String> getHourComplaintKeysForTheDay(String prefix, Date date);

    List<String> getHourComplaintKeysForLast24Hours(String prefix, Date date);

    List<String> getDayComplaintKeysForTheMonth(String prefix, Date date);

    List<String> getMonthComplaintKeysForTheYear(String prefix, Date date);

    List<String> getYearComplaintKeysForEternitySinceStart(String prefix);


}
