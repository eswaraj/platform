package com.eswaraj.domain.nodes;

import java.util.Set;

import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

import com.eswaraj.domain.base.BaseNode;

/**
 * A user of the system which will be linked to person
 * 
 * @author ravi
 * @date Jul 20, 2014
 *
 */
@NodeEntity
public class User extends BaseNode {

	private Person person;
	@RelatedTo(type="OF_USER", elementClass=FacebookAccount.class)
	private Set<FacebookAccount> facebookAccounts;
	@RelatedTo(type="OF_USER", elementClass=Device.class)
	private Set<Device> devices;

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public Set<FacebookAccount> getFacebookAccounts() {
		return facebookAccounts;
	}

	public void setFacebookAccounts(Set<FacebookAccount> facebookAccounts) {
		this.facebookAccounts = facebookAccounts;
	}

	public Set<Device> getDevices() {
		return devices;
	}

	public void setDevices(Set<Device> devices) {
		this.devices = devices;
	}
}
