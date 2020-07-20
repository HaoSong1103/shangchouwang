package com.atguigu.crowd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author 宋浩
 * @version 1.0
 * @Classname CrowdMainClass
 * @Description TODO
 * @Date 2020/7/13 21:53
 */
@EnableEurekaServer
@SpringBootApplication
public class CrowdMainClass {
    public static void main(String[] args) {
        SpringApplication.run(CrowdMainClass.class, args);
    }
}
