package com.eswaraj.core.service.impl;

import org.springframework.stereotype.Component;

import com.eswaraj.core.service.AppKeyService;

@Component
public class AppKeyServiceImpl implements AppKeyService {

    private final String CATEGORY_PREFIX = "Category.";
    private final String POLITICAL_ADMIN_PREFIX = "PBA.";
    private final String EXECUTIVE_ADMIN_PREFIX = "EBA.";
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

    @Override
    public String getComplaintObjectKey(String complaintId) {
        return COMPLAINT_PREFIX + complaintId;
    }

    @Override
    public String getPoliticalBodyAdminObjectKey(String politicalBodyAdminId) {
        return POLITICAL_ADMIN_PREFIX + politicalBodyAdminId;
    }

    @Override
    public String getExecutiveBodyAdminObjectKey(String executiveBodyAdminId) {
        return EXECUTIVE_ADMIN_PREFIX + executiveBodyAdminId;
    }

}
