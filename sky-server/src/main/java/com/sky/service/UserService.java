package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;

/**
 * @author: lushihao
 * @version: 1.0
 * create:   2025-07-16   23:49
 */
public interface UserService {

    User login(UserLoginDTO userLoginDTO);
}
