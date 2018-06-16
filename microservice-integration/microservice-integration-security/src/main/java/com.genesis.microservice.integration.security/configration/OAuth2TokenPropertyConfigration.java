package com.genesis.microservice.integration.security.configration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by Aizhanglin on 2017/9/25.
 */
@Data
@ConfigurationProperties(prefix = "security.oauth2.client.token")
public class OAuth2TokenPropertyConfigration {
    private String keystoreClassPath;
    private String keystorePassword;
    private String alias;
    private String aliasPassword;
}
