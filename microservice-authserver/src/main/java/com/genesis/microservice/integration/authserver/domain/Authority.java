package com.genesis.microservice.integration.authserver.domain;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

/**
 * Created by Aizhanglin on 2017/10/24.
 */
@Data
public class Authority implements GrantedAuthority{
    private String Authority;
    @Override
    public String getAuthority() {
        return null;
    }
}
