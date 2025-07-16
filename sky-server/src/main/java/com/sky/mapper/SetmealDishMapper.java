package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author: lushihao
 * @version: 1.0
 * create:   2025-05-20   14:42
 */
@Mapper
public interface SetmealDishMapper {

    /**
     * 根据菜品ID查询套餐的ID
     * @param dishIds
     * @return
     */

    List<Long> getSetmealIdsByDishIds(List<Long> dishIds);

    void deleteBySetmealIds(List<Long> setmealIds);

    @Select("select * from setmeal_dish where setmeal_id = #{setmealId} ")
    List<SetmealDish> getbySetmealId(Long setmealId);

    @Delete("delete from setmeal_dish where setmeal_id =#{setmealId}")
    void deleteBySetmealId(Long setmealId);
}
