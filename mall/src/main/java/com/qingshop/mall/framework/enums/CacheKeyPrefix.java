package com.qingshop.mall.framework.enums;

public enum CacheKeyPrefix {

	SYS_CONFIG("MALL_SYS_CONFIG"), 
	SYS_CONFIG_STORAGE("MALL_CONFIG_STORAGE");

	String prefix;

	CacheKeyPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getPrefix() {
		return prefix;
	}
}
