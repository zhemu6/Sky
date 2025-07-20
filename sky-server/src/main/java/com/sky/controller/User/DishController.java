package com.sky.controller.User;

import com.sky.constant.StatusConstant;
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
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: lushihao
 * @version: 1.0
 * create:   2025-05-20   16:13
 */
@RestController("userDishController")
@RequestMapping("/user/dish")
@Slf4j
@Api(tags = "菜品相关接口")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate redisTemplate;

    // 修改成使用Redis对菜品进行缓存
    @GetMapping("/list")
    @ApiOperation("根据分类的ID查询菜品")
    @Cacheable(cacheNames = "dishCache", key = "#categoryId")
    public Result<List<DishVO>> list(Long categoryId) {
        log.info("根据分类的ID查询菜品 ID为：{}",categoryId);

//        // 构造redis中的key
//        String key = "dish_" + categoryId;
//        // 查询redis中是否存在菜品数据
//        List<DishVO> list = (List<DishVO>) redisTemplate.opsForValue().get(key);
//
//        if(list!=null && !list.isEmpty()){
//            // 如果存在 直接返回 无需查询数据库
//            return Result.success(list);
//        }
        // 如果不存在 缓存到redis中
        Dish dish = new Dish();
        dish.setCategoryId(categoryId);
        dish.setStatus(StatusConstant.ENABLE);
        List<DishVO> list = dishService.listWithFlavor(dish);
//        redisTemplate.opsForValue().set(key,list);
        return Result.success(list);
    }

}
