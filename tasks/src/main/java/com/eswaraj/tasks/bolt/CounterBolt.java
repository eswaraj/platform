package com.eswaraj.tasks.bolt;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.eswaraj.tasks.topology.EswarajBaseBolt;

public abstract class CounterBolt extends EswarajBaseBolt {

    private static final long serialVersionUID = 1L;
    protected DateFormat hourFormat = new SimpleDateFormat("yyyyMMddkk");
    protected DateFormat dayFormat = new SimpleDateFormat("yyyyMMdd");
    protected DateFormat monthFormat = new SimpleDateFormat("yyyyMM");
    private String keyPrefix;
    private String keyPrefixFieldName;

    public String getKeyPrefix() {
        return keyPrefix;
    }

    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }

    public String getKeyPrefixFieldName() {
        return keyPrefixFieldName;
    }

    public void setKeyPrefixFieldName(String keyPrefixFieldName) {
        this.keyPrefixFieldName = keyPrefixFieldName;
    }

    protected final String buildGlobalKey(DateFormat df, Date date) {
        return keyPrefix + df.format(date);
    }

    protected String buildGlobalDayKey(Date date) {
        return buildGlobalKey(dayFormat, date);
    }

    protected String buildGlobalMonthKey(Date date) {
        return buildGlobalKey(monthFormat, date);
    }

    protected String buildAllTimeKey() {
        return keyPrefix + "AllTime";
    }

    protected String buildAllTimeKey(Long id) {
        return keyPrefix + id + ".AllTime";
    }

    protected String buildGlobalAllTimeKey() {
        return buildAllTimeKey();
    }

    private final String buildStateKey(DateFormat df, Date date, Long stateId) {
        return keyPrefix + stateId + "." + df.format(date);
    }
    protected String buildStateHourKey(Date date, Long stateId) {
        return buildStateKey(hourFormat, date, stateId);
    }

    protected String buildStateDayKey(Date date, Long stateId) {
        return buildStateKey(dayFormat, date, stateId);
    }

    protected String buildStateMonthKey(Date date, Long stateId) {
        return buildStateKey(monthFormat, date, stateId);
    }

    protected String buildStateAllTimeKey(Date date, Long stateId) {
        return keyPrefix + stateId + ".AllTime";
    }

    protected Long getStartOfHour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MILLISECOND, 1);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        logInfo("startOfHour = " + calendar.getTimeInMillis() + " , " + calendar.getTime());
        return calendar.getTimeInMillis();
    }

    protected Long getEndOfHour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        logInfo("endOfHour = " + calendar.getTimeInMillis() + " , " + calendar.getTime());
        return calendar.getTimeInMillis();
    }
}
