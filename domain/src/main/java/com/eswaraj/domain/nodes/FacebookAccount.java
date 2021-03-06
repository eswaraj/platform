package com.eswaraj.domain.nodes;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

import com.eswaraj.domain.base.BaseNode;

@NodeEntity
@TypeAlias("FacebookAccount")
public class FacebookAccount extends BaseNode {

    @Indexed(indexName = "FacebookUserNameIdx")
    private String userName;

    @Indexed(indexName = "FacebookUserIdIdx", unique = true)
    private String facebookUserId;
    
    private String name;
    
    private String gender;
    
    @Indexed(indexName = "FAEmail")
    private String email;
    
    @RelatedTo(type = "OF_USER")
    private User user;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFacebookUserId() {
		return facebookUserId;
	}

	public void setFacebookUserId(String facebookUserId) {
		this.facebookUserId = facebookUserId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
