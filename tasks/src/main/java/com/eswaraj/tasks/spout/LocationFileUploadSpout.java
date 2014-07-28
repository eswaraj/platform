package com.eswaraj.tasks.spout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import backtype.storm.tuple.Values;

import com.eswaraj.tasks.topology.EswarajAwsSqsBaseSpout;

public class LocationFileUploadSpout extends EswarajAwsSqsBaseSpout {

    private static final long serialVersionUID = 1L;

    @Autowired
    public LocationFileUploadSpout(@Value("${aws_location_file_queue_name}") String awsQueueName) {
        super(awsQueueName);
    }

    @Override
    public void nextTuple() {
        String message = getMessage();
        logInfo("Mesage Recieved in Spout :  " + message);
        if (message != null) {
            writeToStream(new Values(message, 1L));
        }
    }
    
    @Override
    protected String[] getFields() {
        return new String[] { "LatLong", "LocationId" };
    }

}
