package com.sky.service;

import com.sky.vo.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author: lushihao
 * @version: 1.0
 * create:   2025-05-20   16:53
 */
public interface WorkspaceService {

    BusinessDataVO getBusinessData(LocalDateTime begin, LocalDateTime end);

    OrderOverViewVO getOrderOverView();

    DishOverViewVO getDishOverView();

    SetmealOverViewVO getSetmealOverView();
}
