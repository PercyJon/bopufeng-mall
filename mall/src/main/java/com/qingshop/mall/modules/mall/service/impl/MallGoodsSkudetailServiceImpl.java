package com.qingshop.mall.modules.mall.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qingshop.mall.modules.mall.entity.MallGoodsSkudetail;
import com.qingshop.mall.modules.mall.mapper.MallGoodsSkudetailMapper;
import com.qingshop.mall.modules.mall.service.IMallGoodsSkudetailService;

/**
 * <p>
 * 商品货品表 服务实现类
 * </p>
 *
 * @author
 * @since 2019-11-28
 */
@Service
public class MallGoodsSkudetailServiceImpl extends ServiceImpl<MallGoodsSkudetailMapper, MallGoodsSkudetail> implements IMallGoodsSkudetailService {

	public int addStock(Long skudetailId, Integer num) {
		return this.baseMapper.addStock(skudetailId, num);
	}

	public int reduceStock(Long skudetailId, Integer num) {
		return this.baseMapper.reduceStock(skudetailId, num);
	}
}