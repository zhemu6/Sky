package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: lushihao
 * @version: 1.0
 * create:   2025-05-20   16:13
 */
@RestController
@RequestMapping("/admin/dish")
@Slf4j
@Api(tags = "菜品相关接口")
public class DishController {
    @Autowired
    private DishService dishService;

    /**
     * 新增菜品
     * @param dishDTO 菜品
     * @return 返回结果
     */
    @ApiOperation("新增菜品")
    @PostMapping
    public Result save(@RequestBody DishDTO dishDTO){
        log.info("新增菜品：{}",dishDTO);
        dishService.saveWithFlavor(dishDTO);
        return Result.success();
    }

    @ApiOperation("分页查询菜品")
    @GetMapping("/page")
    public Result<PageResult>  page(DishPageQueryDTO dishPageQueryDTO){
        log.info("分页查询菜品，查询参数：{}",dishPageQueryDTO);
        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 删除菜品
     * 1. 包含批量删除 针对单个菜品进行删除
     * 2. 删除之前 首先要确定菜品状态 是否是起售中
     * 3. 被套餐关联的菜品也不能删除
     * 4. 以及删除菜品之后 关联的口味数据也需要删掉
     * @param ids 需要删除的菜品的id
     * @return
     */
    @ApiOperation("批量删菜品")
    @DeleteMapping
    public Result  delete(@RequestParam List<Long> ids){
        log.info("批量删除菜品，删除的IDs为 ：{}",ids);
        dishService.deleteBatch(ids);
        return Result.success();
    }
}
