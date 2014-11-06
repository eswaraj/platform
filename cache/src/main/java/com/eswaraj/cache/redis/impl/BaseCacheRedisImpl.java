package com.eswaraj.cache.redis.impl;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.eswaraj.core.service.AppKeyService;
import com.eswaraj.core.service.AppService;
import com.eswaraj.core.service.StormCacheAppServices;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

public abstract class BaseCacheRedisImpl {

    @Autowired
    protected AppKeyService appKeyService;

    @Autowired
    protected StormCacheAppServices stormCacheAppServices;

    @Autowired
    protected AppService appService;

    protected JsonParser jsonParser = new JsonParser();

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    protected Gson gson = new Gson();

    protected String[] convertToStringArray(Collection<Long> values) {
        String[] valueString = new String[values.size()];
        int i = 0;
        for (Long oneLong : values) {
            valueString[i] = String.valueOf(oneLong);
            i++;
        }
        return valueString;

    }

    protected Set<Long> convertToLongSet(Collection<String> values) {
        Set<Long> valueLong = new LinkedHashSet<>();
        for (String oneString : values) {
            valueLong.add(Long.parseLong(oneString));
        }
        return valueLong;

    }
}
