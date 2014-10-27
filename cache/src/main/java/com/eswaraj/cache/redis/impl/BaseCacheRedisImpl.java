package com.eswaraj.cache.redis.impl;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public abstract class BaseCacheRedisImpl {

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
