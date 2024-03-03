package com.example.thymeleaf.project;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Connection;

public class RedisTest {
    public static void main(String[] args) {
        try {
            Jedis jedis = new Jedis("redis://admin:Temmyre6x$@redis-12397.c281.us-east-1-2.ec2.cloud.redislabs.com:12397");
            Connection connection = jedis.getConnection();
            System.out.println("Connected to Redis");
        }catch(Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }
}
