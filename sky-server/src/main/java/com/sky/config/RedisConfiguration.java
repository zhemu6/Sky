package com.sky.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author: lushihao
 * @version: 1.0
 * create:   2025-07-16   21:10
 */
@Configuration
@Slf4j
public class RedisConfiguration {
    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory){
        log.info("开始常见redis模板对象");
        RedisTemplate redisTemplate = new RedisTemplate<>();
        // 设置Redis的连接工厂对象
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // 设置Redis中 key的序列化器
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    }
}
