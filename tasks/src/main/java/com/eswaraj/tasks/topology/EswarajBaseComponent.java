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
import org.springframework.data.neo4j.annotation.QueryType;
import org.springframework.data.neo4j.conversion.Result;
import org.springframework.data.neo4j.rest.SpringRestGraphDatabase;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import com.eswaraj.core.service.AppService;
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

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    private boolean initializeDbServices = false;
    private boolean initializeRedisServices = false;
    private boolean initializeQueueServices = false;
    private int paralellism = 1;

    private GraphDatabaseService graphDatabaseService;
    private Neo4jTemplate neo4jTemplate;
    private RedisTemplate redisTemplate;
    private AppService appService;

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

    private void printAllConfigs() {
        System.out.println("dbUrl=" + dbUrl);
        System.out.println("redisUrl=" + redisUrl);
        System.out.println("redisPort=" + redisPort);
        System.out.println("regions=" + regions);
        System.out.println("accessKey=******");
        System.out.println("awsLocationQueueName=" + awsLocationQueueName);
        System.out.println("awsCategoryUpdateQueueName=" + awsCategoryUpdateQueueName);
        System.out.println("awsComplaintCreatedQueueName=" + awsComplaintCreatedQueueName);

    }

    protected void init() {
        printAllConfigs();
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
        redisTemplate.setConnectionFactory(jedisConnectionFactory);
        redisTemplate.afterPropertiesSet();
    }

    protected void destroy() {
    }

    private void initializeQueueService(String regions, String accessKey, String secretKey) {
        AwsQueueManager awsQueueManager = new AwsQueueManager(regions, accessKey, secretKey);
        queueService = new AwsQueueServiceImpl(awsQueueManager, awsLocationQueueName, awsCategoryUpdateQueueName, awsComplaintCreatedQueueName);

    }

    protected abstract void writeToStream(List<Object> tuple);

    protected abstract void writeToTaskStream(int taskId, List<Object> tuple);

    protected abstract void writeToTaskStream(int taskId, List<Object> tuple, Object messageId);
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

    // DB Related Functions
    protected Long executeCountQueryAndReturnLong(String cypherQuery, Map<String, Object> params, String totalFieldName) {
        Result<Object> result = getNeo4jTemplate().queryEngineFor(QueryType.Cypher).query(cypherQuery, params);
        Long totalCount = ((Integer) ((Map) result.single()).get(totalFieldName)).longValue();
        return totalCount;
    }
    // Redis related functions
    protected <T> Long writeToMemoryStoreSet(String redisKey, T id) {
        checkRedisServices();
        return redisTemplate.opsForSet().add(redisKey, id);
    }

    protected void writeToMemoryStoreValue(String redisKey, Long id) {
        checkRedisServices();
        redisTemplate.opsForValue().set(redisKey, id);
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

    public AppService getAppService() {
        return appService;
    }

    public void setAppService(AppService appService) {
        this.appService = appService;
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

    protected void logInfo(String message) {
        logger.info(message);
        System.out.println(message);
    }

    protected void logWarning(String message) {
        logger.warn(message);
    }

    protected void logError(String message) {
        logger.error(message);
    }

    protected void logError(String message, Throwable ex) {
        logger.error(message, ex);
    }

}
