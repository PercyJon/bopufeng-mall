package com.qingshop.mall.modules.oss;

import com.qingshop.mall.framework.config.OssConfig;

/**
 * 文件上传
 */
public final class OssFactory {

	public static OssService init(OssConfig config) {
		// 获取云存储配置信息
		if (config.getType() == OssTypeEnum.LOCAL.getValue()) {
			return new LocalOssService(config);
		} else if (config.getType() == OssTypeEnum.QINIU.getValue()) {
			return new QiniuOssService(config);
		} else if (config.getType() == OssTypeEnum.ALIYUN.getValue()) {
			return new AliyunOssService(config);
		} else if (config.getType() == OssTypeEnum.QCLOUD.getValue()) {
			return new QcloudOssService(config);
		}
		return null;
	}

}
