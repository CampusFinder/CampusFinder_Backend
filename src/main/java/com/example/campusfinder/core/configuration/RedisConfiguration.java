package com.example.campusfinder.core.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * packageName    : com.example.campusfinder.core.configuration
 * fileName       : RedisConfiguration
 * author         : tlswl
 * date           : 2024-08-19
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-19        tlswl       최초 생성
 */
@Configuration
public class RedisConfiguration {

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

   //Redis와의 연결을 관리하는 ConnectionFactory 생성
    @Bean
    public RedisConnectionFactory redisConnectionFactory(){
        return new LettuceConnectionFactory(redisHost,redisPort);
    }


    //Redis와 상호작용을 위한 RedisTemplate 생성
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){
        return createRedisTemplate(redisConnectionFactory);
    }

    //JWT 블랙리스트 관리를 위한 별도의 RedisTemplate 생성
    @Bean
    public RedisTemplate<String, Object> blackListTemplate(RedisConnectionFactory redisConnectionFactory) {
        return createRedisTemplate(redisConnectionFactory);
    }

    private RedisTemplate<String, Object> createRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    }
}
