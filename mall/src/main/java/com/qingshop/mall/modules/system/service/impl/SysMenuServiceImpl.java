package com.qingshop.mall.modules.system.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.qingshop.mall.modules.system.entity.SysMenu;
import com.qingshop.mall.modules.system.entity.SysUser;
import com.qingshop.mall.modules.system.entity.vo.MenuChildVO;
import com.qingshop.mall.modules.system.entity.vo.MenuVO;
import com.qingshop.mall.modules.system.entity.vo.TreeMenuAllowAccess;
import com.qingshop.mall.modules.system.mapper.SysMenuMapper;
import com.qingshop.mall.modules.system.service.ISysMenuService;
import com.qingshop.mall.modules.system.service.ISysRoleMenuService;

/**
 * SysMenu 表数据服务层接口实现类
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {
	/**
	 * 角色菜单关系服务
	 */
	@Autowired
	private ISysRoleMenuService sysRoleMenuService;

	@Override
	public List<TreeMenuAllowAccess> selectTreeMenuAllowAccessByMenuIdsAndPid(final List<Long> menuIds, Long pid) {
		QueryWrapper<SysMenu> ew = new QueryWrapper<SysMenu>();
		ew.orderByAsc("sort");
		ew.eq("pid", pid);
		List<SysMenu> sysMenus = list(ew);
		List<TreeMenuAllowAccess> treeMenuAllowAccesss = new ArrayList<TreeMenuAllowAccess>();
		for (SysMenu sysMenu : sysMenus) {
			TreeMenuAllowAccess treeMenuAllowAccess = new TreeMenuAllowAccess();
			treeMenuAllowAccess.setSysMenu(sysMenu);
			/**
			 * 是否有权限
			 */
			if (menuIds.contains(sysMenu.getMenuId())) {
				treeMenuAllowAccess.setAllowAccess(true);
			}
			/**
			 * 子节点
			 */
			if (sysMenu.getDeep() < 3) {
				treeMenuAllowAccess.setChildren(selectTreeMenuAllowAccessByMenuIdsAndPid(menuIds, sysMenu.getMenuId()));
			}
			treeMenuAllowAccesss.add(treeMenuAllowAccess);
		}
		return treeMenuAllowAccesss;
	}

	@Override
	public List<MenuVO> selectMenuByUserId(SysUser user) {
		List<Long> menuIds = new ArrayList<>();
		if (user.isAdmin()) {
			List<SysMenu> menuList = this.list();
			for (SysMenu menu : menuList) {
				menuIds.add(menu.getMenuId());
			}
		} else {
			menuIds = sysRoleMenuService.selectRoleMenuIdsByUserId(user.getUserId());
		}
		QueryWrapper<SysMenu> ew = new QueryWrapper<SysMenu>();
		ew.orderByAsc("sort");
		ew.in("menu_id", menuIds.size() > 0 ? menuIds : Lists.newArrayList(RandomStringUtils.randomNumeric(30)));
		List<SysMenu> sysMenuList = list(ew);
		List<MenuVO> menuVO = new ArrayList<>();
		List<SysMenu> firstMenuList = sysMenuList.stream().filter(s -> s.getPid() == 0L).collect(Collectors.toList());
		for (SysMenu firstSysMenu : firstMenuList) {
			MenuVO vo = new MenuVO();
			vo.setId(firstSysMenu.getMenuId());
			vo.setText(firstSysMenu.getMenuName());
			vo.setIcon(firstSysMenu.getIcon());
			String id = firstSysMenu.getMenuId().toString();
			List<SysMenu> secondMenuList = sysMenuList.stream().filter(m -> id.equals(m.getPid().toString())).collect(Collectors.toList());
			List<MenuChildVO> childList = new ArrayList<>();
			for (SysMenu secondSysMenu : secondMenuList) {
				MenuChildVO child = new MenuChildVO();
				child.setId(secondSysMenu.getMenuId());
				child.setText(secondSysMenu.getMenuName());
				child.setIcon(secondSysMenu.getIcon());
				child.setUrl(secondSysMenu.getUrl());
				child.setTargetType("iframe-tab");
				childList.add(child);
			}
			vo.setChildren(childList);
			menuVO.add(vo);
		}
		return menuVO;
	}

}