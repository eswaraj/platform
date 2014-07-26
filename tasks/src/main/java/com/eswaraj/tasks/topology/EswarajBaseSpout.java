package com.eswaraj.tasks.topology;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;

public abstract class EswarajBaseSpout extends BaseRichSpout {

	private static final long serialVersionUID = 1L;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
	Map<String, Object> configuration = new HashMap<String, Object>();

	private String outputStream;
    protected String componentId;

	@Override
	public Map<String, Object> getComponentConfiguration() {
		return configuration;
	}

	public String getOutputStream() {
		return outputStream;
	}

    @Override
    public final void declareOutputFields(OutputFieldsDeclarer declarer) {
        if (outputStream != null) {
            Fields fields = new Fields("Test");
            declarer.declareStream(getOutputStream(), fields);
        }
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
	protected void logInfo(String message){
		logger.info(message);
	}
	protected void logWarning(String message){
		logger.warn(message);
	}
	protected void logError(String message){
		logger.error(message);
	}

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

}
