package com.eswaraj.tasks.topology;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;

/**
 * All bolts should extend this class
 * 
 * @author Ravi Sharma
 * @data Jul 25, 2014
 */

public abstract class EswarajBaseBolt extends EswarajBaseComponent implements IRichBolt {

	private static final long serialVersionUID = 1L;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    public EswarajBaseBolt() {}

    protected OutputCollector outputCollector;
    protected String outputStream;
    protected String componentId;
    // key - CompnenetId , Value - Stream
    Map<String, String> sourceComponentStreams;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.outputCollector = collector;
        super.init();
    }

    @Override
    public final void declareOutputFields(OutputFieldsDeclarer declarer) {
        logInfo("outputStream = " + outputStream);
        if (outputStream != null) {
            declarer.declareStream(outputStream, new Fields(getFields()));
        }
    }

    public enum Result {
        Success, Failed;
    }

    protected abstract Result processTuple(Tuple inputTuple);

    @Override
    public final void execute(Tuple inputTuple) {
        logDebug("Received Message {}", inputTuple.getMessageId());
        getTupleThreadLocal().set(inputTuple);
        try {
            Result result = processTuple(inputTuple);
            if (Result.Success.equals(result)) {
                acknowledgeTuple(inputTuple);
            } else {
                failTuple(inputTuple);
            }
        } catch (Throwable t) {
            failTuple(inputTuple);
        } finally {
            getTupleThreadLocal().remove();
        }
    }

    protected String[] getFields() {
        return new String[] { "Default" };
    }

    protected void writeToStream(Tuple anchor, List<Object> tuple) {
        logDebug("Writing To Stream {}", outputStream);
        outputCollector.emit(outputStream, anchor, tuple);
    }

    protected void writeToTaskStream(int taskId, Tuple anchor, List<Object> tuple) {
        outputCollector.emitDirect(taskId, outputStream, anchor, tuple);
    }

    private void acknowledgeTuple(Tuple input) {
        logDebug("acknowledgeTuple : " + printTuple(input));
        outputCollector.ack(input);
    }

    private void failTuple(Tuple input) {
        logInfo("***Failed acknowledgeTuple : " + printTuple(input));
        outputCollector.fail(input);
    }

    protected String printTuple(Tuple input) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("getSourceComponent : " + input.getSourceComponent() + " , ");
        stringBuilder.append("getSourceStreamId : " + input.getSourceStreamId() + " , ");
        stringBuilder.append("getSourceGlobalStreamid : " + input.getSourceGlobalStreamid() + " , ");
        stringBuilder.append("getSourceTask : " + input.getSourceTask() + " , ");
        stringBuilder.append("getMessageId : " + input.getMessageId() + " , ");
        stringBuilder.append("getMessageId.getAnchors : " + input.getMessageId().getAnchors() + " , ");
        stringBuilder.append("getMessageId.getAnchorsToIds : " + input.getMessageId().getAnchorsToIds() + " , ");
        return stringBuilder.toString();
    }

    @Override
    public void cleanup() {
        super.destroy();
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }

    public String getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(String outputStream) {
        this.outputStream = outputStream;
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public Map<String, String> getSourceComponentStreams() {
        return sourceComponentStreams;
    }

    public void setSourceComponentStreams(Map<String, String> sourceComponentStreams) {
        this.sourceComponentStreams = sourceComponentStreams;
    }

    protected Long getStartOfHour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MILLISECOND, 1);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        return calendar.getTimeInMillis();
    }

    protected Long getEndOfHour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        return calendar.getTimeInMillis();
    }

}
