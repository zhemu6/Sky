package com.sky.controller.User;

import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: lushihao
 * @version: 1.0
 * create:   2025-07-16   21:46
 */
@RestController("userAddressBookController")
@RequestMapping("/user/addressBook")
@Api(tags = "地址相关接口")
@Slf4j
public class AddressBookController {
    @Autowired
    private AddressBookService addressBookService;

    /**
     * 查询当前用户的所有地址信息
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("查询当前用户的所有地址信息")
    public Result<List<AddressBook>> getStatus(){
        log.info("查询当前用户的所有地址信息");
        AddressBook addressBook = new AddressBook();
        Long userId = BaseContext.getCurrentId();
        addressBook.setUserId(userId);
        List<AddressBook> addressBooks = addressBookService.list(addressBook);

        return Result.success(addressBooks);
    }

    /**
     * 新增地址信息
     * @param addressBook
     * @return
     */
    @PostMapping
    @ApiOperation("新增地址")
    public Result save(@RequestBody AddressBook addressBook){
        log.info("新增地址信息 信息为：{}",addressBook);
        addressBookService.save(addressBook);
        return Result.success();
    }

    /**
     * 根据id查询信息
     * @param id 用户id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询地址信息")
    public Result<AddressBook> save(@PathVariable Long id){
        log.info("根据id查询地址信息 id为：{}",id);
        AddressBook addressBook =  addressBookService.getById(id);
        return Result.success(addressBook);
    }

    /**
     * 根据id修改地址
     *
     * @param addressBook
     * @return
     */
    @PutMapping
    @ApiOperation("根据id修改地址")
    public Result update(@RequestBody AddressBook addressBook) {
        addressBookService.update(addressBook);
        return Result.success();
    }

    /**
     * 设置默认的地址
     * @param addressBook
     * @return
     */
    @PutMapping("/default")
    @ApiOperation("设置默认的地址")
    public Result setDefault(@RequestBody AddressBook addressBook){
        addressBookService.setDefault(addressBook);
        return Result.success();
    }

    /**
     * 根据ID删除地址
     * @param id
     * @return
     */
    @DeleteMapping
    @ApiOperation("根据ID删除地址")
    public Result deleteById(Long id){
        addressBookService.deleteById(id);
        return Result.success();
    }

    /**
     *
     */
    @GetMapping("/default")
    @ApiOperation("查询默认地址")
    public Result<AddressBook> getDefault(){
        AddressBook addressBook = new AddressBook();
        addressBook.setIsDefault(1);
        addressBook.setUserId(BaseContext.getCurrentId());
        List<AddressBook> list = addressBookService.list(addressBook);

        if (list!=null && !list.isEmpty()){
            return Result.success(list.get(0));
        }

        return Result.error("没有查询到默认地址");
    }




}
