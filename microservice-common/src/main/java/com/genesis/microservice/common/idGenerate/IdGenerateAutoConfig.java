package com.genesis.microservice.common.idGenerate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Aizhanglin on 2017/11/22.
 */
@Configuration
@EnableConfigurationProperties(IdGenerateProperty.class)
public class IdGenerateAutoConfig {
    @Autowired
    IdGenerateProperty idGenerateProperty;
    @Bean
    public IdWorker idWorker(){
        IdWorker idWorker=new IdWorker(idGenerateProperty);
        return idWorker;
    }
}
