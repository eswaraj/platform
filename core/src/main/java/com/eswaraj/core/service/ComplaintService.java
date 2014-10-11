package com.eswaraj.core.service;

import java.util.List;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.messaging.dto.ComplaintMessage;
import com.eswaraj.web.dto.ComplaintDto;
import com.eswaraj.web.dto.ComplaintStatusChangeByPoliticalAdminRequestDto;
import com.eswaraj.web.dto.ComplaintViewdByPoliticalAdminRequestDto;
import com.eswaraj.web.dto.PhotoDto;
import com.eswaraj.web.dto.PoliticalAdminComplaintDto;
import com.eswaraj.web.dto.SaveComplaintRequestDto;
import com.eswaraj.web.dto.comment.CommentSaveRequestDto;
import com.eswaraj.web.dto.comment.CommentSaveResponseDto;

/**
 * Complaint service for complaint related calls
 * @author anuj
 * @data Jun 22, 2014
 */

public interface ComplaintService {
	
	List<ComplaintDto> getPagedUserComplaints(Long personId, int start, int end) throws ApplicationException;
	
	List<ComplaintDto> getAllUserComplaints(Long personId) throws ApplicationException;
	
	List<ComplaintDto> getPagedDeviceComplaints(String deviceId, int start, int end) throws ApplicationException;
	
	List<ComplaintDto> getAllUserComplaints(String deviceId) throws ApplicationException;

    List<ComplaintDto> getAllComplaints(Long start, Long totalComplaints) throws ApplicationException;

    List<ComplaintDto> getAllComplaintsOfLocation(Long locationId, Long start, Long totalComplaints) throws ApplicationException;

    List<PoliticalAdminComplaintDto> getAllComplaintsOfPoliticalAdmin(Long politicalAdmin, Long start, Long totalComplaints) throws ApplicationException;

    PoliticalAdminComplaintDto updateComplaintViewStatus(ComplaintViewdByPoliticalAdminRequestDto complaintViewdByPoliticalAdminRequestDto) throws ApplicationException;

    PoliticalAdminComplaintDto updateComplaintPoliticalAdminStatus(ComplaintStatusChangeByPoliticalAdminRequestDto complaintStatusChangeByPoliticalAdminRequestDto) throws ApplicationException;

    CommentSaveResponseDto commentOnComplaint(CommentSaveRequestDto commentRequestDto) throws ApplicationException;

	ComplaintDto getComplaintById(Long complaintId) throws ApplicationException;
	
    void mergeComplaints(List<Long> complaintIds) throws ApplicationException;

	ComplaintDto saveComplaint(SaveComplaintRequestDto complaintDto) throws ApplicationException;
	
    ComplaintMessage updateLocationAndAdmins(Long complaintId) throws ApplicationException;

	PhotoDto addPhotoToComplaint(Long complaintId, PhotoDto photoDto) throws ApplicationException;

    List<PhotoDto> getComplaintPhotos(Long complaintId) throws ApplicationException;
}
