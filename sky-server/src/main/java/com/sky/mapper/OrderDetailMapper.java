package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
    * @author:  lushihao
    * @version: 1.0
    * create:   2025-05-20   14:05
    */
@Mapper
public interface OrderDetailMapper {
    /**
     * 批量插入
     * @param orderDetails
     */
    void insertBatch(List<OrderDetail> orderDetails);

    /**
     * 根据订单ID获得订单细节
     * @param orderId
     * @return
     */

    @Select("select * from order_detail where order_id = #{orderId}")
    List<OrderDetail> getByOrderId(Long orderId);
}
