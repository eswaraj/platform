package com.eswaraj.tasks.bolt.processors;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.tuple.Tuple;

import com.eswaraj.tasks.topology.EswarajBaseBolt;

public abstract class AbstractBoltProcessor implements BoltProcessor {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private ThreadLocal<Tuple> currentTuple;
    private EswarajBaseBolt eswarajBaseBolt;

    // Log related functions
    protected void logInfo(String message) {
        logger.info(getCurremtTupleAnchor() + " : " + message);
    }

    protected void logInfo(String message, Object... objects) {
        logger.info(getCurremtTupleAnchor() + " : " + message, objects);
    }

    protected void logDebug(String message) {
        logger.debug(getCurremtTupleAnchor() + " : " + message);
    }

    protected void logDebug(String message, Object... obj1) {
        logger.debug(getCurremtTupleAnchor() + " : " + message, obj1);
    }

    protected void logWarning(String message) {
        logger.warn(getCurremtTupleAnchor() + " : " + message);
    }

    protected void logWarning(String message, Object... obj1) {
        logger.warn(getCurremtTupleAnchor() + " : " + message, obj1);
    }

    protected void logError(String message) {
        logger.error(getCurremtTupleAnchor() + " : " + message);
    }

    protected void logError(String message, Throwable ex) {
        logger.error(getCurremtTupleAnchor() + " : " + message, ex);
    }

    @Override
    public void initBoltProcessorForTuple(ThreadLocal<Tuple> tuple, EswarajBaseBolt eswarajBaseBolt) {
        this.currentTuple = tuple;
        this.eswarajBaseBolt = eswarajBaseBolt;
    }
    protected String getCurremtTupleAnchor() {
        if (currentTuple == null) {
            return "NI";
        }
        Tuple tuple = currentTuple.get();
        if (tuple == null) {
            return "NI";
        }
        return tuple.getMessageId().getAnchors().toString();
    }

    public void writeToStream(Tuple anchor, List<Object> tuple) {
        eswarajBaseBolt.writeToStream(anchor, tuple);
    }

    public void writeToTaskStream(int taskId, Tuple anchor, List<Object> tuple) {
        eswarajBaseBolt.writeToTaskStream(taskId, anchor, tuple);
    }

}
