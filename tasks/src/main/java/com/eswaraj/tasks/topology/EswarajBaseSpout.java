package com.eswaraj.tasks.topology;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;

import com.eswaraj.tasks.spout.mesage.id.MessageId;

public abstract class EswarajBaseSpout extends EswarajBaseComponent implements IRichSpout {

	private static final long serialVersionUID = 1L;
	Map<String, Object> configuration = new HashMap<String, Object>();

	private String outputStream;
    protected String componentId;
    private SpoutOutputCollector collector;
    private int retry;


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

    public abstract void getNextTuple();
    @Override
    public final void nextTuple() {
        try {
            getNextTuple();
        } catch (Exception e) {
            logError("Unable to get Next Tuple", e);
        }

    }

    protected MessageId<List<Object>> writeToStream(List<Object> tuple) {
        MessageId<List<Object>> messageId = new MessageId<>();
        messageId.setData(tuple);
        collector.emit(outputStream, tuple, messageId);
        logInfo("Mesage Written by Spout :  {}", messageId);
        return messageId;
    }

    protected void writeToStream(List<Object> tuple, Object messageId) {
        logInfo("Writing To Stream " + outputStream + " with message id as " + messageId);
        collector.emit(outputStream, tuple, messageId);
    }

    protected void writeToTaskStream(int taskId, List<Object> tuple) {
        collector.emitDirect(taskId, outputStream, tuple);
    }

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
    public final void ack(Object msgId) {
        logInfo("********************************");
        logInfo("Message {} has been processed", msgId + " , " + msgId.getClass());
        onAck(msgId);
        logInfo("********************************");

    }

    protected void onAck(Object msgId) {

    }

    protected void onFail(Object msgId) {

    }

    @Override
    public final void fail(Object msgId) {
        logInfo("********************************");
        logInfo("Message {} has been failed", msgId + " , " + msgId.getClass());
        if (getRetry() > 0) {
            logDebug("Retry count set to {} so will see if i can retry", getRetry());
            // If retry count set more then 0
            if (msgId instanceof MessageId) {
                MessageId messageId = (MessageId) msgId;
                logDebug("current Retry count is {} " + messageId.getRetryCount());
                if (messageId.getRetryCount() < getRetry()) {
                    logDebug("Retrying {} " + messageId);
                    messageId.setRetryCount(messageId.getRetryCount() + 1);
                    writeToStream((List<Object>) messageId.getData(), messageId);
                }
            } else {
                logDebug("msgId is not of type MessageId so can not retry");
            }

        } else {
            logDebug("Message Failed and i will not retry it : {}", msgId);
        }
        onFail(msgId);
        logInfo("********************************");
    }

    public int getRetry() {
        return retry;
    }

    public void setRetry(int retry) {
        this.retry = retry;
    }

}
