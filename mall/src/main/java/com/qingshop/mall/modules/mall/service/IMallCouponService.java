package com.qingshop.mall.modules.mall.service;

import com.qingshop.mall.modules.mall.entity.MallCart;
import com.qingshop.mall.modules.mall.entity.MallCoupon;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 店家优惠券 服务类
 * </p>
 *
 * @author 
 * @since 2019-12-31
 */
public interface IMallCouponService extends IService<MallCoupon> {

	List<MallCoupon> checkedCounpon(Long userId, MallCart cart);
	
	int reduceCouponNum(Long couponId);

}
