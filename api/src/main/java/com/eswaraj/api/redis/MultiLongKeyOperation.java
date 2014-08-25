package com.eswaraj.api.redis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class MultiLongKeyOperation implements RedisOperation<Long> {

    private Collection<Long> ids;

    public MultiLongKeyOperation(Collection<Long> ids) {
        this.ids = ids;
    }

    protected abstract String buildKey(Long id);

    @Override
    public List<String> getKeys() {
        List<String> keys = new ArrayList<>(ids.size());
        for (Long oneLocationId : ids) {
            keys.add(buildKey(oneLocationId));
        }
        return keys;
    }

    @Override
    public Map<Long, String> prepareResult(List<String> results, int index) {
        int startIndex = index;
        Map<Long, String> result = new LinkedHashMap<>();
        for (Long oneLocationId : ids) {
            result.put(oneLocationId, results.get(startIndex));
            startIndex++;
        }
        return result;
    }
}
