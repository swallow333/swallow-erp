package com.swalllow_erp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.swalllow_erp.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper // 用于标识接口，以便将SQL映射到接口
public interface SysUserMapper extends BaseMapper<SysUser> {

}