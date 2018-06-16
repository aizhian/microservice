package com.genesis.microservice.business.repository;

import com.microservice.rest.vo.UserVo;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Aizhanglin on 2018/6/3.
 */
public interface UserVoRepository extends CrudRepository<UserVo,String>{
}
