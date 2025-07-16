package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

import java.util.List;

/**
 * @author: lushihao
 * @version: 1.0
 * create:   2025-07-16   17:00
 */
public interface SetmealService {
    void saveWithDish(SetmealDTO setmealDTO);

    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    void deleteBatch(List<Long> ids);

    void startOrStop(Integer status, Long id);

    SetmealVO getByIdWithDish(Long id);

    void updateWithDish(SetmealDTO setmealDTO);
}
