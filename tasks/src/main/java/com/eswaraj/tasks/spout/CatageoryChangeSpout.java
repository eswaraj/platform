package com.eswaraj.tasks.spout;

import backtype.storm.tuple.Values;

import com.eswaraj.tasks.topology.EswarajAwsSqsBaseSpout;

public class CatageoryChangeSpout extends EswarajAwsSqsBaseSpout {

    private static final long serialVersionUID = 1L;

    @Override
    public void nextTuple() {
        String message = getMessage();
        if (message != null) {
            writeToStream(new Values(message));
        }

    }

}
