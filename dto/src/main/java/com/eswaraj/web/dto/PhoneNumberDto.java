package com.eswaraj.web.dto;

import org.springframework.data.neo4j.annotation.NodeEntity;


@NodeEntity
public class PhoneNumberDto extends BaseDto{

	private static final long serialVersionUID = 1L;
	private String countryCode;
	private String stdCode;
	private String phoneNumber;
	
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public String getStdCode() {
		return stdCode;
	}
	public void setStdCode(String stdCode) {
		this.stdCode = stdCode;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	@Override
	public String toString() {
		return "PhoneNumber [countryCode=" + countryCode + ", stdCode=" + stdCode + ", phoneNumber=" + phoneNumber + ", id=" + id + "]";
	}
	

}
