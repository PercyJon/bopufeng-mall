package com.qingshop.mall.modules.system.vo;

/**
 * <p>ClassName: MenuChildVO</p>
 * <p>Description: (首页菜单构造vo)</p>
 */
public class MenuChildVO {
	
	private Long id;
	
	private String text;
	
	private String icon;
	
	private String url;
	
	private String targetType;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}
	
}
