package com.eswaraj.web.dto;




/**
 * DTO for a complaint that has been made by a user.
 * @author anuj
 * @data Jun 22, 2014
 */

public class SaveComplaintResponseDto extends ComplaintDto {

	private static final long serialVersionUID = 1L;
	
	private String deviceId;
	private String userId;
	private String deviceTypeRef;//Android or Iphone
	private String name;
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getDeviceTypeRef() {
		return deviceTypeRef;
	}
	public void setDeviceTypeRef(String deviceTypeRef) {
		this.deviceTypeRef = deviceTypeRef;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
