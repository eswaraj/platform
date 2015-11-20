package com.eswaraj.domain.nodes;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

import com.eswaraj.domain.base.BaseNode;


/**
 * A executive department 
 * @author ravi
 * @date Jan 18, 2014
 *
 */
@NodeEntity
@TypeAlias("Department")
public class Department extends BaseNode {

	
	private String name;
	private String description;
    @RelatedTo(type = "UNDER")
    private Department parentDepartment;
    @RelatedTo(type = "DEPT_ADDRESS")
    @Fetch
    private Address address;
    private boolean root;

    private String email;
    private Integer level;

    private String fbPage;
    private String twitterHandle;
    private String website;

    private String landLine1;
    private String landLine2;
    private String mobile1;
    private String mobile2;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public boolean isRoot() {
        return root;
    }

    public void setRoot(boolean root) {
        this.root = root;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFbPage() {
        return fbPage;
    }

    public void setFbPage(String fbPage) {
        this.fbPage = fbPage;
    }

    public String getTwitterHandle() {
        return twitterHandle;
    }

    public void setTwitterHandle(String twitterHandle) {
        this.twitterHandle = twitterHandle;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getLandLine1() {
        return landLine1;
    }

    public void setLandLine1(String landLine1) {
        this.landLine1 = landLine1;
    }

    public String getLandLine2() {
        return landLine2;
    }

    public void setLandLine2(String landLine2) {
        this.landLine2 = landLine2;
    }

    public String getMobile1() {
        return mobile1;
    }

    public void setMobile1(String mobile1) {
        this.mobile1 = mobile1;
    }

    public String getMobile2() {
        return mobile2;
    }

    public void setMobile2(String mobile2) {
        this.mobile2 = mobile2;
    }

    public Department getParentDepartment() {
        return parentDepartment;
    }

    public void setParentDepartment(Department parentDepartment) {
        this.parentDepartment = parentDepartment;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
    @Override
    public String toString() {
        return "Department [name=" + name + ", description=" + description + ", parentDepartment=" + parentDepartment + ", address=" + address + ", root=" + root + ", email=" + email + ", level="
                + level + ", fbPage=" + fbPage + ", twitterHandle=" + twitterHandle + ", website=" + website + ", landLine1=" + landLine1 + ", landLine2=" + landLine2 + ", mobile1=" + mobile1
                + ", mobile2=" + mobile2 + "]";
    }
}
