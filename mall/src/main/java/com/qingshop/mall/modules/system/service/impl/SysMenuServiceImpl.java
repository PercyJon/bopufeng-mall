package com.qingshop.mall.modules.system.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qingshop.mall.common.utils.StringUtils;
import com.qingshop.mall.modules.system.entity.SysMenu;
import com.qingshop.mall.modules.system.entity.SysUser;
import com.qingshop.mall.modules.system.mapper.SysMenuMapper;
import com.qingshop.mall.modules.system.service.ISysMenuService;
import com.qingshop.mall.modules.system.service.ISysRoleMenuService;
import com.qingshop.mall.modules.system.vo.MenuChildVO;
import com.qingshop.mall.modules.system.vo.MenuVO;
import com.qingshop.mall.modules.system.vo.TreeMenuAllowAccess;

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
			if (menuIds.contains(sysMenu.getMenuId())) { //设置权限标识
				treeMenuAllowAccess.setAllowAccess(true);
			}
			if (sysMenu.getDeep() < 3) { //递归调用设置子节点
				treeMenuAllowAccess.setChildren(selectTreeMenuAllowAccessByMenuIdsAndPid(menuIds, sysMenu.getMenuId()));
			}
			treeMenuAllowAccesss.add(treeMenuAllowAccess);
		}
		return treeMenuAllowAccesss;
	}
	
	@Override
	public List<MenuVO> selectMenuByUserId(SysUser user) {
		List<Long> menuIds = new ArrayList<>();
		List<MenuVO> menuVO = new ArrayList<>();
		if (user.isAdmin()) {
			List<SysMenu> menuList = this.list();
			menuIds = menuList.stream().map(m->m.getMenuId()).collect(Collectors.toList());
		} else {
			menuIds = sysRoleMenuService.selectRoleMenuIdsByUserId(user.getUserId());
		}
		if(StringUtils.isEmpty(menuIds)) {
			return menuVO;
		}
		QueryWrapper<SysMenu> ew = new QueryWrapper<SysMenu>();
		ew.in("menu_id", menuIds);
		ew.orderByAsc("sort");
		List<SysMenu> sysMenuList = list(ew);
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
	
	@Override
	public List<SysMenu> selectMenusByUserId(SysUser user) {
		List<Long> menuIds = new ArrayList<>();
		if (user.isAdmin()) {
			List<SysMenu> menuList = this.list();
			menuIds = menuList.stream().map(m->m.getMenuId()).collect(Collectors.toList());
		} else {
			menuIds = sysRoleMenuService.selectRoleMenuIdsByUserId(user.getUserId());
		}
		if(StringUtils.isEmpty(menuIds)) {
			return new ArrayList<>();
		}
		QueryWrapper<SysMenu> ew = new QueryWrapper<SysMenu>();
		ew.in("menu_id", menuIds);
		ew.lt("deep", 3);
		ew.orderByAsc("sort");
		List<SysMenu> sysMenuList = list(ew);
		return getTreeData(sysMenuList, 0);
	}
	
	@Override
	public List<SysMenu> getTreeData(List<SysMenu> list, int parentId) {
		List<SysMenu> returnList = new ArrayList<SysMenu>();
		for (Iterator<SysMenu> iterator = list.iterator(); iterator.hasNext();) {
			SysMenu t = (SysMenu) iterator.next();
			// 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
			if (t.getPid() == parentId) {
				recursionFn(list, t);
				returnList.add(t);
			}
		}
		return returnList;
	}
	
	/**
	 * 递归列表
	 * 
	 * @param list
	 * @param t
	 */
	private void recursionFn(List<SysMenu> list, SysMenu t) {
		// 得到子节点列表
		List<SysMenu> childList = getChildList(list, t);
		t.setChildren(childList);
		for (SysMenu tChild : childList) {
			if (hasChild(list, tChild)) {
				// 判断是否有子节点
				Iterator<SysMenu> it = childList.iterator();
				while (it.hasNext()) {
					SysMenu n = (SysMenu) it.next();
					recursionFn(list, n);
				}
			}
		}
	}

	/**
	 * 得到子节点列表
	 */
	private List<SysMenu> getChildList(List<SysMenu> list, SysMenu t) {
		List<SysMenu> tlist = new ArrayList<SysMenu>();
		Iterator<SysMenu> it = list.iterator();
		while (it.hasNext()) {
			SysMenu n = (SysMenu) it.next();
			if (n.getPid().longValue() == t.getMenuId().longValue()) {
				tlist.add(n);
			}
		}
		return tlist;
	}

	/**
	 * 判断是否有子节点
	 */
	private boolean hasChild(List<SysMenu> list, SysMenu t) {
		return getChildList(list, t).size() > 0 ? true : false;
	}

}