package com.eswaraj.cache;

import com.eswaraj.core.exceptions.ApplicationException;

public interface CategoryCache {

    void refreshAllCategories() throws ApplicationException;

    String getAllCategories() throws ApplicationException;
}
