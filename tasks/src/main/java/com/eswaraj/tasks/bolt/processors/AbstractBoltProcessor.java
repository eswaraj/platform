package com.eswaraj.tasks.bolt.processors;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.annotation.QueryType;
import org.springframework.data.neo4j.conversion.Result;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import backtype.storm.tuple.Tuple;

import com.eswaraj.core.service.AppKeyService;
import com.eswaraj.tasks.topology.EswarajBaseBolt;

public abstract class AbstractBoltProcessor implements BoltProcessor {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private ThreadLocal<Tuple> currentTuple;
    private EswarajBaseBolt eswarajBaseBolt;

    @Autowired
    protected AppKeyService appKeyService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private Neo4jTemplate neo4jTemplate;

    // Log related functions
    protected void logInfo(String message) {
        logger.info(getCurremtTupleAnchor() + " : " + message);
    }

    protected void logInfo(String message, Object... objects) {
        logger.info(getCurremtTupleAnchor() + " : " + message, objects);
    }

    protected void logDebug(String message) {
        logger.debug(getCurremtTupleAnchor() + " : " + message);
    }

    protected void logDebug(String message, Object... obj1) {
        logger.debug(getCurremtTupleAnchor() + " : " + message, obj1);
    }

    protected void logWarning(String message) {
        logger.warn(getCurremtTupleAnchor() + " : " + message);
    }

    protected void logWarning(String message, Object... obj1) {
        logger.warn(getCurremtTupleAnchor() + " : " + message, obj1);
    }

    protected void logError(String message) {
        logger.error(getCurremtTupleAnchor() + " : " + message);
    }

    protected void logError(String message, Throwable ex) {
        logger.error(getCurremtTupleAnchor() + " : " + message, ex);
    }

    @Override
    public void initBoltProcessorForTuple(ThreadLocal<Tuple> tuple, EswarajBaseBolt eswarajBaseBolt) {
        this.currentTuple = tuple;
        this.eswarajBaseBolt = eswarajBaseBolt;
    }
    protected String getCurremtTupleAnchor() {
        if (currentTuple == null) {
            return "NI";
        }
        Tuple tuple = currentTuple.get();
        if (tuple == null) {
            return "NI";
        }
        return tuple.getMessageId().getAnchors().toString();
    }

    public void writeToStream(Tuple anchor, List<Object> tuple) {
        eswarajBaseBolt.writeToStream(anchor, tuple);
    }

    public void writeToTaskStream(int taskId, Tuple anchor, List<Object> tuple) {
        eswarajBaseBolt.writeToTaskStream(taskId, anchor, tuple);
    }

    // Redis related functions
    // Set
    protected Long writeToMemoryStoreSet(String redisKey, String... value) {
        logDebug("redisKey = {}, Value = {}", redisKey, value);
        return stringRedisTemplate.opsForSet().add(redisKey, value);
    }

    protected Long writeToMemoryStoreSet(String redisKey, Set<Long> values) {
        Set<String> valueString = new HashSet<>();
        for (Long oneLong : values) {
            valueString.add(String.valueOf(oneLong));
        }
        return writeToMemoryStoreSet(redisKey, valueString.toArray(new String[valueString.size()]));
    }

    protected Long removeFromMemoryStoreSet(String redisKey, String... value) {
        logDebug("redisKey = {}, Value = {}", redisKey, value);
        return stringRedisTemplate.opsForSet().remove(redisKey, value);
    }

    protected Long removeFromMemoryStoreSet(String redisKey, Set<Long> values) {
        Set<String> valueString = new HashSet<>();
        for (Long oneLong : values) {
            valueString.add(String.valueOf(oneLong));
        }
        return removeFromMemoryStoreSet(redisKey, valueString.toArray(new String[valueString.size()]));
    }

    protected Boolean writeToMemoryStoreSortedSet(String redisKey, String value, double score) {
        logDebug("redisKey = {}, Value = {}, score = {}", redisKey, value, score);
        return stringRedisTemplate.opsForZSet().add(redisKey, value, score);
    }

    protected Long removeFromMemoryStoreSortedSet(String redisKey, String value) {
        logDebug("redisKey = {}, Value = {}", redisKey, value);
        return stringRedisTemplate.opsForZSet().remove(redisKey, value);
    }


    // Value
    protected void writeToMemoryStoreValue(String redisKey, String value) {
        logDebug("redisKey = {}, Value = {}", redisKey, value);
        stringRedisTemplate.opsForValue().set(redisKey, value);
    }

    protected void writeToMemoryStoreValue(String redisKey, Long value) {
        logDebug("redisKey = {}, Value = {}", redisKey, String.valueOf(value));
        stringRedisTemplate.opsForValue().set(redisKey, String.valueOf(value));
    }

    protected void writeToMemoryStoreHash(String redisKey, String hashKey, Long value) {
        writeToMemoryStoreHash(redisKey, hashKey, String.valueOf(value));
    }

    protected void writeToMemoryStoreHash(String redisKey, String hashKey, String value) {
        logDebug("redisKey = {}, hashKey ={}, Value = {}", redisKey, hashKey, value);
        stringRedisTemplate.opsForHash().put(redisKey, hashKey, value);
    }

    protected List<String> readMultiKeyFromStringMemoryStore(List<String> redisKeys) {
        return stringRedisTemplate.opsForValue().multiGet(redisKeys);
    }

    protected List<Object> readMultiKeyFromStringMemoryHashStore(String redisKey, List<String> hashKeys) {
        return stringRedisTemplate.opsForHash().multiGet(redisKey, convertStringListToObjectList(hashKeys));
    }

    private List<Object> convertStringListToObjectList(List<String> stringList) {
        List<Object> objectList = new ArrayList<>(stringList.size());
        for (String oneString : stringList) {
            objectList.add(oneString);
        }
        return objectList;

    }

    protected Long incrementCounterInMemoryStore(String redisKey, Long delta) {
        return stringRedisTemplate.opsForValue().increment(redisKey, delta);
    }

    protected Long getStartOfHour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MILLISECOND, 1);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        return calendar.getTimeInMillis();
    }

    protected Long getEndOfHour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        return calendar.getTimeInMillis();
    }

    protected Long executeCountQueryAndReturnLong(String cypherQuery, Map<String, Object> params, String totalFieldName) {
        logDebug("Running Query {} with Params {}", cypherQuery, params);
        Result<Object> result = neo4jTemplate.queryEngineFor(QueryType.Cypher).query(cypherQuery, params);
        Long totalCount = ((Integer) ((Map) result.single()).get(totalFieldName)).longValue();
        return totalCount;
    }

}
