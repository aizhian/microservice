package com.genesis.microservice.business.pojo;

import lombok.Data;

/**
 * Created by Aizhanglin on 2018/6/3.
 */
@Data
public class User {
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
