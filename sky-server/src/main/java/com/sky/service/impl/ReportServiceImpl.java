package com.sky.service.impl;


import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.*;

import com.sky.service.ReportService;

import com.sky.vo.*;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDate;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: lushihao
 * @version: 1.0
 * create:   2025-05-20   16:53
 */
@Slf4j
@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Override
    public TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end) {
        // 首先计算日期的dateList
        List<LocalDate> dateList = new ArrayList<>();
        LocalDate date = begin;
        while (!date.isAfter(end)) {
            dateList.add(date);
            date = date.plusDays(1);
        }
        // 2. 计算每天营业额
        List<Double> turnoverList = new ArrayList<>();
        for (LocalDate d : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(d, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(d, LocalTime.MAX);

            Map<String, Object> map = new HashMap<>();
            map.put("begin", beginTime);
            map.put("end", endTime);
            map.put("status", Orders.COMPLETED);

            Double turnover = orderMapper.sumByMap(map);
            turnoverList.add(turnover != null ? turnover : 0.0);
        }

        log.info("dateList: {}", StringUtils.join(dateList, ","));
        log.info("turnoverList: {}", StringUtils.join(turnoverList, ","));

        return TurnoverReportVO
                .builder()
                .dateList(StringUtils.join(dateList, ","))
                .turnoverList(StringUtils.join(turnoverList, ","))
                .build();
    }

    @Override
    public UserReportVO getUserStatistics(LocalDate begin, LocalDate end) {
        // 首先计算日期的dateList
        List<LocalDate> dateList = new ArrayList<>();
        LocalDate date = begin;
        while (!date.isAfter(end)) {
            dateList.add(date);
            date = date.plusDays(1);
        }


        // 2. 计算每天新增用户
        List<Integer> newList = new ArrayList<>();
        // 总的用户数量
        List<Integer> totalUserList = new ArrayList<>();
        for (LocalDate d : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(d, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(d, LocalTime.MAX);

            Map<String, Object> map = new HashMap<>();
            // 查询总的用户数量
            map.put("end", endTime);
            Integer totalUserNumber = userMapper.sumByMap(map);
            totalUserList.add(totalUserNumber != null ? totalUserNumber : 0);
            // 查询新增的用户数量
            map.put("begin", beginTime);
            Integer userNumber = userMapper.sumByMap(map);
            newList.add(userNumber != null ? userNumber : 0);
        }
        return UserReportVO
                .builder()
                .dateList(StringUtils.join(dateList, ","))
                .newUserList(StringUtils.join(newList, ","))
                .totalUserList(StringUtils.join(totalUserList, ","))
                .build();
    }

    /**
     * 订单统计
     * @param begin 开始时间
     * @param end 结束日期
     * @return
     */
    @Override
    public OrderReportVO getOrdersStatistics(LocalDate begin, LocalDate end) {
        // 首先计算日期的dateList
        List<LocalDate> dateList = new ArrayList<>();
        LocalDate date = begin;
        while (!date.isAfter(end)) {
            dateList.add(date);
            date = date.plusDays(1);
        }
        // 2. 计算每天的订单数 有效订单数 总的订单数 总的有效订单数 以及订单完成率
        // 每天的订单数
        List<Integer> orderCountList = new ArrayList<>();
        // 每天的有效订单数
        List<Integer> validOrderCountList = new ArrayList<>();
        for (LocalDate d : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(d, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(d, LocalTime.MAX);
            Map<String, Object> map = new HashMap<>();

            // 当前这一天总的订单数
            map.put("end", endTime);
            map.put("begin", beginTime);
            Integer orderNumber = orderMapper.sumOrderByMap(map);
            orderCountList.add(orderNumber!=null ? orderNumber : 0);
            // 当前这一天有效的订单数
            map.put("status", Orders.COMPLETED);
            Integer validOrderNumber = orderMapper.sumOrderByMap(map);
            validOrderCountList.add(validOrderNumber != null ? validOrderNumber : 0);
        }
        Integer  totalOrderCount = orderCountList.stream().reduce(Integer::sum).get();
        Integer validOrderCount = validOrderCountList.stream().reduce(Integer::sum).get();
        Double orderCompletionRate = 0.0;
        if (totalOrderCount!=0){
            orderCompletionRate = validOrderCount.doubleValue() / totalOrderCount;
        }
        return OrderReportVO
                .builder()
                .dateList(StringUtils.join(dateList, ","))
                .validOrderCountList(StringUtils.join(validOrderCountList, ","))
                .orderCountList(StringUtils.join(orderCountList, ","))
                .orderCompletionRate(orderCompletionRate)
                .totalOrderCount(totalOrderCount)
                .validOrderCount(validOrderCount)
                .build();
    }

    @Override
    public SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end) {
        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);
        List<GoodsSalesDTO> salesTop10 = orderMapper.getSalesTop10(beginTime, endTime);

        List<String> names = salesTop10.stream().map(GoodsSalesDTO::getName).collect(Collectors.toList());
        List<Integer> numbers = salesTop10.stream().map(GoodsSalesDTO::getNumber).collect(Collectors.toList());

        return SalesTop10ReportVO
                .builder()
                .nameList(StringUtils.join(names, ","))
                .numberList(StringUtils.join(numbers, ","))
                .build();

    }
}
