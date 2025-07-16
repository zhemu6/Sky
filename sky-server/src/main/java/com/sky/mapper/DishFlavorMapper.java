package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author: lushihao
 * @version: 1.0
 * create:   2025-05-20   14:41
 */
@Mapper
public interface DishFlavorMapper {

    /**
     * 批量插入口味数据
     * @param flavors
     */

    void insertBatch(List<DishFlavor> flavors);

    /**
     * 根据菜品ID删除
     * @param dishId
     */
    @Delete("delete from dish_flavor where dish_id = #{dishId}")
    void deteteByDishId(Long dishId);

    void deteteByDishIds(List<Long> dishIds);

    /**
     * 根据菜品的ID查询口味表
     * @param dishId 菜品ID
     * @return 返回菜品提供的口味表
     */
    @Select("select * from dish_flavor where dish_id = #{dishId}")
    List<DishFlavor> getByDishId(Long dishId);
}
