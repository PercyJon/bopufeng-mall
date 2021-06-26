package com.qingshop.mall.modules.system.vo;

import java.io.Serializable;

/**
 * 文件存储表
 */
public class SysUploadFile implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 存储方式
	 */
	private Integer ossType;
	/**
	 * 存储域名
	 */
	private String fileUrl;
	/**
	 * 文件名
	 */
	private String fileName;
	/**
	 * 文件路径
	 */
	private String filePath;
	/**
	 * 文件大小(字节)
	 */
	private Long fileSize;

	/**
	 * 文件类型
	 */
	private String fileType;

	/**
	 * 文件hash
	 */
	private String fileHash;

	/**
	 * 文件名
	 */
	private String originalName;

	public Integer getOssType() {
		return ossType;
	}

	public void setOssType(Integer ossType) {
		this.ossType = ossType;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getFileHash() {
		return fileHash;
	}

	public void setFileHash(String fileHash) {
		this.fileHash = fileHash;
	}

	public String getOriginalName() {
		return originalName;
	}

	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}

}
