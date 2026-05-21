package com.swalllow_erp.mapper;

import com.swalllow_erp.SwallowErpApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = SwallowErpApplication.class)
public class SysUserMapperTest {
    @Autowired  // 自动注入
    private SysUserMapper sysUserMapper;  // 4. 注入Mapper对象

    @Test // 标记为测试方法
    public void test() {  // 测试方法
        // 调用Mapper的方法查询所有用户
        System.out.println(sysUserMapper.selectList(null));
    }
}