package com.example.thymeleaf.project;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Connection;

public class RedisTest {
    public static void main(String[] args) {
        String url = "redis://admin:"+ System.getenv("REDIS_PASSWORD")+"@redis-12397.c281.us-east-1-2.ec2.cloud.redislabs.com:12397";
        try {
            Jedis jedis = new Jedis(url);
            Connection connection = jedis.getConnection();
            System.out.println(System.getenv("JAVA_HOME"));
            System.out.println("Connected to Redis");
        }catch(Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }
}