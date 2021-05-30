package com.qingshop.mall.modules.system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qingshop.mall.common.utils.JsonUtils;
import com.qingshop.mall.common.utils.StringUtils;
import com.qingshop.mall.framework.annotation.RedisCache;
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
	public String selectAll() {
		String value = redisService.get(ConfigKey.SYS_CONFIG.getValue());
		if (value == null || StringUtils.isEmpty(value)) {
			value = this.baseMapper.getByKey(ConfigKey.CONFIG_STORAGE.getValue());
			redisService.set(ConfigKey.SYS_CONFIG.getValue(), value);
		}
		return value;
	}

	@Override
	@RedisCache(key = "SYS_CONFIG")
	public String selectStorageConfig() {
		return this.baseMapper.getByKey(ConfigKey.CONFIG_STORAGE.getValue());
	}

	@Override
	@RedisCache(key = "SYS_CONFIG", flush = true)
	public void saveStorageConfig(ConfigStorageVo vo) {
		this.baseMapper.updateByKey(ConfigKey.CONFIG_STORAGE.getValue(), JsonUtils.beanToJson(vo));
	}

}
