package com.genesis.microservice.business.service;

import com.microservice.rest.vo.UserVo;

/**
 * Created by Aizhanglin on 2018/6/3.
 */
public interface UserService {
    void saveUser(UserVo userVo);

    UserVo findUserByToken(String token);

    UserVo findUserByOpenid(String openid);
}
