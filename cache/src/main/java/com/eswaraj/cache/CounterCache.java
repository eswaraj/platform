package com.eswaraj.cache;

import java.util.Date;
import java.util.List;

import com.eswaraj.core.exceptions.ApplicationException;
import com.google.gson.JsonObject;

public interface CounterCache {

    JsonObject getLast30DayLocationCounters(Long locationId, Date endDate) throws ApplicationException;

    JsonObject getLastNDayLocationCounters(Long locationId, Date endDate, int NumberOfDays) throws ApplicationException;

    Long getLocationComplaintCounter(Long locationId) throws ApplicationException;

    Long getLocationCategoryComplaintCounter(Long locationId, Long categoryId) throws ApplicationException;

    List<Long> getLocationCategoryComplaintCounter(Long locationId, List<Long> categoryIds) throws ApplicationException;
}
