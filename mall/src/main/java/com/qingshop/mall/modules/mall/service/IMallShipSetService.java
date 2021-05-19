package com.qingshop.mall.modules.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qingshop.mall.common.bean.Rest;
import com.qingshop.mall.modules.mall.entity.MallShipSet;
import com.qingshop.mall.modules.mall.vo.ShipSetVo;

/**
 * <p>
 * 配送设置表 服务类
 * </p>
 *
 * @author 
 * @since 2019-12-17
 */
public interface IMallShipSetService extends IService<MallShipSet> {

	Rest insertShipSet(ShipSetVo shipSet);

}
