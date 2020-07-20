package com.atguigu.crowd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * @author 宋浩
 * @version 1.0
 * @Classname CrowdMainClass
 * @Description TODO
 * @Date 2020/7/14 23:09
 */
@EnableZuulProxy
@SpringBootApplication
public class CrowdMainClass {
    public static void main(String[] args) {
        SpringApplication.run(CrowdMainClass.class, args);
    }
}
