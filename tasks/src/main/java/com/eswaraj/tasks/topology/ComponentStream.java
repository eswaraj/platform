package com.eswaraj.tasks.topology;

/**
 * @author Ravi Sharma
 * @data Jul 25, 2014
 */
public class ComponentStream {

	/**
	 * Stream of the Bolt from where Bolt will read data
	 */
	private String sourceStream;
	/**
	 * Component If of stream Source 
	 */
	private String sourceComponentId;
	
	public String getSourceStream() {
		return sourceStream;
	}
	public void setSourceStream(String sourceStream) {
		this.sourceStream = sourceStream;
	}
	public String getSourceComponentId() {
		return sourceComponentId;
	}
	public void setSourceComponentId(String sourceComponentId) {
		this.sourceComponentId = sourceComponentId;
	}
}
