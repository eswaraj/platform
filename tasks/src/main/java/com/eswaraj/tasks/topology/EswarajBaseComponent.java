package com.eswaraj.tasks.topology;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.NotFoundException;
import org.neo4j.graphdb.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.neo4j.annotation.QueryType;
import org.springframework.data.neo4j.conversion.EndResult;
import org.springframework.data.neo4j.conversion.Result;
import org.springframework.data.neo4j.rest.SpringRestGraphDatabase;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import backtype.storm.tuple.Tuple;

import com.eswaraj.core.service.AppService;
import com.eswaraj.core.service.ComplaintService;
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

    private boolean initializeDbServices = false;
    private boolean initializeRedisServices = false;
    private boolean initializeQueueServices = false;
    private int paralellism = 1;
    private ClassPathXmlApplicationContext applicationContext;
    private ComplaintService complaintService;
    private AppService appService;

    private GraphDatabaseService graphDatabaseService;
    private Neo4jTemplate neo4jTemplate;
    private RedisTemplate redisTemplate;
    StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();

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
        if (initializeDbServices) {
            initializeDbService(dbUrl);
        }
        if (initializeQueueServices) {
            initializeQueueService(regions, accessKey, secretKey);
        }
        if (initializeRedisServices) {
            initializeRedisService(redisUrl, redisPort);
        }
    }

    protected void initializeDbService(String dbUrl) {
        graphDatabaseService = new SpringRestGraphDatabase(dbUrl);
        neo4jTemplate = new Neo4jTemplate(graphDatabaseService);
    }

    protected void initializeRedisService(String redisUrl, int redisPort) {
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setHostName(redisUrl);
        jedisConnectionFactory.setPort(redisPort);
        jedisConnectionFactory.setUsePool(true);
        jedisConnectionFactory.afterPropertiesSet();

        redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(redisTemplate.getStringSerializer());
        redisTemplate.setConnectionFactory(jedisConnectionFactory);
        redisTemplate.afterPropertiesSet();

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

    // Neo4j related functions
    protected Node getNodeByid(Long id) {
        Transaction trx = graphDatabaseService.beginTx();
        Node node = null;
        try {
            node = graphDatabaseService.getNodeById(id);
        } catch (NotFoundException nfe) {
            // Don't do anything
            nfe.printStackTrace();
        } finally {
            trx.finish();
        }

        return node;
    }
    
 // Neo4j related functions
    protected <T> T getNodeById(Long id, Class<T> clazz) {
        Transaction trx = graphDatabaseService.beginTx();
        Node node = null;
        try {
            return neo4jTemplate.findOne(id, clazz);
        } catch (NotFoundException nfe) {
            // Don't do anything
            nfe.printStackTrace();
        } finally {
            trx.finish();
        }

        return null;
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
        checkRedisServices();
        return redisTemplate.opsForSet().add(redisKey, id);
    }

    protected void writeToMemoryStoreValue(String redisKey, Object value) {
        checkRedisServices();
        logDebug("redisKey = {}, Value = {}", redisKey, value);
        redisTemplate.opsForValue().set(redisKey, value);
    }

    protected <T> List<T> readMultiKeyFromMemoryStore(List<String> redisKeys, Class<T> clazz) {
        checkRedisServices();
        return redisTemplate.opsForValue().multiGet(redisKeys);
    }

    protected List<Object> readMultiKeyFromMemoryStore(List<String> redisKeys) {
        checkRedisServices();
        return redisTemplate.opsForValue().multiGet(redisKeys);
    }

    protected Long incrementCounterInMemoryStore(String redisKey, Long delta) {
        checkRedisServices();
        return redisTemplate.opsForValue().increment(redisKey, delta);
    }

    protected <T> Long removeFromMemoryStoreSet(String redisKey, T id) {
        checkRedisServices();
        return redisTemplate.opsForSet().remove(redisKey, id);
    }

    public QueueService getQueueService() {
        checkQueueServices();
        return queueService;
    }

    public void setQueueService(QueueService queueService) {
        this.queueService = queueService;
    }

    public boolean isInitializeDbServices() {
        return initializeDbServices;
    }

    public void setInitializeDbServices(boolean initializeDbServices) {
        this.initializeDbServices = initializeDbServices;
    }

    public boolean isInitializeRedisServices() {
        return initializeRedisServices;
    }

    public void setInitializeRedisServices(boolean initializeRedisServices) {
        this.initializeRedisServices = initializeRedisServices;
    }

    public boolean isInitializeQueuServices() {
        return initializeQueueServices;
    }

    public void setInitializeQueuServices(boolean initializeQueuServices) {
        this.initializeQueueServices = initializeQueuServices;
    }

    public boolean isInitializeQueueServices() {
        return initializeQueueServices;
    }

    public void setInitializeQueueServices(boolean initializeQueueServices) {
        this.initializeQueueServices = initializeQueueServices;
    }

    public GraphDatabaseService getGraphDatabaseService() {
        checkDbServices();
        return graphDatabaseService;
    }

    public void setGraphDatabaseService(GraphDatabaseService graphDatabaseService) {
        this.graphDatabaseService = graphDatabaseService;
    }

    public Neo4jTemplate getNeo4jTemplate() {
        checkDbServices();
        return neo4jTemplate;
    }

    public void setNeo4jTemplate(Neo4jTemplate neo4jTemplate) {
        this.neo4jTemplate = neo4jTemplate;
    }

    public RedisTemplate getRedisTemplate() {
        checkRedisServices();
        return redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private void checkQueueServices() {
        if (!initializeQueueServices) {
            throw new RuntimeException("Queue Service has not been intialized, set the property initializeQueueServices to true");
        }
    }

    private void checkRedisServices() {
        if (!initializeRedisServices) {
            throw new RuntimeException("Redis Cache Service has not been intialized, set the property initializeRedisServices to true");
        }
    }

    private void checkDbServices() {
        if (!initializeDbServices) {
            throw new RuntimeException("DB Service has not been intialized, set the property initializeDbServices to true");
        }
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

    public ThreadLocal<Tuple> getTupleThreadLocal() {
        return tupleThreadLocal;
    }

}
