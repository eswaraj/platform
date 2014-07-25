package com.eswaraj.tasks.topology;

import java.util.List;

import backtype.storm.topology.IRichBolt;

/**
 * @author Ravi Sharma
 * @data Jul 25, 2014
 */
public class SpringBoltConfig {

	/**
	 * Id of the Bolt
	 */
	private String componentId;
	
	private IRichBolt bolt;
	
	List<ComponentStream> sourceComponentStreams;
	
	public String getComponentId() {
		return componentId;
	}
	public void setComponentId(String componentId) {
		this.componentId = componentId;
	}
	public IRichBolt getBolt() {
		return bolt;
	}
	public void setBolt(IRichBolt bolt) {
		this.bolt = bolt;
	}
	public List<ComponentStream> getSourceComponentStreams() {
		return sourceComponentStreams;
	}
	public void setSourceComponentStreams(
			List<ComponentStream> sourceComponentStreams) {
		this.sourceComponentStreams = sourceComponentStreams;
	}

	
	
}
