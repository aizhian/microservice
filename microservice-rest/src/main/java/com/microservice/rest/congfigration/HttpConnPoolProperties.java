package com.microservice.rest.congfigration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by Aizhanglin on 2018/6/2.
 */
@Data
@ConfigurationProperties
public class HttpConnPoolProperties {
    @Value("${http.pool.maxTotal:400}")
    private Integer maxTotal;
    @Value("${http.pool.defaultMaxPerRoute:200}")
    private Integer defaultMaxPerRoute;
    @Value("${http.pool.connectTimeout:20000}")
    private Integer connectTimeout;
    @Value("${http.pool.readTimeout:20000}")
    private Integer readTimeout;
}
