package com.eswaraj.domain.nodes;

import java.util.Date;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

import com.eswaraj.domain.base.BaseNode;

/**
 * Administrators working in an executive body
 * @author ravi
 * @data Jun 19, 2014
 */

@NodeEntity
@TypeAlias("DepartmentAdmin")
public class DepartmentAdmin extends BaseNode {

	@RelatedTo(type="REPORTS_TO")
	private DepartmentAdmin manager;
	
	@RelatedTo(type="WORKS_FOR")
    private Department department;

	@RelatedTo(type="IS")
	private Person person;

	@RelatedTo(type="HAS")
	private DepartmentPost post;
	
	private Date startDate;
	private Date endDate;
	public DepartmentAdmin getManager() {
		return manager;
	}
	public void setManager(DepartmentAdmin manager) {
		this.manager = manager;
	}

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Person getPerson() {
		return person;
	}
	public void setPerson(Person person) {
		this.person = person;
	}
	public DepartmentPost getPost() {
		return post;
	}
	public void setPost(DepartmentPost post) {
		this.post = post;
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
