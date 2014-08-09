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
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
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
        //initializeRedisService("cache.vyaut5.0001.usw2.cache.amazonaws.com", 6379);
        //initializeDbService("http://ip-172-31-47-87.us-west-2.compute.internal:7474/db/data");
    }

    public final void saveToRedis(String key, Object object) {

    }

    @Override
    public final void declareOutputFields(OutputFieldsDeclarer declarer) {
        logInfo("outputStream = " + outputStream);
        if (outputStream != null) {
            declarer.declareStream(outputStream, new Fields(getFields()));
        }
    }

    protected String[] getFields() {
        return new String[] { "Default" };
    }

    protected void writeToStream(Tuple anchor, List<Object> tuple) {
        logInfo("Writing To Stream " + outputStream);
        outputCollector.emit(outputStream, anchor, tuple);
    }

    protected void writeToTaskStream(int taskId, Tuple anchor, List<Object> tuple) {
        outputCollector.emitDirect(taskId, outputStream, anchor, tuple);
    }

    protected void acknowledgeTuple(Tuple input) {
        outputCollector.ack(input);
    }

    @Override
    public void cleanup() {
        super.destroy();
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }

    @Override
    protected void logInfo(String message) {
        logger.info(message);
    }

    @Override
    protected void logWarning(String message) {
        logger.warn(message);
    }

    @Override
    protected void logError(String message) {
        logger.error(message);
    }

    @Override
    protected void logError(String message, Throwable ex) {
        logger.error(message, ex);
        System.out.println(message);
        ex.printStackTrace();
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
        logInfo("startOfHour = " + calendar.getTimeInMillis() + " , " + calendar.getTime());
        return calendar.getTimeInMillis();
    }

    protected Long getEndOfHour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        logInfo("endOfHour = " + calendar.getTimeInMillis() + " , " + calendar.getTime());
        return calendar.getTimeInMillis();
    }

}
