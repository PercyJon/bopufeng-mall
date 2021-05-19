package com.qingshop.mall.modules.system.entity.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.qingshop.mall.modules.system.entity.SysMenu;


/**
 * 菜单树
 */
public class TreeMenu implements Serializable {

	/**
	 * @Fields serialVersionUID : TODO()
	 */

	private static final long serialVersionUID = 1L;
	/**
	 * 菜单
	 */
	private SysMenu sysMenu;
	/**
	 * 子菜单
	 */
	private List<TreeMenu> children = new ArrayList<TreeMenu>();

	public SysMenu getSysMenu() {
		return sysMenu;
	}

	public void setSysMenu(SysMenu sysMenu) {
		this.sysMenu = sysMenu;
	}

	public List<TreeMenu> getChildren() {
		return children;
	}

	public void setChildren(List<TreeMenu> children) {
		this.children = children;
	}

}
