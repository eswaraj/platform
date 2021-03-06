package com.eswaraj.domain.nodes.relationships;

import java.util.Date;

import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;

import com.eswaraj.domain.nodes.FacebookAccount;
import com.eswaraj.domain.nodes.FacebookApp;

@RelationshipEntity(type = "FB_APP_PERMISSION")
public class FacebookAppPermission {

    @GraphId
    protected Long id;

    @Indexed
	private String token;
	private Date expireTime;
    private Date lastLoginTime;
    @StartNode
    @Fetch
    private FacebookAccount FacebookAccount;
    @EndNode
    @Fetch
    private FacebookApp facebookApp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    @Override
    public String toString() {
        return "FacebookAppPermission [id=" + id + ", token=" + token + ", expireTime=" + expireTime + ", FacebookAccount=" + FacebookAccount + ", facebookApp=" + facebookApp + "]";
    }

}
