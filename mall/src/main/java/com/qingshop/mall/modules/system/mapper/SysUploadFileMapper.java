package com.qingshop.mall.modules.system.mapper;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qingshop.mall.modules.system.entity.SysUploadFile;

/**
 * <p>
 * 文件存储表 Mapper 接口
 * </p>
 *
 * @author lich
 * @since 2019-09-17
 */
public interface SysUploadFileMapper extends BaseMapper<SysUploadFile> {

	int updateUploadFileGroupId(@Param("fileIds") String[] fileIds, @Param("groupId") Long groupId);

}
