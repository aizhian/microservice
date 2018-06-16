package com.genesis.microservice.integration.wechat.api.user.entity;

import lombok.Data;

/**
 * Created by Aizhanglin on 2018/6/3.
 */
@Data
public class AccessTokenReq {
    private String appid;
    private String secret;
    private String code;
}
