package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;

import java.util.List;

/**
 * @author: lushihao
 * @version: 1.0
 * create:   2025-05-20   14:03
 */
public interface CategoryService {
    void save(CategoryDTO categoryDTO);

    PageResult pageQuery(CategoryPageQueryDTO categoryPageQuery);

    void deleteById(Long id);

    void update(CategoryDTO categoryDTO);

    void startOrStop(Integer status, Long id);

    List<Category> list(Integer type);
}
