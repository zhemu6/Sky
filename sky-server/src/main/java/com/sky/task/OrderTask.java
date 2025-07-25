package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author: lushihao * @version: 1.0
 * create:   2025-07-20   12:05
 * 定时任务类 定时处理订单任务
 */
@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;

    @Scheduled(cron = "0 * * * * ?")
    public void processTimeoutOrder(){
        log.info("定时处理超时订单：{}", LocalDateTime.now());
        // 首先查询
        List<Orders> ordersList = orderMapper.getByStatusAndOrderTimeLT(Orders.PENDING_PAYMENT, LocalDateTime.now().plusMinutes(-15));

        if(ordersList!=null && !ordersList.isEmpty()){
            for(Orders orders:ordersList){
                orders.setStatus(Orders.CANCELLED);
                orders.setCancelReason("订单超时，自动取消");
                orders.setCancelTime(LocalDateTime.now());
                orderMapper.update(orders);
            }
        }
    }

    /**
     * 处理一直处于派送的订单 每天凌晨一点
     */
    @Scheduled(cron = "0 0 1 * *  ?")
    public void processDeliveryOrder(){
        log.info("处理一直处于派送的订单：{}", LocalDateTime.now());
        // 首先查询 一直处于派送中
        List<Orders> ordersList = orderMapper.getByStatusAndOrderTimeLT(Orders.DELIVERY_IN_PROGRESS, LocalDateTime.now().plusMinutes(-60));

        if(ordersList!=null && !ordersList.isEmpty()){
            for(Orders orders:ordersList){
                orders.setStatus(Orders.COMPLETED);
                orderMapper.update(orders);
            }
        }
    }

}
