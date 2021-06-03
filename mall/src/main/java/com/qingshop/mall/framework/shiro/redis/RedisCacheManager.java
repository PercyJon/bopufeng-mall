package com.qingshop.mall.framework.shiro.redis;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.qingshop.mall.common.utils.RedisUtils;

@Component
public class RedisCacheManager implements CacheManager {
	
	private final Logger logger = LoggerFactory.getLogger(RedisCacheManager.class);

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private final ConcurrentMap<String, Cache> caches = new ConcurrentHashMap();

	@Autowired
	private RedisUtils redisUtils;

	public static final String DEFAULT_CACHE_KEY_PREFIX = "shiro:redisCache:";
	private String keyPrefix = DEFAULT_CACHE_KEY_PREFIX;

	// expire time in seconds
	public static final long DEFAULT_EXPIRE = 30 * 60 * 1000;
	private long expire = DEFAULT_EXPIRE;

	public static final String DEFAULT_PRINCIPAL_ID_FIELD_NAME = "authCacheKey or id";
	private String principalIdFieldName = DEFAULT_PRINCIPAL_ID_FIELD_NAME;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public <K, V> Cache<K, V> getCache(String cacheName) throws CacheException {
		this.logger.debug("get cache, cacheName=" + cacheName);
		Cache cache = (Cache) this.caches.get(cacheName);
		if (cache == null) {
			this.logger.debug("get cache but cache is null");
			cache = new ShiroRedisCache(this.redisUtils, this.keyPrefix + cacheName + ":", expire, this.principalIdFieldName);
			this.caches.put(cacheName, cache);
		}

		return (Cache) cache;
	}

	public String getKeyPrefix() {
		return keyPrefix;
	}

	public void setKeyPrefix(String keyPrefix) {
		this.keyPrefix = keyPrefix;
	}

	public long getExpire() {
		return expire;
	}

	public void setExpire(long expire) {
		this.expire = expire;
	}

	public String getPrincipalIdFieldName() {
		return principalIdFieldName;
	}

	public void setPrincipalIdFieldName(String principalIdFieldName) {
		this.principalIdFieldName = principalIdFieldName;
	}

	public void setRedisUtils(RedisUtils redisUtils) {
		this.redisUtils = redisUtils;
	}
}
