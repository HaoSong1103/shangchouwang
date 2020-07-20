package com.atguigu.crowd.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

/**
 * @author 宋浩
 * @version 1.0
 * @Classname RedisTest
 * @Description TODO
 * @Date 2020/7/14 21:33
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {
    private Logger logger = LoggerFactory.getLogger(RedisTest.class);
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Test
    public void testSet(){
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        operations.set("apple", "red1");
    }
    @Test
    public void testSetNew(){
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        operations.set("ping", "pong", 5, TimeUnit.MINUTES);
    }
}
