package com.eswaraj.tasks.topology;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;

public abstract class EswarajBaseSpout extends EswarajBaseComponent implements IRichSpout {

	private static final long serialVersionUID = 1L;
	Map<String, Object> configuration = new HashMap<String, Object>();

	private String outputStream;
    protected String componentId;
    private SpoutOutputCollector collector;


    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        this.collector = collector;
        super.init();
    }
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
            Fields fields = new Fields(getFields());
            declarer.declareStream(getOutputStream(), fields);
        }
    }

    protected String[] getFields() {
        return new String[] { "Default" };
    }

	public void setOutputStream(String outputStream) {
		this.outputStream = outputStream;
	}

    @Override
    protected void writeToStream(List<Object> tuple) {
        collector.emit(outputStream, tuple, UUID.randomUUID().toString());
    }

    protected void writeToStream(List<Object> tuple, Object messageId) {
        logInfo("Writing To Stream " + outputStream + " with message id as " + messageId);
        collector.emit(outputStream, tuple, messageId);
    }

    @Override
    protected void writeToTaskStream(int taskId, List<Object> tuple) {
        collector.emitDirect(taskId, outputStream, tuple);
    }

    @Override
    protected void writeToTaskStream(int taskId, List<Object> tuple, Object messageId) {
        collector.emitDirect(taskId, outputStream, tuple);
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public SpoutOutputCollector getCollector() {
        return collector;
    }

    public void setCollector(SpoutOutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void close() {
        super.destroy();
    }

    @Override
    public void activate() {

    }

    @Override
    public void deactivate() {

    }

    @Override
    public void ack(Object msgId) {
    }

    @Override
    public void fail(Object msgId) {
    }

}
