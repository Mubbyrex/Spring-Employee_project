package com.example.thymeleaf.project.Service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class JWTBlacklistService {
    private final RedisTemplate<String,String> redisTemplate;

    public JWTBlacklistService(RedisTemplate<String,String> redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    public void blacklistToken(String token){
        redisTemplate.opsForValue().set(token,"",5, TimeUnit.MINUTES);
    }

    public Boolean isTokenBlacklisted(String token){
        return Boolean.TRUE.equals(redisTemplate.hasKey(token));
    }
}
