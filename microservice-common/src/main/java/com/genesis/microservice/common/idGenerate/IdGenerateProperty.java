package com.genesis.microservice.common.idGenerate;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by Aizhanglin on 2017/11/16.
 */
@ConfigurationProperties(
        prefix = "spring.server"
)
@Data
public class IdGenerateProperty {
    private Long workerId=0L;
    private Long datacenterId=0L;
}
