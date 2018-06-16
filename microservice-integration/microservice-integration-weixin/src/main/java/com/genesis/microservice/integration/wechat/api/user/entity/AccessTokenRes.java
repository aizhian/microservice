package com.genesis.microservice.integration.wechat.api.user.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

/**
 * Created by Aizhanglin on 2018/6/3.
 */
@Data
@RedisHash("wechat:oauth2:accessToken")
public class AccessTokenRes {
    @Id
    private String openid;
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("expires_in")
    private Double expriesIn;
    @JsonProperty("refresh_token")
    private String refreshToken;
    private String scope;
    private Integer errcode=0;
    private String errmsg;
}
