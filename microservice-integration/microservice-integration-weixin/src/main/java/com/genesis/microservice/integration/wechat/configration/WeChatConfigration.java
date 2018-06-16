package com.genesis.microservice.integration.wechat.configration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Aizhanglin on 2018/1/16.
 */

@Configuration
@ComponentScan(basePackages = {"com.genesis.microservice.integration.wechat"})
@EnableCircuitBreaker
@EnableFeignClients(basePackages = {"com.genesis.microservice.integration.wechat.api"})//启动spring对feign声明式的web service客户端的注解支持
@EnableConfigurationProperties({WeChatProperties.class})
public class WeChatConfigration {

}
