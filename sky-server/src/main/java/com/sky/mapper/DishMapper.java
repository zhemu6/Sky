package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author: lushihao
 * @version: 1.0
 * create:   2025-05-20   14:41
 */
@Mapper
public interface DishMapper {

    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);
}
