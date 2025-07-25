package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * @author: lushihao
 * @version: 1.0
 * create:   2025-05-20   16:13
 */
@RestController("adminDishController")
@RequestMapping("/admin/dish")
@Slf4j
@Api(tags = "菜品相关接口")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 新增菜品
     * @param dishDTO 菜品
     * @return 返回结果
     */
    @ApiOperation("新增菜品")
    @PostMapping
    @CacheEvict(cacheNames = "dishCache",key = "#dishDTO.getCategoryId()")
    public Result save(@RequestBody DishDTO dishDTO){
        log.info("新增菜品：{}",dishDTO);
        dishService.saveWithFlavor(dishDTO);
//        String key = "dish_"  + dishDTO.getCategoryId();
//        cleanCache(key);
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
    @CacheEvict(cacheNames = "dishCache",allEntries = true)
    public Result  delete(@RequestParam List<Long> ids){
        log.info("批量删除菜品，删除的IDs为 ：{}",ids);
        dishService.deleteBatch(ids);
        return Result.success();
    }

    @ApiOperation("根据ID查询")
    @GetMapping("/{id}")
    public Result<DishVO>  getById(@PathVariable Long id){
        log.info("根据ID查询菜品的相关信息，ID为 ：{}",id);
        DishVO dishVo = dishService.getByIdWithFlavor(id);
        return Result.success(dishVo);
    }

    @ApiOperation("更新相关信息")
    @PutMapping
    @CacheEvict(cacheNames = "dishCache",allEntries = true)
    public Result update(@RequestBody DishDTO dishDTO){
        log.info("更新菜品相关信息：{}",dishDTO);
        dishService.updateWithFlavor(dishDTO);

        return Result.success();
    }

    @PostMapping("/status/{status}")
    @ApiOperation("启用禁用菜品")
    @CacheEvict(cacheNames = "dishCache",allEntries = true)
    public Result<String> startOrStop(@PathVariable("status") Integer status, Long id){
        log.info("启用禁用菜品");
        dishService.startOrStop(status,id);
        return Result.success();
    }

    @GetMapping("/list")
    @ApiOperation("根据分类的ID查询菜品")
    public Result<List<Dish>> list(Long categoryId){
        log.info("根据分类的ID查询菜品 ID为：{}",categoryId);
        List<Dish> dishLists = dishService.list(categoryId);
        return Result.success(dishLists);
    }

//    /**
//     * 清理缓存数据
//     * @param pattern
//     */
//    private void cleanCache(String pattern){
//        Set keys = redisTemplate.keys(pattern);
//        redisTemplate.delete(keys);
//    }

}
