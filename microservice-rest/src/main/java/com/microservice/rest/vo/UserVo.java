package com.microservice.rest.vo;

import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;

/**
 * Created by Aizhanglin on 2017/8/22.
 */
@Data
@RedisHash("user:token")
public class UserVo {
    @Id
    private String token;
    private Long id;

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
