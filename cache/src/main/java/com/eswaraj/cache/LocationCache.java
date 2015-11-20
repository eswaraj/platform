package com.eswaraj.cache;

import java.util.Collection;
import java.util.Set;

import com.eswaraj.core.exceptions.ApplicationException;
import com.google.gson.JsonArray;

public interface LocationCache {

    void refreshLocationInfo(long locationId) throws ApplicationException;

    String getLocationInfoById(long locationId) throws ApplicationException;

    String getLocationInfoById(String locationId) throws ApplicationException;

    String getLocationInfoByUrlIdentifier(String urlIdentifier) throws ApplicationException;

    void setLocationPoliticalAdmins(Long locationId, Set<String> pbAdminIds) throws ApplicationException;

    Set<String> getLocationPoliticalAdmins(Long locationId) throws ApplicationException;

    JsonArray getLocationsByIds(Collection<Long> locationIds) throws ApplicationException;
}
