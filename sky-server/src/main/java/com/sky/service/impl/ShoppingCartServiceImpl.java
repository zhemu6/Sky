package com.sky.service.impl;


import com.sky.context.BaseContext;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;

import com.sky.entity.ShoppingCart;

import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;

import com.sky.service.ShoppingCartService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author: lushihao
 * @version: 1.0
 * create:   2025-07-16   17:00
 */
@Service
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 添加购物车
     * @param shoppingCartDTO
     */
    @Override
    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        // 添加购物车逻辑
        // 判断当前加入到购物车中的商品是否存在
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);
        // 查询购物车数据
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        // 如果已经存在 数量加一
        if(list!=null && !list.isEmpty()){
            ShoppingCart cart = list.get(0);
            cart.setNumber(cart.getNumber()+1);
            shoppingCartMapper.updateNumberId(cart);
        }else {
            // 否则 插入一条数据
            Long dishId = shoppingCartDTO.getDishId();
            if(dishId!=null){
                // 本次添加到购物车的是菜品
                Dish dish = dishMapper.getById(dishId);
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());
            }else{
                // 本次添加到购物车的是套餐
                Long setmeanlId= shoppingCartDTO.getSetmealId();
                Setmeal setmeal = setmealMapper.getById(setmeanlId);
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());
            }
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            // 统一插入
            shoppingCartMapper.insert(shoppingCart);
        }
    }

    @Override
    public List<ShoppingCart> showShoppingCart() {
        Long userId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = ShoppingCart.builder().userId(userId).build();
        return shoppingCartMapper.list(shoppingCart);
    }

    @Override
    public void cleanShoppingCart() {
        Long userId = BaseContext.getCurrentId();
        shoppingCartMapper.deleteByUserId(userId);

    }


    @Override
    public void subShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        // 清空购物车逻辑
        // 获得当前购物车中信息
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        //设置查询条件，查询当前登录用户的购物车数据
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);
        // 查询购物车数据
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);

        // 如果已经存在 数量加一
        if(list!=null && !list.isEmpty()){
            ShoppingCart cart = list.get(0);

            // 获得这个购物车中的数量
            Integer number  = cart.getNumber();
            // 如果当前这个购物车中数量为1 直接从数据库中删除
            if(number==1){
                shoppingCartMapper.deleteById(shoppingCart.getId());

            }else{
                cart.setNumber(cart.getNumber()-1);
                shoppingCartMapper.updateNumberId(cart);
            }
        }
    }
}
