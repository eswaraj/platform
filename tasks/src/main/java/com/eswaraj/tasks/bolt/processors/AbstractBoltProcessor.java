package com.eswaraj.tasks.bolt.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.tuple.Tuple;

public abstract class AbstractBoltProcessor implements BoltProcessor {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private Tuple currentTuple;

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
    public void initBoltProcessorForTuple(Tuple tuple) {
        this.currentTuple = tuple;
    }
    protected String getCurremtTupleAnchor() {
        if (currentTuple == null) {
            return "NI";
        }
        return currentTuple.getMessageId().getAnchors().toString();
    }
}
