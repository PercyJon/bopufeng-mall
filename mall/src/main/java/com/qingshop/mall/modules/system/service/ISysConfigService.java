package com.qingshop.mall.modules.system.service;

import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qingshop.mall.modules.system.entity.SysConfig;
import com.qingshop.mall.modules.system.vo.ConfigStorageVo;

/**
 * ConfigService
 */
public interface ISysConfigService extends IService<SysConfig>{

	Map<String, String> selectAll();

	int updateByKey(String key, String value);

	ConfigStorageVo selectStorageConfig();

	void saveStorageConfig(ConfigStorageVo vo);

}
