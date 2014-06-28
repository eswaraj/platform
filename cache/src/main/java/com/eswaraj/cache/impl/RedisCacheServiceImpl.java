package com.eswaraj.cache.impl;

import java.io.Serializable;
import com.eswaraj.cache.CacheService;

/**
 * Redis implementation for the cache service
 * @author anuj
 * @data Jun 28, 2014
 */

public class RedisCacheServiceImpl<K,V extends Serializable> implements CacheService<K, V> {

	public V get(K key) {
		return null;
	}

	public boolean set(V value) {
		return false;
	}
}
