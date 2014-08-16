package com.eswaraj.core.service.impl;

import com.eswaraj.core.service.AppKeyService;

public class AppKeyServiceImpl implements AppKeyService {

    private final String CATEGORY_PREFIX = "Category.";
    private final String CATEGORY_ALL_KEY = CATEGORY_PREFIX + "ALL";

    @Override
    public String getAllCategoriesKey() {
        return CATEGORY_ALL_KEY;
    }

}
