package com.eswaraj.domain.nodes;

import java.io.Serializable;

import com.eswaraj.domain.base.BaseNode;


public class PhoneNumber extends BaseNode implements Serializable{

	private static final long serialVersionUID = 1L;
	private String countryCode;
	private String stdCode;
	private String phoneNumber;
	private PhoneType phoneType;
	
	public static enum PhoneType{
		Landline, Mobile
	}
	
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
	public PhoneType getPhoneType() {
		return phoneType;
	}
	public void setPhoneType(PhoneType phoneType) {
		this.phoneType = phoneType;
	}
	@Override
	public String toString() {
		return "PhoneNumber [countryCode=" + countryCode + ", stdCode=" + stdCode + ", phoneNumber=" + phoneNumber + ", id=" + id + "]";
	}
	

}
