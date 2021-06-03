package com.qingshop.mall.framework.shiro.redis;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.serializer.SerializationException;

import com.qingshop.mall.common.utils.RedisUtils;
import com.qingshop.mall.framework.shiro.redis.Exception.CacheManagerPrincipalIdNotAssignedException;
import com.qingshop.mall.framework.shiro.redis.Exception.PrincipalIdNullException;
import com.qingshop.mall.framework.shiro.redis.Exception.PrincipalInstanceException;

public class ShiroRedisCache<K, V> implements Cache<K, V> {
	
	private final Logger logger = LoggerFactory.getLogger(ShiroRedisCache.class);

	private RedisUtils redisUtils;

	private String keyPrefix = RedisCacheManager.DEFAULT_CACHE_KEY_PREFIX;

	private long expire = RedisCacheManager.DEFAULT_EXPIRE;

	private String principalIdFieldName = RedisCacheManager.DEFAULT_PRINCIPAL_ID_FIELD_NAME;

	public ShiroRedisCache(RedisUtils redisUtils, String prefix, long expire, String principalIdFieldName) {
		if (redisUtils == null) {
			throw new IllegalArgumentException("redisUtils cannot be null.");
		}
		this.redisUtils = redisUtils;
		if (prefix != null && !"".equals(prefix)) {
			this.keyPrefix = prefix;
		}
		if (expire != -1) {
			this.expire = expire;
		}
		if (principalIdFieldName != null && !"".equals(principalIdFieldName)) {
			this.principalIdFieldName = principalIdFieldName;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public V get(K key) throws CacheException {
		if (key == null) {
			return null;
		} else {

			String redisCacheKey = getRedisCacheKey(key);
			Object rawValue = redisUtils.get(true, redisCacheKey);

			if (rawValue == null) {
				return null;
			}
			V value = (V) rawValue;
			return value;
		}

	}

	@Override
	public V put(K key, V value) throws CacheException {
		if (key == null) {
			logger.warn("Saving a null key is meaningless, return value directly without call Redis.");
			return value;
		}
		try {
			String redisCacheKey = getRedisCacheKey(key);
			redisUtils.set(true, redisCacheKey, value != null ? value : null, expire);
			return value;
		} catch (SerializationException e) {
			throw new CacheException(e);
		}
	}

	private String getRedisCacheKey(K key) {
		if (key == null) {
			return null;
		}

		return this.keyPrefix + getStringRedisKey(key);

	}

	private String getStringRedisKey(K key) {
		String redisKey;
		if (key instanceof PrincipalCollection) {
			redisKey = getRedisKeyFromPrincipalIdField((PrincipalCollection) key);
		} else {
			redisKey = key.toString();
		}
		return redisKey;
	}

	/**
	 * getRedisKeyFromPrincipalIdField()是获取缓存的用户身份信息 和用户权限信息。
	 * 里面有一个属性principalIdFieldName
	 * 在RedisCacheManager也有这个属性,设置其中一个就可以.是为了给缓存用户身份和权限信息在Redis中的key唯一,
	 * 登录用户名可能是username 或者 phoneNum 或者是Email中的一个, 如 我的User实体类中 有一个
	 * usernane字段,也是登录时候使用的用户名,在redis中缓存的权限信息key 如下, 这个admin 就是 通过getUsername获得的。
	 */

	private String getRedisKeyFromPrincipalIdField(PrincipalCollection key) {
		Object principalObject = key.getPrimaryPrincipal();
		if (principalObject instanceof String) {
			return principalObject.toString();
		}
		Method pincipalIdGetter = getPrincipalIdGetter(principalObject);
		return getIdObj(principalObject, pincipalIdGetter);
	}

	private String getIdObj(Object principalObject, Method pincipalIdGetter) {
		String redisKey;
		try {
			Object idObj = pincipalIdGetter.invoke(principalObject);
			if (idObj == null) {
				throw new PrincipalIdNullException(principalObject.getClass(), this.principalIdFieldName);
			}
			redisKey = idObj.toString();
		} catch (IllegalAccessException e) {
			throw new PrincipalInstanceException(principalObject.getClass(), this.principalIdFieldName, e);
		} catch (InvocationTargetException e) {
			throw new PrincipalInstanceException(principalObject.getClass(), this.principalIdFieldName, e);
		}
		return redisKey;
	}

	private Method getPrincipalIdGetter(Object principalObject) {
		Method pincipalIdGetter = null;
		String principalIdMethodName = this.getPrincipalIdMethodName();
		try {
			pincipalIdGetter = principalObject.getClass().getMethod(principalIdMethodName);
		} catch (NoSuchMethodException e) {
			throw new PrincipalInstanceException(principalObject.getClass(), this.principalIdFieldName);
		}
		return pincipalIdGetter;
	}

	private String getPrincipalIdMethodName() {
		if (this.principalIdFieldName == null || "".equals(this.principalIdFieldName)) {
			throw new CacheManagerPrincipalIdNotAssignedException();
		}
		return "get" + this.principalIdFieldName.substring(0, 1).toUpperCase() + this.principalIdFieldName.substring(1);
	}

	@SuppressWarnings("unchecked")
	@Override
	public V remove(K key) throws CacheException {
		if (key == null) {
			return null;
		}
		try {
			String redisCacheKey = getRedisCacheKey(key);
			Object rawValue = redisUtils.get(true, redisCacheKey);
			V previous = (V) rawValue;
			redisUtils.del(true, redisCacheKey);
			return previous;
		} catch (SerializationException e) {
			throw new CacheException(e);
		}
	}

	@Override
	public void clear() throws CacheException {
		Set<String> keys = null;
		try {
			keys = redisUtils.scan(true, this.keyPrefix + "*");
		} catch (SerializationException e) {
			logger.error("get keys error", e);
		}
		if (keys == null || keys.size() == 0) {
			return;
		}
		for (String key : keys) {
			redisUtils.del(true, key);
		}
	}

	@Override
	public int size() {
		Long longSize = 0L;
		try {
			longSize = new Long(redisUtils.scanSize(true, this.keyPrefix + "*"));
		} catch (SerializationException e) {
			logger.error("get keys error", e);
		}
		return longSize.intValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<K> keys() {
		Set<String> keys = null;
		try {
			keys = redisUtils.scan(true, this.keyPrefix + "*");
		} catch (SerializationException e) {
			logger.error("get keys error", e);
			return Collections.emptySet();
		}

		if (CollectionUtils.isEmpty(keys)) {
			return Collections.emptySet();
		}

		Set<K> convertedKeys = new HashSet<K>();
		for (String key : keys) {
			try {
				convertedKeys.add((K) key);
			} catch (SerializationException e) {
				logger.error("deserialize keys error", e);
			}
		}
		return convertedKeys;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<V> values() {
		Set<String> keys = null;
		try {
			keys = redisUtils.scan(true, this.keyPrefix + "*");
		} catch (SerializationException e) {
			logger.error("get values error", e);
			return Collections.emptySet();
		}

		if (CollectionUtils.isEmpty(keys)) {
			return Collections.emptySet();
		}

		List<V> values = new ArrayList<V>(keys.size());
		for (String key : keys) {
			V value = null;
			try {
				value = (V) redisUtils.get(true, key);
			} catch (SerializationException e) {
				logger.error("deserialize values= error", e);
			}
			if (value != null) {
				values.add(value);
			}
		}
		return Collections.unmodifiableList(values);
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
