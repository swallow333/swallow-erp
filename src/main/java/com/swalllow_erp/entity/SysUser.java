package com.swalllow_erp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data   // Lombok：生成getter/setter/tostring
@TableName("sys_user")  // Mybatis Plus:指明表名
public class SysUser {
    @TableId(type = IdType.AUTO)    // Mybatis Plus:指明主键，属性自增
    private Integer id;
    private String username;
    private String password;
    private String nickname;
    private String phone;
    private Integer status;
    private LocalDateTime createTime;
    private String updateBy;
    private LocalDateTime updateTime;
    private Integer deleted;
}
