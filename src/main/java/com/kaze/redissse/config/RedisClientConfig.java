package com.kaze.redissse.config;

import io.lettuce.core.RedisClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisClientConfig {
    @Value("${spring.redis.host}")
    private String redisHost;
    @Value("${spring.redis.port}")
    private String redisPort;
    @Value("${spring.redis.password}")
    private String redisPassword;
    @Bean
    public RedisClient createLettuceRedisClient(){
        return RedisClient.create(String.format("redis://%s@%s:%s", redisPassword, redisHost, redisPort));
    }
}
