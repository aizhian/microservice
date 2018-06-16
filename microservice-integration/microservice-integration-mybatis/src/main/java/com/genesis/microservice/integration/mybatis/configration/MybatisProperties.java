package com.genesis.microservice.integration.mybatis.configration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Aizhanglin on 2017/8/31.
 */
@Data
@ConfigurationProperties(
        prefix = "spring.mybatis"
)
public class MybatisProperties {
    private String mapperLocations;
    private String typeAliasesPackage;
    private String mapperBasePackages;
    public MybatisProperties(){
    }
}
