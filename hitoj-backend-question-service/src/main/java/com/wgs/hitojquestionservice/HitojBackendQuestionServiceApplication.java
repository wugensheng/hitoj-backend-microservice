package com.wgs.hitojquestionservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.wgs.hitojquestionservice.mapper")
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ComponentScan("com.wgs")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = ("com.wgs.serviceclientservice.service"))
public class HitojBackendQuestionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(HitojBackendQuestionServiceApplication.class, args);
    }

}
