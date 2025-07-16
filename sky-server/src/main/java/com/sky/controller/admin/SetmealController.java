package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.DishVO;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: lushihao
 * @version: 1.0
 * 套餐相关控制层代码
 * create:   2025-07-16   16:58
 */
@RestController
@RequestMapping("/admin/setmeal")
@Slf4j
@Api(tags = "套餐相关接口")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    @PostMapping
    public Result save(@RequestBody SetmealDTO setmealDTO){
        log.info("新增套餐：{}",setmealDTO);
        setmealService.saveWithDish(setmealDTO);
        return Result.success();
    }

    @ApiOperation("分页查询套餐")
    @GetMapping("/page")
    public Result<PageResult>  page(SetmealPageQueryDTO setmealPageQueryDTO){
        log.info("分页查询套餐，查询参数：{}",setmealPageQueryDTO);
        PageResult pageResult = setmealService.pageQuery(setmealPageQueryDTO);
        return Result.success(pageResult);
    }
    /**
     * 删除套餐
     * @param ids 需要删除的套餐的id
     * @return
     */
    @ApiOperation("批量删套餐")
    @DeleteMapping
    public Result  delete(@RequestParam List<Long> ids){
        log.info("批量删除套餐，删除的IDs为 ：{}",ids);
        setmealService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 根据ID 根据ID查询套餐相关信息
     * @param id 套餐ID
     * @return
     */
    @ApiOperation("根据ID查询套餐相关信息")
    @GetMapping("/{id}")
    public Result<SetmealVO>  getById(@PathVariable Long id){
        log.info("根据ID查询套餐相关信息，ID为 ：{}",id);
        SetmealVO setmealVO = setmealService.getByIdWithDish(id);
        return Result.success(setmealVO);
    }

    @ApiOperation("更新相关信息")
    @PutMapping
    public Result update(@RequestBody SetmealDTO setmealDTO){
        log.info("更新套餐相关信息：{}",setmealDTO);
        setmealService.updateWithDish(setmealDTO);
        return Result.success();
    }

    @PostMapping("/status/{status}")
    @ApiOperation("启用禁用套餐")
    public Result<String> startOrStop(@PathVariable("status") Integer status, Long id){
        log.info("启用禁用套餐");
        setmealService.startOrStop(status,id);
        return Result.success();
    }
}
