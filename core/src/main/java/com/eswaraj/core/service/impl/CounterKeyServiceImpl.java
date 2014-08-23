package com.eswaraj.core.service.impl;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.eswaraj.core.service.CounterKeyService;

@Component
public class CounterKeyServiceImpl implements CounterKeyService, Serializable {

    private static final long serialVersionUID = 1L;
    private final String GLOBAL_PREFIX = "Global.";
    private final String CATEGORY_PREFIX = "Category.";
    private final String LOCATION_PREFIX = "Location.";
    private final String POLITICAL_BODY_ADMIN_PREFIX = "PBA.";

    protected DateFormat hourFormat = new SimpleDateFormat("yyyyMMddkk");
    protected DateFormat dayFormat = new SimpleDateFormat("yyyyMMdd");
    protected DateFormat monthFormat = new SimpleDateFormat("yyyyMM");
    protected DateFormat yearFormat = new SimpleDateFormat("yyyy");

    @Override
    public String getGlobalHourComplaintCounterKey(Date date) {
        return GLOBAL_PREFIX + hourFormat.format(date);
    }

    @Override
    public String getHourComplaintCounterKey(String prefix, Date date) {
        return prefix + hourFormat.format(date);
    }

    @Override
    public String getDayComplaintCounterKey(String prefix, Date date) {
        return prefix + dayFormat.format(date);
    }

    @Override
    public String getMonthComplaintCounterKey(String prefix, Date date) {
        return prefix + monthFormat.format(date);
    }

    @Override
    public String getYearComplaintCounterKey(String prefix, Date date) {
        return prefix + yearFormat.format(date);
    }

    @Override
    public String getTotalComplaintCounterKey(String prefix) {
        return prefix + "Total";
    }

    @Override
    public String getGlobalKeyPrefix() {
        return GLOBAL_PREFIX;
    }

    @Override
    public List<String> getHourComplaintKeysForTheDay(String prefix, Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        return getHourComplaintKeysFor24Hours(prefix, calendar);
    }

    @Override
    public List<String> getHourComplaintKeysForLast24Hours(String prefix, Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, -23);
        return getHourComplaintKeysFor24Hours(prefix, calendar);
    }

    private List<String> getHourComplaintKeysFor24Hours(String prefix, Calendar startDateCalendar) {
        List<String> returnList = new ArrayList<>();
        returnList.add(getHourComplaintCounterKey(prefix, startDateCalendar.getTime()));
        for (int i = 0; i < 23; i++) {
            startDateCalendar.add(Calendar.HOUR_OF_DAY, 1);
            returnList.add(getHourComplaintCounterKey(prefix, startDateCalendar.getTime()));
        }
        return returnList;
    }

    @Override
    public String getLast24HourComplaintCounterKey(String prefix, Date date) {
        return prefix + "24.HOUR";
    }

    @Override
    public List<String> getDayComplaintKeysForTheMonth(String prefix, Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        int totalDaysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        List<String> allDayKeysOfMonth = new ArrayList<>();
        // Add for first of month
        allDayKeysOfMonth.add(getDayComplaintCounterKey(prefix, calendar.getTime()));
        //and then loop through until total day - 2, if 28 days we want to add 1 max 27 times
        //one is already covered above
        for (int i = 0; i < totalDaysInMonth - 1; i++) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            allDayKeysOfMonth.add(getDayComplaintCounterKey(prefix, calendar.getTime()));
        }
        return allDayKeysOfMonth;
    }

    @Override
    public List<String> getHourComplaintKeysForLast30Days(String prefix, Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Date endDate = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, -30);
        Date startDate = calendar.getTime();
        return getDayComplaintKeys(prefix, startDate, endDate);
    }

    private List<String> getDayComplaintKeys(String prefix, Date startDate, Date endDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        List<String> allDayKeysOfMonth = new ArrayList<>();
        // Add for first of month
        allDayKeysOfMonth.add(getDayComplaintCounterKey(prefix, calendar.getTime()));
        // and then loop through until total day - 2, if 28 days we want to add
        // 1 max 27 times
        // one is already covered above
        while (calendar.getTime().before(endDate)) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            allDayKeysOfMonth.add(getDayComplaintCounterKey(prefix, calendar.getTime()));
        }
        return allDayKeysOfMonth;
    }

    @Override
    public List<String> getMonthComplaintKeysForTheYear(String prefix, Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, 0);

        int totalMonths = 12;

        List<String> allMonthKeysOfYear = new ArrayList<>();
        // Add for first of month
        allMonthKeysOfYear.add(getMonthComplaintCounterKey(prefix, calendar.getTime()));
        for (int i = 0; i < totalMonths - 1; i++) {
            calendar.add(Calendar.MONTH, 1);
            allMonthKeysOfYear.add(getMonthComplaintCounterKey(prefix, calendar.getTime()));
        }
        return allMonthKeysOfYear;
    }

    @Override
    public List<String> getYearComplaintKeysForEternitySinceStart(String prefix) {
        Calendar calendar = Calendar.getInstance();
        int startYear = 2014;
        int endYear = calendar.get(Calendar.YEAR);

        List<String> allYearKeys = new ArrayList<>(endYear - startYear + 1);
        for (int year = startYear; year <= endYear; year++) {
            calendar.set(Calendar.YEAR, year);
            allYearKeys.add(getYearComplaintCounterKey(prefix, calendar.getTime()));
        }
        return allYearKeys;
    }

    @Override
    public String getCategoryHourComplaintCounterKey(Date date, Long categoryId) {
        return getCategoryKeyPrefix(categoryId) + hourFormat.format(date);
    }

    @Override
    public String getCategoryKeyPrefix(Long categoryId) {
        return CATEGORY_PREFIX + categoryId + ".";
    }

    @Override
    public String getLocationHourComplaintCounterKey(Date date, Long locationId) {
        return getLocationKeyPrefix(locationId) + hourFormat.format(date);
    }

    @Override
    public String getLocationKeyPrefix(Long locationId) {
        return LOCATION_PREFIX + locationId + ".";
    }

    @Override
    public String getLocationCategoryHourComplaintCounterKey(Date date, Long locationId, Long categoryId) {
        return getLocationCategoryKeyPrefix(locationId, categoryId) + hourFormat.format(date);
    }

    @Override
    public String getLocationCategoryKeyPrefix(Long locationId, Long categoryId) {
        return LOCATION_PREFIX + locationId + "." + CATEGORY_PREFIX + categoryId + ".";
    }

    @Override
    public String getPoliticalAdminHourComplaintCounterKey(Date date, Long getPoliticalAdminKeyPrefix) {
        return getPoliticalAdminKeyPrefix(getPoliticalAdminKeyPrefix) + hourFormat.format(date);
    }

    @Override
    public String getPoliticalAdminKeyPrefix(Long politicalAdminId) {
        return POLITICAL_BODY_ADMIN_PREFIX + politicalAdminId + ".";
    }


}
