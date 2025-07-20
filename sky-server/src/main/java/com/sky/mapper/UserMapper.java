package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

/**
 * @author: lushihao
 * @version: 1.0
 * create:   2025-07-16   23:49
 */
@Mapper
public interface UserMapper {
    @Select("select * from user where openid=#{openid}")
    User getByOpenid(String openid);

    void insert(User user);

    User getById(Long userId);

    Integer sumByMap(Map<String, Object> map);


}
