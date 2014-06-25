package com.eswaraj.domain.nodes;import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

import com.eswaraj.domain.base.BaseNode;
import com.eswaraj.domain.nodes.division.Boundary;

/**
 * Represents an executive body like Water Department or Fire Department
 * @author anuj
 * @date Jan 28, 2014
 *
 */

@NodeEntity
public class ExecutiveBody extends BaseNode {

	private String name;
	private Address address;
	private Boundary boundary;
	@RelatedTo(type="CHILD_OF")
    private ExecutiveBody parentExecutiveBody;
	@RelatedTo(type="BELONGS_TO")
    private Department department;
	private boolean root;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	public Boundary getBoundary() {
		return boundary;
	}
	public void setBoundary(Boundary boundary) {
		this.boundary = boundary;
	}
	public ExecutiveBody getParentExecutiveBody() {
		return parentExecutiveBody;
	}
	public void setParentExecutiveBody(ExecutiveBody parentExecutiveBody) {
		this.parentExecutiveBody = parentExecutiveBody;
	}
	public boolean isRoot() {
		return root;
	}
	public void setRoot(boolean root) {
		this.root = root;
	}
	public Department getDepartment() {
		return department;
	}
	public void setDepartment(Department department) {
		this.department = department;
	}
	@Override
	public String toString() {
		return "ExecutiveBody [name=" + name + ", address=" + address + ", boundary=" + boundary + ", parentExecutiveBody=" + parentExecutiveBody + ", root="
				+ root + ", id=" + id + "]";
	}

}
