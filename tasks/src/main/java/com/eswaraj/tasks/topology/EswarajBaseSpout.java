package com.eswaraj.tasks.topology;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.topology.base.BaseRichSpout;

public abstract class EswarajBaseSpout extends BaseRichSpout {

	private static final long serialVersionUID = 1L;
	Map<String, Object> configuration = new HashMap<String, Object>();
	private String outputStream;
	
	@Override
	public Map<String, Object> getComponentConfiguration() {
		return configuration;
	}

	public String getOutputStream() {
		return outputStream;
	}

	public void setOutputStream(String outputStream) {
		this.outputStream = outputStream;
	}
	protected void writeToStream(SpoutOutputCollector collector, List<Object> tuple){
		collector.emit(outputStream, tuple);
	}
	protected void writeToStream(SpoutOutputCollector collector, List<Object> tuple, Object messageId){
		collector.emit(outputStream, tuple, messageId);
	}
	
	protected void writeToTaskStream(SpoutOutputCollector collector, int taskId, List<Object> tuple){
		collector.emitDirect(taskId, outputStream, tuple);
	}
	protected void writeToTaskStream(SpoutOutputCollector collector, int taskId, List<Object> tuple, Object messageId){
		collector.emitDirect(taskId, outputStream, tuple);
	}

}
