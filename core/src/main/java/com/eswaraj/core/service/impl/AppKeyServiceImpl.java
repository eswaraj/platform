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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.AppKeyService;

@Component
public class AppKeyServiceImpl implements AppKeyService, Serializable {

    private static final long serialVersionUID = 1L;

    private DecimalFormat decimalFormat;
    private DecimalFormat decimalFormatUpto2DecimalPoints;

    private final String CATEGORY_PREFIX = "CG.";

    private final String COUNT = "CNT";

    private final String POLITICAL_ADMIN_URL_KEY = "PBA_URLS";
    private final String POLITICAL_ADMIN_HASH_KEY = "PBA_LIST";
    private final String POLITICAL_ADMIN_PREFIX = "PBA.";
    private final String POLITICAL_ADMIN_TYPE_PREFIX = "PBAT.";
    private final String EXECUTIVE_ADMIN_PREFIX = "EBA.";
    private final String CATEGORY_ALL_KEY = CATEGORY_PREFIX + "ALL";

    private final String COMPLAINT_PREFIX = "CO.";
    private final String PERSON_PREFIX = "P.";
    private final String USER_DAILY_COMPLAINT_COUNT_PREFIX = "UDC.";

    private final String GLOBAL_PREFIX = "Global.";
    private final String LOCATION_PREFIX = "LC.";
    private final String URL = "U";
    private final String URL_PREFIX = URL + ".";
    private final String POLITICAL_BODY_ADMIN = "PBA";
    private final String POLITICAL_BODY_ADMIN_PREFIX = POLITICAL_BODY_ADMIN + ".";

    protected DateFormat hourFormat = new SimpleDateFormat("yyyyMMddkk");
    protected DateFormat dayFormat = new SimpleDateFormat("yyyyMMdd");
    protected DateFormat monthFormat = new SimpleDateFormat("yyyyMM");
    protected DateFormat yearFormat = new SimpleDateFormat("yyyy");

    public AppKeyServiceImpl() {
        decimalFormat = new DecimalFormat("#.000");
        decimalFormat.setRoundingMode(RoundingMode.DOWN);
        decimalFormatUpto2DecimalPoints = new DecimalFormat("#.00");
        decimalFormatUpto2DecimalPoints.setRoundingMode(RoundingMode.DOWN);

    }
    @Override
    public String getAllCategoriesKey() {
        return CATEGORY_ALL_KEY;
    }

    @Override
    public String getComplaintObjectKey(Long complaintId) {
        return COMPLAINT_PREFIX + complaintId;
    }

    @Override
    public String getComplaintObjectKey(String complaintId) {
        return COMPLAINT_PREFIX + complaintId;
    }

    @Override
    public String getPoliticalBodyAdminObjectKey(String politicalBodyAdminId) {
        return POLITICAL_ADMIN_PREFIX + politicalBodyAdminId;
    }

    @Override
    public String getPoliticalBodyAdminUrlKey(String politicalBodyAdminUrlIdentifier) {
        return POLITICAL_ADMIN_PREFIX + URL_PREFIX + politicalBodyAdminUrlIdentifier;
    }

    @Override
    public String getExecutiveBodyAdminObjectKey(String executiveBodyAdminId) {
        return EXECUTIVE_ADMIN_PREFIX + executiveBodyAdminId;
    }

    @Override
    public String getGlobalComplaintCounterKey() {
        return GLOBAL_PREFIX + COUNT;
    }

    @Override
    public String getHourComplaintCounterKey(String prefix, Date date) {
        return getCounterKey(prefix, hourFormat, date);
    }

    @Override
    public String getDayComplaintCounterKey(String prefix, Date date) {
        return getCounterKey(prefix, dayFormat, date);
    }

    private String getCounterKey(String prefix, DateFormat format, Date date) {
        if (StringUtils.isEmpty(prefix)) {
            return format.format(date);
        }
        if (prefix.endsWith(".")) {
            return prefix + format.format(date);
        }
        return prefix + "." + format.format(date);

    }

    @Override
    public String getMonthComplaintCounterKey(String prefix, Date date) {
        return getCounterKey(prefix, monthFormat, date);
    }

    @Override
    public String getYearComplaintCounterKey(String prefix, Date date) {
        return getCounterKey(prefix, yearFormat, date);
    }

    @Override
    public String getTotalComplaintCounterKey(String prefix) {
        if (StringUtils.isEmpty(prefix)) {
            return "Total";
        }
        if (prefix.endsWith(".")) {
            return prefix + "Total";
        }
        return prefix + "." + "Total";
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
        if (StringUtils.isEmpty(prefix)) {
            return "24.HOUR";
        }
        if (prefix.endsWith(".")) {
            return prefix + "24.HOUR";
        }
        return prefix + "." + "24.HOUR";
    }

    @Override
    public List<String> getDayComplaintKeysForTheMonth(String prefix, Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        int totalDaysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        List<String> allDayKeysOfMonth = new ArrayList<>();
        // Add for first of month
        allDayKeysOfMonth.add(getDayComplaintCounterKey(prefix, calendar.getTime()));
        // and then loop through until total day - 2, if 28 days we want to add
        // 1 max 27 times
        // one is already covered above
        for (int i = 0; i < totalDaysInMonth - 1; i++) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            allDayKeysOfMonth.add(getDayComplaintCounterKey(prefix, calendar.getTime()));
        }
        return allDayKeysOfMonth;
    }

    @Override
    public List<String> getHourComplaintKeysForLast30Days(String prefix, Date date) {
        return getHourComplaintKeysForLastNDays(prefix, date, 30);
    }

    @Override
    public List<String> getHourComplaintKeysForLastNDays(String prefix, Date date, int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Date endDate = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, 0 - n);
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
        calendar.setTime(date);
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
        return getCategoryCounterKey(categoryId) + "." + hourFormat.format(date);
    }

    @Override
    public String getCategoryKey(Long categoryId) {
        return CATEGORY_PREFIX + categoryId;
    }

    @Override
    public String getCategoryCounterKey(Long categoryId) {
        return CATEGORY_PREFIX + categoryId + "." + COUNT;
    }

    @Override
    public String getLocationHourComplaintCounterKey(Date date, Long locationId) {
        return getLocationKey(locationId) + "." + hourFormat.format(date);
    }

    @Override
    public String getLocationKey(Long locationId) {
        return getLocationKey(locationId.toString());
    }

    @Override
    public String getLocationPoliticalAdminKey(String locationId) {
        return getLocationKey(locationId) + "." + POLITICAL_BODY_ADMIN;
    }

    @Override
    public String getLocationKey(String locationId) {
        return LOCATION_PREFIX + locationId;
    }

    @Override
    public String getLocationCounterKey(Long locationId) {
        return LOCATION_PREFIX + locationId + "." + COUNT;
    }

    @Override
    public String getLocationUrlKey(String url) {
        return URL_PREFIX + url;
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
    public String getHourKey(Date date) {
        return hourFormat.format(date);
    }

    @Override
    public String getPoliticalAdminKey(Long politicalAdminId) {
        return POLITICAL_BODY_ADMIN_PREFIX + politicalAdminId;
    }

    @Override
    public String getPoliticalAdminCounterKey(Long politicalAdminId) {
        return POLITICAL_BODY_ADMIN_PREFIX + politicalAdminId + "." + COUNT;
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
        int i = 0;
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
    public String getEnityInformationHashKey() {
        return "info";
    }

    @Override
    public String getLocationComplaintsKey(Long locationId) {
        return LOCATION_PREFIX + locationId + ".complaints";
    }

    @Override
    public String getLocationCategoryComplaintsKey(Long locationId, long categoryId) {
        return LOCATION_PREFIX + locationId + "." + CATEGORY_PREFIX + categoryId + ".complaints";
    }

    @Override
    public String getPoliticalBodyAdminTypeHashKey(Long pbTypeId) {
        return POLITICAL_ADMIN_TYPE_PREFIX + pbTypeId;
    }

    @Override
    public String getPoliticalBodyAdminUrlsKey() {
        return POLITICAL_ADMIN_URL_KEY;
    }

    @Override
    public String getPoliticalBodyAdminHashKey() {
        return POLITICAL_ADMIN_HASH_KEY;
    }

    @Override
    public String getCommentListIdForComplaintKey(Long complaintId) {
        return "COM.LIST.CMP." + complaintId;
    }

    @Override
    public String getCommentIdKey(Long commentId) {
        return "COM." + commentId;
    }

    @Override
    public String getCommentIdKey(String commentId) {
        return "COM." + commentId;
    }

    @Override
    public String getAdminCommentListIdForComplaintKey(Long complaintId) {
        return "COM.LIST.CMP.AO." + complaintId;
    }

    @Override
    public String getPersonKey(Long personId) {
        return PERSON_PREFIX + personId;
    }

    @Override
    public String getPersonKey(String personId) {
        return PERSON_PREFIX + personId;
    }

    @Override
    public String getPersonDailyComplaintCountKey(String userId, Date date) {
        return USER_DAILY_COMPLAINT_COUNT_PREFIX + userId + "." + dayFormat.format(date);
    }
}
