package com.eswaraj.cache;

import com.eswaraj.core.exceptions.ApplicationException;

public interface ComplaintCache {

    void refreshComplaintInfo(long complaintId) throws ApplicationException;

    String getComplaintById(long complaintId) throws ApplicationException;

    String getComplaintById(String complaintId) throws ApplicationException;
}
