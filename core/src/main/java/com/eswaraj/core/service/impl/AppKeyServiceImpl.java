package com.eswaraj.core.service.impl;

import org.springframework.stereotype.Component;

import com.eswaraj.core.service.AppKeyService;

@Component
public class AppKeyServiceImpl implements AppKeyService {

    private final String CATEGORY_PREFIX = "Category.";
    private final String CATEGORY_ALL_KEY = CATEGORY_PREFIX + "ALL";

    private final String COMPLAINT_PREFIX = "Complaint.";

    @Override
    public String getAllCategoriesKey() {
        return CATEGORY_ALL_KEY;
    }

    @Override
    public String getComplaintObjectKey(Long complaintId) {
        return COMPLAINT_PREFIX + complaintId;
    }

}
