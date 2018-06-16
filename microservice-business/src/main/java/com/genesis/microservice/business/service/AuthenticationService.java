package com.genesis.microservice.business.service;

import com.genesis.microservice.integration.wechat.api.user.entity.AccessTokenRes;

/**
 * Created by Aizhanglin on 2018/6/3.
 */
public interface AuthenticationService {
    String validateWebsite(String signature, String echostr, String timestamp, String nonce);

    void authorize();

    AccessTokenRes accessToken(String code);

    void login(String accessToken, String openid);
}
