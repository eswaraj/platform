package com.eswaraj.tasks.bolt;

import org.springframework.data.redis.core.StringRedisTemplate;

import backtype.storm.tuple.Tuple;

import com.eswaraj.tasks.topology.EswarajBaseBolt;

public class DayCounterBolt extends EswarajBaseBolt {

    private static final long serialVersionUID = 1L;
    StringRedisTemplate t;

    @Override
    public void execute(Tuple input) {
        String keySuffix = input.getStringByField("key-suffix");

    }

}
