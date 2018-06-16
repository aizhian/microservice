package com.genesis.microservice.sdk.configuration;

import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Aizhanglin on 2017/5/25.
 *
 */
@Configuration
@EnableCircuitBreaker
@EnableFeignClients(basePackages = {"com.genesis.microservice.sdk"})//启动spring对feign声明式的web service客户端的注解支持
@ComponentScan(basePackages = {"com.genesis.microservice.sdk"})
public class WebpayAutoConfiguration {
}
