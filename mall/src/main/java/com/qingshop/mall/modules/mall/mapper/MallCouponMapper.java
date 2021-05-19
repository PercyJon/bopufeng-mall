package com.qingshop.mall.modules.mall.mapper;

import com.qingshop.mall.modules.mall.entity.MallCoupon;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 店家优惠券 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2019-12-31
 */
public interface MallCouponMapper extends BaseMapper<MallCoupon> {
	int reduceCouponNum(@Param("couponId") Long couponId);
}
