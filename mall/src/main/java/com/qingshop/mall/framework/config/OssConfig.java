package com.qingshop.mall.framework.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OssConfig {

	// 类型 0-本地，1：七牛 2：阿里云 3：腾讯云
	@Value("${oss.type}")
	private Integer type;

	// 本地文件服务器
	@Value("${oss.localDomain}")
	private String localDomain;
	// 本地前綴
	@Value("${oss.localPrefix}")
	private String localPrefix;

	// 七牛绑定的域名
	@Value("${oss.qiniuDomain}")
	private String qiniuDomain;
	// 七牛路径前缀
	@Value("${oss.qiniuPrefix}")
	private String qiniuPrefix;
	// 七牛ACCESS_KEY
	@Value("${oss.qiniuAccessKey}")
	private String qiniuAccessKey;
	// 七牛SECRET_KEY
	@Value("${oss.qiniuSecretKey}")
	private String qiniuSecretKey;
	// 七牛存储空间名
	@Value("${oss.qiniuBucketName}")
	private String qiniuBucketName;

	// 阿里云绑定的域名
	@Value("${oss.aliyunDomain}")
	private String aliyunDomain;
	// 阿里云路径前缀
	@Value("${oss.aliyunPrefix}")
	private String aliyunPrefix;
	// 阿里云EndPoint
	@Value("${oss.aliyunEndPoint}")
	private String aliyunEndPoint;
	// 阿里云AccessKeyId
	@Value("${oss.aliyunAccessKeyId}")
	private String aliyunAccessKeyId;
	// 阿里云AccessKeySecret
	@Value("${oss.aliyunAccessKeySecret}")
	private String aliyunAccessKeySecret;
	// 阿里云BucketName
	@Value("${oss.aliyunBucketName}")
	private String aliyunBucketName;

	// 腾讯云绑定的域名
	@Value("${oss.qcloudDomain}")
	private String qcloudDomain;
	// 腾讯云路径前缀
	@Value("${oss.qcloudPrefix}")
	private String qcloudPrefix;
	// 腾讯云SecretId
	@Value("${oss.qcloudSecretId}")
	private String qcloudSecretId;
	// 腾讯云SecretKey
	@Value("${oss.qcloudSecretKey}")
	private String qcloudSecretKey;
	// 腾讯云BucketName
	@Value("${oss.qcloudBucketName}")
	private String qcloudBucketName;
	// 腾讯云COS所属地区
	@Value("${oss.qcloudRegion}")
	private String qcloudRegion;

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getLocalDomain() {
		return localDomain;
	}

	public void setLocalDomain(String localDomain) {
		this.localDomain = localDomain;
	}

	public String getLocalPrefix() {
		return localPrefix;
	}

	public void setLocalPrefix(String localPrefix) {
		this.localPrefix = localPrefix;
	}

	public String getQiniuDomain() {
		return qiniuDomain;
	}

	public void setQiniuDomain(String qiniuDomain) {
		this.qiniuDomain = qiniuDomain;
	}

	public String getQiniuPrefix() {
		return qiniuPrefix;
	}

	public void setQiniuPrefix(String qiniuPrefix) {
		this.qiniuPrefix = qiniuPrefix;
	}

	public String getQiniuAccessKey() {
		return qiniuAccessKey;
	}

	public void setQiniuAccessKey(String qiniuAccessKey) {
		this.qiniuAccessKey = qiniuAccessKey;
	}

	public String getQiniuSecretKey() {
		return qiniuSecretKey;
	}

	public void setQiniuSecretKey(String qiniuSecretKey) {
		this.qiniuSecretKey = qiniuSecretKey;
	}

	public String getQiniuBucketName() {
		return qiniuBucketName;
	}

	public void setQiniuBucketName(String qiniuBucketName) {
		this.qiniuBucketName = qiniuBucketName;
	}

	public String getAliyunDomain() {
		return aliyunDomain;
	}

	public void setAliyunDomain(String aliyunDomain) {
		this.aliyunDomain = aliyunDomain;
	}

	public String getAliyunPrefix() {
		return aliyunPrefix;
	}

	public void setAliyunPrefix(String aliyunPrefix) {
		this.aliyunPrefix = aliyunPrefix;
	}

	public String getAliyunEndPoint() {
		return aliyunEndPoint;
	}

	public void setAliyunEndPoint(String aliyunEndPoint) {
		this.aliyunEndPoint = aliyunEndPoint;
	}

	public String getAliyunAccessKeyId() {
		return aliyunAccessKeyId;
	}

	public void setAliyunAccessKeyId(String aliyunAccessKeyId) {
		this.aliyunAccessKeyId = aliyunAccessKeyId;
	}

	public String getAliyunAccessKeySecret() {
		return aliyunAccessKeySecret;
	}

	public void setAliyunAccessKeySecret(String aliyunAccessKeySecret) {
		this.aliyunAccessKeySecret = aliyunAccessKeySecret;
	}

	public String getAliyunBucketName() {
		return aliyunBucketName;
	}

	public void setAliyunBucketName(String aliyunBucketName) {
		this.aliyunBucketName = aliyunBucketName;
	}

	public String getQcloudDomain() {
		return qcloudDomain;
	}

	public void setQcloudDomain(String qcloudDomain) {
		this.qcloudDomain = qcloudDomain;
	}

	public String getQcloudPrefix() {
		return qcloudPrefix;
	}

	public void setQcloudPrefix(String qcloudPrefix) {
		this.qcloudPrefix = qcloudPrefix;
	}

	public String getQcloudSecretId() {
		return qcloudSecretId;
	}

	public void setQcloudSecretId(String qcloudSecretId) {
		this.qcloudSecretId = qcloudSecretId;
	}

	public String getQcloudSecretKey() {
		return qcloudSecretKey;
	}

	public void setQcloudSecretKey(String qcloudSecretKey) {
		this.qcloudSecretKey = qcloudSecretKey;
	}

	public String getQcloudBucketName() {
		return qcloudBucketName;
	}

	public void setQcloudBucketName(String qcloudBucketName) {
		this.qcloudBucketName = qcloudBucketName;
	}

	public String getQcloudRegion() {
		return qcloudRegion;
	}

	public void setQcloudRegion(String qcloudRegion) {
		this.qcloudRegion = qcloudRegion;
	}

}
