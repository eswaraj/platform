package com.eswaraj.tasks.bolt;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.eswaraj.tasks.topology.EswarajBaseBolt;

public abstract class CounterBolt extends EswarajBaseBolt {

    private static final long serialVersionUID = 1L;
    DateFormat hourFormat = new SimpleDateFormat("yyyyMMddkk");
    DateFormat dayFormat = new SimpleDateFormat("yyyyMMdd");
    DateFormat monthFormat = new SimpleDateFormat("yyyyMM");
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

    private final String buildGlobalKey(DateFormat df, Date date) {
        return keyPrefix + df.format(date);
    }

    protected String buildGlobalHourKey(Date date){
        return buildGlobalKey(hourFormat, date);
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
}
