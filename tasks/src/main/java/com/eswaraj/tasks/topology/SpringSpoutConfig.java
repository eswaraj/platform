package com.eswaraj.tasks.topology;

import javax.annotation.PostConstruct;


/**
 * @author Ravi Sharma
 * @data Jul 25, 2014
 */
public class SpringSpoutConfig {

	private String componentId;
	private String outputStream;
	private EswarajBaseSpout spout;

	@PostConstruct
	public void init(){
		spout.setOutputStream(outputStream);
	}
	public String getComponentId() {
		return componentId;
	}

	public void setComponentId(String componentId) {
		this.componentId = componentId;
	}

	public String getOutputStream() {
		return outputStream;
	}
	public void setOutputStream(String outputStream) {
		this.outputStream = outputStream;
	}
	public EswarajBaseSpout getSpout() {
		return spout;
	}

	public void setSpout(EswarajBaseSpout spout) {
		this.spout = spout;
	}

	
}
