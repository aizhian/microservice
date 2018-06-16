package com.genesis.microservice.business.configuration;

import com.genesis.microservice.business.exception.ExceptionHandler;
//import com.genesis.microservice.integration.authresource.annotation.EnableNormalResourceServer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by Aizhanglin on 2017/4/21.
 * 项目配置
 *
 */
@Configuration
@EnableDiscoveryClient
//@EnableNormalResourceServer
public class BusinessConfiguration extends WebMvcConfigurerAdapter {

    /**
     * 异常处理器
     * @return
     */
    @Bean
    public ExceptionHandler exceptionHandler(){
        return new ExceptionHandler();
    }
}
