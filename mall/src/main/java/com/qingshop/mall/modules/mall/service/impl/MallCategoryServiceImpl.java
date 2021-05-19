package com.qingshop.mall.modules.mall.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qingshop.mall.common.bean.Ztree;
import com.qingshop.mall.common.exception.BusinessException;
import com.qingshop.mall.common.utils.idwork.DistributedIdWorker;
import com.qingshop.mall.modules.mall.entity.MallCategory;
import com.qingshop.mall.modules.mall.mapper.MallCategoryMapper;
import com.qingshop.mall.modules.mall.service.IMallCategoryService;

/**
 * 类目表 服务实现类
 */
@Service
public class MallCategoryServiceImpl extends ServiceImpl<MallCategoryMapper, MallCategory> implements IMallCategoryService {

	@Override
	public List<Long> selectCategoryIdByPid(String parentId) {
		List<Long> categorys = baseMapper.selectCategoryIdByPid(parentId);
		return categorys;
	}

	@Override
	public void insertMallCategory(MallCategory mallCategory) {
		mallCategory.setCategoryId(DistributedIdWorker.nextId());
		MallCategory info = this.getById(mallCategory.getParentId());
		if (info == null) {
			mallCategory.setAncestors(String.valueOf(mallCategory.getParentId()));
		} else {
			// 如果父节点不为"正常"状态,则不允许新增子节点
			if ("0".equals(info.getStatus())) {
				throw new BusinessException("类目停用，不允许新增");
			}
			mallCategory.setAncestors(info.getAncestors() + "," + mallCategory.getParentId());
		}
		this.save(mallCategory);
	}

	@Override
	public List<Ztree> selectCategoryTree(MallCategory mallCategory) {
		List<MallCategory> categoryList = baseMapper.selectCategoryList(mallCategory);
		List<Ztree> ztrees = initZtree(categoryList);
		return ztrees;
	}

	/**
	 * 对象转类目树
	 * 
	 * @param categoryList
	 *            类目列表
	 * @return 树结构列表
	 */
	public List<Ztree> initZtree(List<MallCategory> categoryList) {
		List<Ztree> ztrees = new ArrayList<Ztree>();
		for (MallCategory category : categoryList) {
			if ("1".equals(category.getStatus())) {
				Ztree ztree = new Ztree();
				ztree.setId(category.getCategoryId());
				ztree.setpId(category.getParentId());
				ztree.setName(category.getName());
				ztree.setTitle(category.getDescripte());
				ztree.setChecked(false);
				ztrees.add(ztree);
			}
		}
		return ztrees;
	}
}
