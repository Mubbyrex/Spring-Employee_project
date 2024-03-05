package com.example.thymeleaf.project.Config;

import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.Connection;
import redis.clients.jedis.Jedis;

import static java.lang.System.getProperty;

@Configuration
public class RedisConfig {

@Bean
public JedisConnectionFactory redisConnectionFactory() {
    RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
    redisStandaloneConfiguration.setHostName("redis-12397.c281.us-east-1-2.ec2.cloud.redislabs.com");
    redisStandaloneConfiguration.setPort(12397);
    redisStandaloneConfiguration.setPassword(RedisPassword.of(System.getenv("REDIS_PASSWORD")));
    redisStandaloneConfiguration.setUsername("admin");

    return new JedisConnectionFactory(redisStandaloneConfiguration);
}


    @Bean
    public RedisTemplate<String, String> redisTemplate(){
        RedisTemplate<String, String> template = new RedisTemplate<>();

        template.setConnectionFactory(redisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer()); // Key as String
        template.setValueSerializer(new StringRedisSerializer()); // Value as String
        return template;
    }
}