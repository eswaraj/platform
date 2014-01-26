package com.eswaraj.domain.nodes;

import org.springframework.data.neo4j.annotation.NodeEntity;

import com.eswaraj.domain.base.BaseNode;

/**
 * Post of the administrator
 * @author anuj
 * @date Jan 18, 2014
 *
 */
@NodeEntity
public class Post extends BaseNode {

	private String name;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
