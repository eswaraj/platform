package com.eswaraj.tasks.bolt;

import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.eswaraj.tasks.topology.EswarajBaseBolt;

public class TestBolt extends EswarajBaseBolt {

    private static final long serialVersionUID = 1L;
    private String boltId;

    @Override
    public void execute(Tuple input) {
        String message = input.getString(0);
        logInfo("Recived = " + getComponentId() + " " + message);
        if(outputStream != null){
            outputCollector.emit(outputStream, new Values(message + " from " + getComponentId()));
        }
    }

    public String getBoltId() {
        return boltId;
    }

    public void setBoltId(String boltId) {
        this.boltId = boltId;
    }

}
