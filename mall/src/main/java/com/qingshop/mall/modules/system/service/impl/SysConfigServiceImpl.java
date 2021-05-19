package com.qingshop.mall.modules.system.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qingshop.mall.common.utils.JsonUtils;
import com.qingshop.mall.common.utils.StringUtils;
import com.qingshop.mall.framework.annotation.RedisCache;
import com.qingshop.mall.framework.enums.CacheKeyPrefix;
import com.qingshop.mall.framework.enums.ConfigKey;
import com.qingshop.mall.modules.system.entity.SysConfig;
import com.qingshop.mall.modules.system.mapper.SysConfigMapper;
import com.qingshop.mall.modules.system.service.ISysConfigService;
import com.qingshop.mall.modules.system.service.RedisService;
import com.qingshop.mall.modules.system.vo.ConfigStorageVo;

/**
 * ConfigServiceImpl
 */
@Service
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig> implements ISysConfigService {

	@Autowired
	private RedisService redisService;

	@Override
	public Map<String, String> selectAll() {
		Map<String, String> map = redisService.get(CacheKeyPrefix.SYS_CONFIG.getPrefix());
		if (map == null || StringUtils.isEmpty(map)) {
			List<SysConfig> sysConfigs = this.list();
			map = new HashMap<>(sysConfigs.size());
			for (SysConfig config : sysConfigs) {
				map.put(config.getSysKey(), config.getSysValue());
			}
			redisService.set(CacheKeyPrefix.SYS_CONFIG.getPrefix(), map);
		}
		return map;
	}

	@Override
	public int updateByKey(String key, String value) {
		redisService.del(CacheKeyPrefix.SYS_CONFIG.getPrefix());
		return this.baseMapper.updateByKey(key, value);
	}

	@Override
	@RedisCache(key = "CONFIG_STORAGE")
	public ConfigStorageVo selectStorageConfig() {
		return JsonUtils.jsonToBean(this.baseMapper.getByKey(ConfigKey.CONFIG_STORAGE.getValue()), ConfigStorageVo.class);
	}

	@Override
	@RedisCache(key = "CONFIG_STORAGE", flush = true)
	public void saveStorageConfig(ConfigStorageVo vo) {
		redisService.del(CacheKeyPrefix.SYS_CONFIG_STORAGE.getPrefix());
		this.baseMapper.updateByKey(ConfigKey.CONFIG_STORAGE.getValue(), JsonUtils.beanToJson(vo));
	}
}
