package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author: lushihao
 * @version: 1.0
 * create:   2025-05-20   14:42
 */
@Mapper
public interface SetmealMapper {

    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

}
