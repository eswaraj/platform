package com.eswaraj.cache;

import com.eswaraj.core.exceptions.ApplicationException;
import com.google.gson.JsonArray;

public interface CategoryCache {

    void refreshAllCategories() throws ApplicationException;

    String getAllCategories() throws ApplicationException;

    JsonArray getAllCategoryStatsForLocation(Long locationId) throws ApplicationException;
}
