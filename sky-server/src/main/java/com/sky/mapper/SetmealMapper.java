package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author: lushihao
 * @version: 1.0
 * create:   2025-05-20   14:42
 */
@Mapper
public interface SetmealMapper {

    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    @AutoFill(value = OperationType.INSERT)
    void insert(Setmeal setmeal);

    void insertBatch(List<SetmealDish> setmealDishes);

    Page<SetmealVO> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);
    @Select("select * from setmeal where id = #{id}")
    Setmeal getById(Long id);

    void deleteByIds(List<Long> ids);

    @AutoFill(value = OperationType.UPDATE)
    void update(Setmeal setmeal);
}
