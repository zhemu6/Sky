package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.bridge.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: lushihao
 * @version: 1.0
 * create:   2025-07-16   23:49
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WeChatProperties weChatProperties;

    public static final String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";
    /**
     * 用户登录的具体实现类 通过前端传给服务器的code 结合secret和appid实现
     * @param userLoginDTO
     * @return
     */
    @Override
    public User login(UserLoginDTO userLoginDTO) {
        String openid = getOpenId(userLoginDTO.getCode());
        // 对Openid去做校验
        if(openid== null){
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }
        // 如果这个用户是新用户 存起来Openid 自动完成注册实现 返回用户对象
        User user = userMapper.getByOpenid(openid);

        if (user==null){
            user = User.builder().openid(openid).createTime(LocalDateTime.now()).build();
            userMapper.insert(user);
        }
        // 返回这个对象
        return user;
    }
    private String getOpenId(String code){
        // 调用微信接口服务 获取当前用户的Openid https://api.weixin.qq.com/sns/jscode2session?appid=wxfc910c6008950980&secret=8ce6f32e638001826ac67ea7c0a01dcb&js_code=0c3tg6000e41CU1NVH200zrGZ02tg60E&grant_type=authorization_code
        Map<String, String> map = new HashMap<>();

        map.put("appid",weChatProperties.getAppid());
        map.put("secret", weChatProperties.getSecret());
        map.put("js_code",code);
        map.put("grant_type","authorization_code");
        String json = HttpClientUtil.doGet(WX_LOGIN, map);
        JSONObject jsonObject = JSON.parseObject(json);
        // 拿到openid
        return jsonObject.getString("openid");
    }
}
