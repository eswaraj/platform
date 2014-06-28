package com.eswaraj.cache;

import java.io.Serializable;

/**
 * Cache interface
 * @author anuj
 * @data Jun 28, 2014
 */

public interface CacheService <K,V extends Serializable> {
	V get(K key);
	
	boolean set(V value);
}
