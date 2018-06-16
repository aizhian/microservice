package com.genesis.microservice.business.service.impl;

import com.genesis.microservice.business.repository.UserVoRepository;
import com.genesis.microservice.business.service.UserService;
import com.microservice.rest.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Aizhanglin on 2018/6/3.
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserVoRepository userVoRepository;

    @Override
    public void saveUser(UserVo userVo) {
        userVoRepository.save(userVo);
        //保存到数据库
    }

    @Override
    public UserVo findUserByToken(String token) {
        UserVo userVo = userVoRepository.findOne(token);
        return userVo;
    }

    @Override
    public UserVo findUserByOpenid(String openid) {
        return null;
    }
}
