package com.eswaraj.core.service;

public interface AppKeyService {

    String getAllCategoriesKey();

    String getComplaintObjectKey(Long complaintId);

    String getComplaintObjectKey(String complaintId);

    String getPoliticalBodyAdminObjectKey(String politicalBodyAdminId);

    String getExecutiveBodyAdminObjectKey(String executiveBodyAdminId);

}
