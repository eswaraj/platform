package com.eswaraj.tasks.bolt;

import backtype.storm.tuple.Tuple;

public class DayCounterBolt extends CounterBolt {

    private static final long serialVersionUID = 1L;

    @Override
    public void execute(Tuple input) {
        String keySuffix = input.getStringByField("KeyPrefix");

    }

}
