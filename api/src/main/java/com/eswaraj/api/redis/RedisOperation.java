package com.eswaraj.api.redis;

import java.util.List;
import java.util.Map;

public interface RedisOperation<KeyType> {

    List<String> getKeys();

    Map<KeyType, String> prepareResult(List<String> results, int index);
}
