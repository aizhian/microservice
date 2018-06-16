package com.genesis.microservice.business.context;

import com.microservice.rest.vo.UserVo;

/**
 * Created by Aizhanglin on 2017/8/22.
 */
public class ApplicationContext {
    public static UserVo currentUser(){
        throw new RuntimeException("暂不支持的方法");
    }
}
