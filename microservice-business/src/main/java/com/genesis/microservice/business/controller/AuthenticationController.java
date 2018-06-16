package com.genesis.microservice.business.controller;

import com.genesis.microservice.business.service.AuthenticationService;
import com.genesis.microservice.integration.wechat.api.user.entity.AccessTokenRes;
import com.microservice.rest.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Aizhanglin on 2018/1/15.
 */
@RestController
public class AuthenticationController {
    @Autowired
    AuthenticationService authenticationService;

    @GetMapping("/wechat/helloworld")
    public String foo(){
        return "hello world";
    }
    /**
     *
     * @param signature 微信加密签名
     * @param echostr 随机字符串
     * @param timestamp 时间戳
     * @param nonce 随机数
     * @return
     */
    @GetMapping("/wechat/validateWebsite")
    public String validateWebsite(@RequestParam String signature,@RequestParam String echostr,@RequestParam String timestamp,@RequestParam String nonce){
        return authenticationService.validateWebsite(signature,echostr,timestamp,nonce);
    }

    @GetMapping("/wechat/authorize")
    public void authorize(){
        authenticationService.authorize();
    }

    @GetMapping("/wechat/login")
    public void login(String code){
        AccessTokenRes accessTokenRes = authenticationService.accessToken(code);
        authenticationService.login(accessTokenRes.getAccessToken(), accessTokenRes.getOpenid());

    }
}
