package com.eswaraj.cache.impl;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import com.eswaraj.cache.CacheService;

/**
 * Redis implementation for the cache service
 * @author anuj
 * @data Jun 28, 2014
 */

public abstract class RedisCacheServiceImpl<K,V extends Serializable> implements CacheService<K, V> {
	
	@Autowired
	protected RedisTemplate<K, V> template;

	/**
	 * set operation using key K and value V
	 */
	public void set(K key, V value) {
		template.opsForValue().set(key, value);
	}
	
	/**
	 * get operation using key as K returns value V
	 */
	public V get(K key) {
		return template.opsForValue().get(key);
	}

	/**
	 * delete operation using key as k
	 */
	public void delete(K key) {
		template.opsForValue().getOperations().delete(key);
	}
}
