package com.swalllow_erp.mapper;



import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.swalllow_erp.entity.Supplier;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

/**
 * @Author: Swallow333
 * @Date: 2026/06/22 1:40
 */

@Mapper
public interface SupplierMapper extends BaseMapper<Supplier> {

    @Select("SELECT * FROM supplier WHERE code = #{code}")
    Supplier findByCode(@Param("code") String code);

    @Select("SELECT * FROM supplier WHERE status = 1 ORDER BY code")
    List<Supplier> selectEnabledList();

    @Select("SELECT * FROM supplier WHERE name LIKE CONCAT('%', #{keyword}, '%') OR code LIKE CONCAT('%', #{keyword}, '%')")
    List<Supplier> searchByKeyword(@Param("keyword") String keyword);
}