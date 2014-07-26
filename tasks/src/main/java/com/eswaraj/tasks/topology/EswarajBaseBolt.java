package com.eswaraj.tasks.topology;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;

/**
 * All bolts should extend this class
 * 
 * @author Ravi Sharma
 * @data Jul 25, 2014
 */

public abstract class EswarajBaseBolt extends BaseRichBolt {

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
    }

    @Override
    public final void declareOutputFields(OutputFieldsDeclarer declarer) {
        logInfo("outputStream = " + outputStream);
        if (outputStream != null) {
            declarer.declareStream(outputStream, new Fields("Test"));
        }
    }

    protected void logInfo(String message) {
        logger.info(message);
    }

    protected void logWarning(String message) {
        logger.warn(message);
    }

    protected void logError(String message) {
        logger.error(message);
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

}
