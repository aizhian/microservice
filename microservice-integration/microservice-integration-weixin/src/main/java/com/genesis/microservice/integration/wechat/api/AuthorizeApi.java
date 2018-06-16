package com.genesis.microservice.integration.wechat.api;

import com.genesis.microservice.integration.wechat.api.user.entity.AccessTokenRes;
import com.genesis.microservice.integration.wechat.api.user.entity.WechatUserInfo;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Aizhanglin on 2017/5/4.
 */
@FeignClient(name = "wechat-api",url = "https://api.weixin.qq.com",fallbackFactory = AuthorizeApi.HystrixClientFallbackFactory.class)
public interface AuthorizeApi {

    @RequestMapping("/sns/oauth2/access_token?appid={appid}&secret={secret}&code={code}&grant_type=authorization_code")
    AccessTokenRes getAccessToken(@PathVariable("appid") String appid, @PathVariable("secret") String secret, @PathVariable("code") String code);

    @RequestMapping("/sns/userinfo?access_token={accessToken}&openid={openid}&lang=zh_CN")
    WechatUserInfo getUserInfo(@PathVariable("accessToken") String accessToken,@PathVariable("openid") String openid);


    @Slf4j
    class HystrixClientFallback implements AuthorizeApi {

        @Override
        public AccessTokenRes getAccessToken(String appid, String secret, String code) {
            log.error("请求access token失败");
            return null;
        }

        @Override
        public WechatUserInfo getUserInfo(@PathVariable("access_token") String accessToken, @PathVariable("openid") String openid) {
            log.error("请求用户信息失败,accessToken：%s,openid：s%",accessToken,openid);
            return null;
        }
    }
    @Slf4j
    @Component
    class HystrixClientFallbackFactory implements FallbackFactory<AuthorizeApi> {
        @Override
        public AuthorizeApi create(Throwable cause) {
            log.error(cause.getMessage());
            return new HystrixClientFallback();
        }
    }
}
