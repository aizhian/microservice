package com.genesis.microservice.business.repository;

import com.genesis.microservice.integration.wechat.api.user.entity.AccessTokenRes;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Aizhanglin on 2018/6/3.
 */
public interface AccessTokenRepository extends CrudRepository<AccessTokenRes,String>{
}
