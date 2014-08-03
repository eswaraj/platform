package com.eswaraj.domain.nodes;

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

    @RelatedTo(type = "ATTACHED_TO", elementClass = Device.class)
	private Person person;

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

    @Override
    public String toString() {
        return "User [person=" + person + ", id=" + id + "]";
    }
}
