package com.genesis.microservice.business.controller;

import com.genesis.microservice.business.mapper.TestMapper;
import com.genesis.microservice.common.idGenerate.IdWorker;
import com.microservice.rest.vo.Seckill;
import com.microservice.rest.vo.Test;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Aizhanglin on 2017/11/27.
 */
@RestController
public class TestController {
    @Autowired
    TestMapper testMapper;
    @Autowired
    IdWorker idWorker;
    @GetMapping("/test/t1")
    public Seckill test1(){
        Seckill seckill=new Seckill();
        return seckill;
    }

    @ApiOperation(value = "business.test.t2")
//    @PreAuthorize("permitAll")
    @GetMapping("/t2")
    public Test test2(Test test){
//        long id = idWorker.nextId();
//        test.setId(id);
        testMapper.insert(test);
        Test test1=testMapper.selectByPrimaryKey(test.getId());
        return test1;
    }

    @GetMapping("/t3")
    public Test test3(Test test){
        Test test1 = testMapper.selectByid(test.getId());
        return test1;
    }
}
