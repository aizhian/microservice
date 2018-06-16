package com.genesis.microservice.api.geteway.configuration;

import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Aizhanglin on 2017/5/25.
 */
@Configuration
@EnableCircuitBreaker//熔断配置
@EnableZuulProxy//路由配置
public class GetWayConfiguration {

}
