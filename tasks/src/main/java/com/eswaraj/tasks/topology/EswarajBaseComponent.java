package com.eswaraj.tasks.topology;

import java.util.List;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.NotFoundException;
import org.neo4j.graphdb.Transaction;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.data.redis.core.RedisTemplate;

import com.eswaraj.core.service.AppService;
import com.eswaraj.queue.service.QueueService;

/**
 * This class gives support for Neo4j and Redis to all Spouts and Bolts
 * 
 * @author Ravi
 *
 */
public abstract class EswarajBaseComponent {
    private boolean initializeDbServices = false;
    private boolean initializeRedisServices = false;
    private boolean initializeQueueServices = false;

    private GraphDatabaseService graphDatabaseService;
    private Neo4jTemplate neo4jTemplate;
    private RedisTemplate redisTemplate;
    private AppService appService;

    private QueueService queueService;
    private ClassPathXmlApplicationContext applicationContext;

    protected void init() {
        applicationContext = new ClassPathXmlApplicationContext("storm-topology-app.xml");
        appService = applicationContext.getBean(AppService.class);
        if (initializeQueueServices) {
            initializeQueueService(applicationContext);
        }
        if (initializeDbServices) {
            initializeDbService(applicationContext);
        }
        if (initializeRedisServices) {
            initializeRedisService(applicationContext);
        }

    }

    protected void destroy() {
        applicationContext.close();
    }

    private void initializeDbService(ApplicationContext applicationContext) {
        graphDatabaseService = applicationContext.getBean(GraphDatabaseService.class);
        neo4jTemplate = applicationContext.getBean(Neo4jTemplate.class);
    }

    private void initializeQueueService(ApplicationContext applicationContext) {
        queueService = applicationContext.getBean(QueueService.class);
    }

    private void initializeRedisService(ApplicationContext applicationContext) {
        redisTemplate = applicationContext.getBean(RedisTemplate.class);
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

    // Redis related functions
    protected <T> Long writeToMemoryStoreSet(String redisKey, T id) {
        checkRedisServices();
        return redisTemplate.opsForSet().add(redisKey, id);
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

}
