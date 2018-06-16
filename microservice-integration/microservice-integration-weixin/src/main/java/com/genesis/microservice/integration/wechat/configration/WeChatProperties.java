package com.genesis.microservice.integration.wechat.configration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by Aizhanglin on 2018/4/14.
 *
 */
@Data
@ConfigurationProperties(prefix = "wechat")
public class WeChatProperties {
    private String appid;
    private String secret;
    private String token;
    private String authorizeBackUrl;


}
