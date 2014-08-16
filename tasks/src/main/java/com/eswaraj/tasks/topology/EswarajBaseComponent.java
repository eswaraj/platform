package com.eswaraj.tasks.topology;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.neo4j.graphdb.GraphDatabaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.neo4j.annotation.QueryType;
import org.springframework.data.neo4j.conversion.EndResult;
import org.springframework.data.neo4j.conversion.Result;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import backtype.storm.tuple.Tuple;

import com.eswaraj.core.service.AppService;
import com.eswaraj.core.service.ComplaintService;
import com.eswaraj.core.service.LocationService;
import com.eswaraj.core.service.StormCacheAppServices;
import com.eswaraj.queue.service.QueueService;
import com.eswaraj.queue.service.aws.impl.AwsQueueManager;
import com.eswaraj.queue.service.aws.impl.AwsQueueServiceImpl;

/**
 * This class gives support for Neo4j and Redis to all Spouts and Bolts
 * 
 * @author Ravi
 *
 */
public abstract class EswarajBaseComponent implements Serializable {

    private static final long serialVersionUID = 1L;

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    private int paralellism = 1;
    private ClassPathXmlApplicationContext applicationContext;
    private ComplaintService complaintService;
    private AppService appService;
    private LocationService locationService;
    private StormCacheAppServices stormCacheAppServices;

    private GraphDatabaseService graphDatabaseService;
    private Neo4jTemplate neo4jTemplate;
    private RedisTemplate<String, Object> redisTemplate;

    private transient ThreadLocal<Tuple> tupleThreadLocal;


    private QueueService queueService;

    private String dbUrl;
    private String redisUrl;
    private int redisPort;
    private String regions;
    private String accessKey;
    private String secretKey;

    private String awsLocationQueueName;
    private String awsCategoryUpdateQueueName;
    private String awsComplaintCreatedQueueName;
    private String awsReprocessAllComplaintQueueName;

    private void initConfigs() {
        dbUrl = System.getenv("db_url");
        redisUrl = System.getenv("redis_server");
        redisPort = Integer.parseInt(System.getenv("redis_port"));
        regions = System.getenv("aws_region");
        accessKey = System.getenv("aws_access_key");
        secretKey = System.getenv("aws_access_secret");
        awsLocationQueueName = System.getenv("aws_location_file_queue_name");
        awsCategoryUpdateQueueName = System.getenv("aws_category_queue_name");
        awsComplaintCreatedQueueName = System.getenv("aws_complaint_created_queue_name");
        awsReprocessAllComplaintQueueName = System.getenv("aws_reprocess_all_complaint_queue_name");
        

        logInfo("SYSTEM : dbUrl= {}", dbUrl);
        logInfo("SYSTEM : redisUrl= {}", redisUrl);
        logInfo("SYSTEM : redisPort= {}", redisPort);
        logInfo("SYSTEM : regions= {}", regions);
        logInfo("SYSTEM : accessKey=******");
        logInfo("SYSTEM : awsLocationQueueName= {}", awsLocationQueueName);
        logInfo("SYSTEM : awsCategoryUpdateQueueName={}", awsCategoryUpdateQueueName);
        logInfo("SYSTEM : awsComplaintCreatedQueueName={}", awsComplaintCreatedQueueName);
        logInfo("SYSTEM : awsReprocessAllComplaintQueueName={}", awsReprocessAllComplaintQueueName);

    }

    protected ClassPathXmlApplicationContext getApplicationContext() {
        if (applicationContext == null) {
            synchronized (this) {
                if (applicationContext == null) {
                    applicationContext = new ClassPathXmlApplicationContext("task-spring-context.xml");
                }
            }
        }
        return applicationContext;
    }
    protected void init() {
        tupleThreadLocal = new ThreadLocal<>();
        initConfigs();
        /*
        initializeDbService(dbUrl);
        initializeQueueService(regions, accessKey, secretKey);
        initializeRedisService(redisUrl, redisPort);
        */
    }

    protected void initializeRedisService(String redisUrl, int redisPort) {
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setHostName(redisUrl);
        jedisConnectionFactory.setPort(redisPort);
        jedisConnectionFactory.setUsePool(true);
        jedisConnectionFactory.afterPropertiesSet();

        redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(redisTemplate.getStringSerializer());
        logInfo("Key Serializer = " + redisTemplate.getKeySerializer());
        redisTemplate.setConnectionFactory(jedisConnectionFactory);
        redisTemplate.afterPropertiesSet();
        logInfo("Key Serializer = " + redisTemplate.getKeySerializer());

    }

    protected void destroy() {
        if (applicationContext != null) {
            try {
                applicationContext.close();
            } catch (Exception ex) {
                logError("Unable to close application context", ex);
            }
        }
    }

    private void initializeQueueService(String regions, String accessKey, String secretKey) {
        AwsQueueManager awsQueueManager = new AwsQueueManager(regions, accessKey, secretKey);
        queueService = new AwsQueueServiceImpl(awsQueueManager, awsLocationQueueName, awsCategoryUpdateQueueName, awsComplaintCreatedQueueName, awsReprocessAllComplaintQueueName);
    }

    // DB Related Functions
    protected Long executeCountQueryAndReturnLong(String cypherQuery, Map<String, Object> params, String totalFieldName) {
        logDebug("Running Query {} with Params {}", cypherQuery, params);
        Result<Object> result = getNeo4jTemplate().queryEngineFor(QueryType.Cypher).query(cypherQuery, params);
        Long totalCount = ((Integer) ((Map) result.single()).get(totalFieldName)).longValue();
        return totalCount;
    }

    protected <T> EndResult<T> findAll(Class<T> clazz) {
        EndResult<T> result = getNeo4jTemplate().findAll(clazz);
        return result;
    }
    // Redis related functions
    protected <T> Long writeToMemoryStoreSet(String redisKey, T id) {
        return redisTemplate.opsForSet().add(redisKey, id);
    }

    protected void writeToMemoryStoreValue(String redisKey, Object value) {
        logDebug("redisKey = {}, Value = {}", redisKey, value);
        logInfo("Key Serializer = " + redisTemplate.getKeySerializer());
        redisTemplate.opsForValue().set(redisKey, value);
    }

    protected <T> List<T> readMultiKeyFromMemoryStore(List<String> redisKeys, Class<T> clazz) {
        return (List<T>) redisTemplate.opsForValue().multiGet(redisKeys);
    }

    protected List<Object> readMultiKeyFromMemoryStore(List<String> redisKeys) {
        return redisTemplate.opsForValue().multiGet(redisKeys);
    }

    protected Long incrementCounterInMemoryStore(String redisKey, Long delta) {
        return redisTemplate.opsForValue().increment(redisKey, delta);
    }

    protected <T> Long removeFromMemoryStoreSet(String redisKey, T id) {
        return redisTemplate.opsForSet().remove(redisKey, id);
    }

    public QueueService getQueueService() {
        return queueService;
    }

    public void setQueueService(QueueService queueService) {
        this.queueService = queueService;
    }

    public GraphDatabaseService getGraphDatabaseService() {
        if(graphDatabaseService == null){
            synchronized (this) {
                if(graphDatabaseService == null){
                    graphDatabaseService = getApplicationContext().getBean(GraphDatabaseService.class);
                }
            }
        }
        return graphDatabaseService;
    }

    public void setGraphDatabaseService(GraphDatabaseService graphDatabaseService) {
        this.graphDatabaseService = graphDatabaseService;
    }

    public Neo4jTemplate getNeo4jTemplate() {
        return neo4jTemplate;
    }

    public void setNeo4jTemplate(Neo4jTemplate neo4jTemplate) {
        this.neo4jTemplate = neo4jTemplate;
    }

    public RedisTemplate getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public String getRedisUrl() {
        return redisUrl;
    }

    public void setRedisUrl(String redisUrl) {
        this.redisUrl = redisUrl;
    }

    public int getRedisPort() {
        return redisPort;
    }

    public void setRedisPort(int redisPort) {
        this.redisPort = redisPort;
    }

    public String getRegions() {
        return regions;
    }

    public void setRegions(String regions) {
        this.regions = regions;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getAwsLocationQueueName() {
        return awsLocationQueueName;
    }

    public void setAwsLocationQueueName(String awsLocationQueueName) {
        this.awsLocationQueueName = awsLocationQueueName;
    }

    public String getAwsCategoryUpdateQueueName() {
        return awsCategoryUpdateQueueName;
    }

    public void setAwsCategoryUpdateQueueName(String awsCategoryUpdateQueueName) {
        this.awsCategoryUpdateQueueName = awsCategoryUpdateQueueName;
    }

    public String getAwsComplaintCreatedQueueName() {
        return awsComplaintCreatedQueueName;
    }

    public void setAwsComplaintCreatedQueueName(String awsComplaintCreatedQueueName) {
        this.awsComplaintCreatedQueueName = awsComplaintCreatedQueueName;
    }

    public int getParalellism() {
        return paralellism;
    }

    public void setParalellism(int paralellism) {
        this.paralellism = paralellism;
    }

    protected void setCurrentTuple(Tuple tuple) {
        getTupleThreadLocal().set(tuple);
    }

    protected void clearCurrentTuple() {
        getTupleThreadLocal().remove();
    }

    protected String getCurremtTupleAnchor() {
        ThreadLocal<Tuple> threadLocal = getTupleThreadLocal();
        if (threadLocal == null) {
            return "NI";
        }
        Tuple tuple = threadLocal.get();
        if (tuple == null) {
            return "NI";
        }
        return tuple.getMessageId().getAnchors().toString();
    }
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

    protected AppService getApplicationService() {
        if (appService == null) {
            synchronized (this) {
                if (appService == null) {
                    appService = getApplicationContext().getBean(AppService.class);
                }
            }
        }
        return appService;
    }

    protected ComplaintService getComplaintService() {
        if (complaintService == null) {
            synchronized (this) {
                if (complaintService == null) {
                    complaintService = getApplicationContext().getBean(ComplaintService.class);
                }
            }
        }
        return complaintService;
    }

    protected LocationService getLocationService() {
        if (locationService == null) {
            synchronized (this) {
                if (locationService == null) {
                    locationService = getApplicationContext().getBean(LocationService.class);
                }
            }
        }
        return locationService;
    }

    protected StormCacheAppServices getStormCacheAppServices() {
        if (stormCacheAppServices == null) {
            synchronized (this) {
                if (stormCacheAppServices == null) {
                    stormCacheAppServices = getApplicationContext().getBean(StormCacheAppServices.class);
                }
            }
        }
        return stormCacheAppServices;
    }

    public ThreadLocal<Tuple> getTupleThreadLocal() {
        return tupleThreadLocal;
    }

}
