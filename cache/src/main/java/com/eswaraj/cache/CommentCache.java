package com.eswaraj.cache;

import com.eswaraj.core.exceptions.ApplicationException;

public interface CommentCache {

    /**
     * Refresh a comment into Cache from Database
     * 
     * @param complaintId
     * @param commentId
     * @throws ApplicationException
     */
    void refreshComplaintComment(long complaintId, long commentId) throws ApplicationException;

    /**
     * Refresh complaint's total comment count
     * 
     * @param complaintId
     * @throws ApplicationException
     */
    void refreshComplaintCommentCount(long complaintId) throws ApplicationException;

    /**
     * Get Complaint's Comment
     * 
     * @param complaintId
     * @param start
     * @param total
     * @return
     * @throws ApplicationException
     */
    String getComplaintComments(long complaintId, int start, int total, String order) throws ApplicationException;

    /**
     * Get Complaint's Comment added by Admins
     * 
     * @param complaintId
     * @param start
     * @param total
     * @param order
     * @return
     * @throws ApplicationException
     */
    String getComplaintAdminComments(long complaintId, int start, int total, String order) throws ApplicationException;
}
