package com.qingshop.mall.modules.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qingshop.mall.modules.mall.entity.MallGoodsSkudetail;

/**
 * <p>
 * 商品货品表 服务类
 * </p>
 *
 * @author
 * @since 2019-11-28
 */
public interface IMallGoodsSkudetailService extends IService<MallGoodsSkudetail> {

	public int addStock(Long skudetailId, Integer num);

	public int reduceStock(Long skudetailId, Integer num);
}
