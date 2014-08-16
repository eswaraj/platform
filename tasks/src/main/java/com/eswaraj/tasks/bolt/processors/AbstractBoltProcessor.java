package com.eswaraj.tasks.bolt.processors;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.annotation.QueryType;
import org.springframework.data.neo4j.conversion.Result;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import backtype.storm.tuple.Tuple;

import com.eswaraj.tasks.topology.EswarajBaseBolt;

public abstract class AbstractBoltProcessor implements BoltProcessor {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private ThreadLocal<Tuple> currentTuple;
    private EswarajBaseBolt eswarajBaseBolt;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate redisTemplate;
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
    protected Long writeToMemoryStoreSet(String redisKey, String value) {
        logDebug("redisKey = {}, Value = {}", redisKey, value);
        return stringRedisTemplate.opsForSet().add(redisKey, value);
    }

    protected Long writeToMemoryStoreSet(String redisKey, Object value) {
        logDebug("redisKey = {}, Value = {}", redisKey, value);
        return redisTemplate.opsForSet().add(redisKey, value);
    }

    protected <T> Long removeFromMemoryStoreSet(String redisKey, T id) {
        return redisTemplate.opsForSet().remove(redisKey, id);
    }


    // Value
    protected void writeToMemoryStoreValue(String redisKey, String value) {
        logDebug("redisKey = {}, Value = {}", redisKey, value);
        stringRedisTemplate.opsForValue().set(redisKey, value);
    }

    protected void writeToMemoryStoreValue(String redisKey, Object value) {
        logDebug("redisKey = {}, Value = {}", redisKey, value);
        redisTemplate.opsForValue().set(redisKey, value);
    }

    protected void writeToMemoryStoreValue(String redisKey, Long value) {
        logDebug("redisKey = {}, Value = {}", redisKey, String.valueOf(value));
        stringRedisTemplate.opsForValue().set(redisKey, String.valueOf(value));
    }

    protected <T> List<T> readMultiKeyFromMemoryStore(List<String> redisKeys, Class<T> clazz) {
        return redisTemplate.opsForValue().multiGet(redisKeys);
    }

    protected List<Object> readMultiKeyFromMemoryStore(List<String> redisKeys) {
        return redisTemplate.opsForValue().multiGet(redisKeys);
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
