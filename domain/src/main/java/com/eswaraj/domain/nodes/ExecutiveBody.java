package com.eswaraj.domain.nodes;

import java.util.List;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.RelatedToVia;

import com.eswaraj.domain.base.BaseLocationNode;
import com.eswaraj.domain.nodes.division.Boundary;

/**
 * Represents an executive body like Water Department or Fire Department
 * @author anuj
 * @date Jan 26, 2014
 *
 */

public class ExecutiveBody extends BaseLocationNode {

	private String name;
	private Address address;
	private Boundary boundary;
	@RelatedToVia(type="BELONGS_TO", direction=Direction.INCOMING)
	private List<Post> posts;

	
}
