package com.genesis.microservice.business.mapper;

import com.microservice.rest.vo.Test;
import tk.mybatis.mapper.common.Mapper;

/**
 * Created by Aizhanglin on 2017/11/27.
 */
@org.apache.ibatis.annotations.Mapper
public interface TestMapper extends Mapper<Test> {
    Test selectByid(Long id);
}
