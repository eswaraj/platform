package com.eswaraj.tasks.topology;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.NotFoundException;
import org.neo4j.graphdb.Transaction;
import org.springframework.data.neo4j.rest.SpringRestGraphDatabase;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * This class gives support for Neo4j and Redis to all Spouts and Bolts
 * 
 * @author Ravi
 *
 */
public class EswarajBaseComponent {

    private GraphDatabaseService graphDatabaseService;
    private Neo4jTemplate neo4jTemplate;
    private RedisTemplate redisTemplate;

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

    // Neo4j related functions
    protected Node getNodeByid(Long id) {
        Transaction trx = graphDatabaseService.beginTx();
        Node node = null;
        try {
            node = graphDatabaseService.getNodeById(845);
        } catch (NotFoundException nfe) {
            // Don't do anything
        } finally {
            trx.finish();
        }

        return node;
    }

    // Redis related functions
    protected <T> Long writeToMemoryStoreSet(String redisKey, T id) {
        return redisTemplate.opsForSet().add(redisKey, id);
    }

    protected <T> Long removeFromMemoryStoreSet(String redisKey, T id) {
        return redisTemplate.opsForSet().remove(redisKey, id);
    }

}
