package com.eswaraj.cache;

import java.util.Collection;

import com.eswaraj.core.exceptions.ApplicationException;
import com.google.gson.JsonArray;

public interface PoliticalAdminCache {

    void refreshPoliticalBodyAdmin(Long politicalBodyAdminId) throws ApplicationException;

    String getPoliticalBodyAdminById(String politicalBodyAdminId) throws ApplicationException;

    String getPoliticalBodyAdminByUrlIdentifier(String politicalBodyAdminUrlIdentifier) throws ApplicationException;

    JsonArray getPoliticalBodyAdminByIds(Collection<String> politicalBodyAdminId) throws ApplicationException;
}
