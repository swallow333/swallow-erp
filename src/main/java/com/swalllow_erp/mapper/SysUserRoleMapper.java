package com.swalllow_erp.mapper;



import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.swalllow_erp.entity.SysRole;
import com.swalllow_erp.entity.SysUserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

/**
 * @Author: Swallow333
 * @Date: 2026/06/23 23:07
 */

@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    @Select("SELECT role_id FROM sys_user_role WHERE user_id = #{userId}")
    List<Integer> selectRoleIdsByUserId(@Param("userId") Integer userId);

    @Select("SELECT r.* FROM sys_role r " +
            "JOIN sys_user_role ur ON r.id = ur.role_id " +
            "WHERE ur.user_id = #{userId}")
    List<SysRole> selectRolesByUserId(@Param("userId") Integer userId);
}