package com.eswaraj.core.service;

import com.eswaraj.core.exceptions.ApplicationException;
import com.google.gson.JsonObject;

/**
 * These services will be called by Batch process usually to update the cache.
 * Not using any Java DTO , directly JsonObject
 * 
 * @author Ravi
 *
 */
public interface StormCacheAppServices {

    JsonObject getCompleteLocationInfo(Long locationId) throws ApplicationException;

    JsonObject getCompleteComplaintInfo(Long complaintId) throws ApplicationException;

    JsonObject getPoliticalBodyAdmin(Long politicalBodyAdminId) throws ApplicationException;

    JsonObject getExecutiveBodyAdmin(Long executiveBodyAdminId) throws ApplicationException;

    JsonObject getComment(Long commentId) throws ApplicationException;

    JsonObject getPerson(Long personId) throws ApplicationException;

}
