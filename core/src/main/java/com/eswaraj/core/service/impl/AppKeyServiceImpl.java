package com.eswaraj.core.service.impl;

import org.springframework.stereotype.Component;

import com.eswaraj.core.service.AppKeyService;

@Component
public class AppKeyServiceImpl implements AppKeyService {

    private final String CATEGORY_PREFIX = "Category.";
    private final String CATEGORY_ALL_KEY = CATEGORY_PREFIX + "ALL";

    @Override
    public String getAllCategoriesKey() {
        return CATEGORY_ALL_KEY;
    }

}
