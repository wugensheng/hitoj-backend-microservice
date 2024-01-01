package com.wgs.hitojjudgeservice;

import com.wgs.hitojjudgeservice.rabbitmq.InitRabbitMq;
import io.swagger.models.auth.In;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ComponentScan("com.wgs")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = ("com.wgs.serviceclientservice.service"))
public class HitojBackendJudgeServiceApplication {
//    InitRabbitMq.doInit();
    public static void main(String[] args) {
        SpringApplication.run(HitojBackendJudgeServiceApplication.class, args);
    }

}
