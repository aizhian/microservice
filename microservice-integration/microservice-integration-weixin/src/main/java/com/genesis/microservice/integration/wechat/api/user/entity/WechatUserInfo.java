package com.genesis.microservice.integration.wechat.api.user.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

/**
 * Created by Aizhanglin on 2018/6/3.
 *
 */
@Data
@RedisHash("wechat:userInfo")
public class WechatUserInfo {
    @Id
    private String openid;
    private String nickname;
    private String sex;
    private String province;
    private String city;
    private String country;
    private String headimgurl;
    private String privilege;
    private String unionid;
}
