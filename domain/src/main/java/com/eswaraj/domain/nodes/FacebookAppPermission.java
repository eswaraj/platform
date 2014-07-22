package com.eswaraj.domain.nodes;

import java.util.Date;

import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

import com.eswaraj.domain.base.BaseNode;

@NodeEntity
public class FacebookAppPermission extends BaseNode {

	private String token;
	private Date expireTime;
	@RelatedTo(type = "FOR_FB_ACCOUNT")
	private FacebookAccount FacebookAccount;
	@RelatedTo(type = "GIVEN_TO_FB_PP")
	private FacebookApp facebookApp;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}

	public FacebookAccount getFacebookAccount() {
		return FacebookAccount;
	}

	public void setFacebookAccount(FacebookAccount facebookAccount) {
		FacebookAccount = facebookAccount;
	}

	public FacebookApp getFacebookApp() {
		return facebookApp;
	}

	public void setFacebookApp(FacebookApp facebookApp) {
		this.facebookApp = facebookApp;
	}

}
