package com.qingshop.mall.modules.system.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qingshop.mall.modules.system.entity.SysMenu;
import com.qingshop.mall.modules.system.entity.SysUser;
import com.qingshop.mall.modules.system.entity.vo.MenuVO;
import com.qingshop.mall.modules.system.entity.vo.TreeMenuAllowAccess;

/**
 *
 * SysMenu 表数据服务层接口
 *
 */
public interface ISysMenuService extends IService<SysMenu> {

	/**
	 * 获取指定用户拥有权限
	 * 
	 * @param menuIds
	 *            该角色拥有的权限ID集合
	 * @param pid
	 *            菜单父ID
	 */
	List<TreeMenuAllowAccess> selectTreeMenuAllowAccessByMenuIdsAndPid(List<Long> menuIds, Long pid);

	/**
	 *查询所有菜单
	 */
	List<MenuVO> selectMenuByUserId(SysUser userId);

	

}