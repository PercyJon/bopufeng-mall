package com.qingshop.mall.modules.system.entity.vo;

import java.util.List;

public class MenuVO {
	
	private Long id;
	
	private String text;
	
	private String icon;
	
	private List<MenuChildVO> children;

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

	public List<MenuChildVO> getChildren() {
		return children;
	}

	public void setChildren(List<MenuChildVO> children) {
		this.children = children;
	}

}
