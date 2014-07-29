package com.eswaraj.tasks.spout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import backtype.storm.tuple.Values;

import com.eswaraj.tasks.topology.EswarajAwsSqsBaseSpout;

public class CatageoryChangeSpout extends EswarajAwsSqsBaseSpout {

    @Autowired
    public CatageoryChangeSpout(@Value("${aws_category_queue_name}") String awsQueueName) {
        super(awsQueueName);
    }

    private static final long serialVersionUID = 1L;

    @Override
    public void nextTuple() {
        String message = getMessage();
        logInfo("Mesage Recieved in Spout :  " + message);
        if (message != null) {
            writeToStream(new Values(message));
        }

    }
}