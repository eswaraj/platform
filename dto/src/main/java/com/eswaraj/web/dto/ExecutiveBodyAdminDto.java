package com.eswaraj.web.dto;

import java.util.Date;

/**
 * Administrators working in an executive body
 * @author ravi
 * @data Jun 19, 2014
 */

public class ExecutiveBodyAdminDto extends BaseDto {

	private static final long serialVersionUID = 1L;

	private Long managerId;
	
	private Long executiveBodyId;

	private Long personId;

	private Long postId;
	
	private Date startDate;
	private Date endDate;
	
	public Long getManagerId() {
		return managerId;
	}
	public void setManagerId(Long managerId) {
		this.managerId = managerId;
	}
	public Long getExecutiveBodyId() {
		return executiveBodyId;
	}
	public void setExecutiveBodyId(Long executiveBodyId) {
		this.executiveBodyId = executiveBodyId;
	}
	public Long getPersonId() {
		return personId;
	}
	public void setPersonId(Long personId) {
		this.personId = personId;
	}
	public Long getPostId() {
		return postId;
	}
	public void setPostId(Long postId) {
		this.postId = postId;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	

}
