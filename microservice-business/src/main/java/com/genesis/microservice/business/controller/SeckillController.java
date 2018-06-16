package com.genesis.microservice.business.controller;

import com.genesis.microservice.business.mapper.SeckillMapper;
import com.genesis.microservice.business.redis.SeckillRepository;
import com.genesis.microservice.business.service.SeckillService;
import com.microservice.rest.vo.Seckill;
import com.microservice.rest.vo.SeckillPlayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Aizhanglin on 2017/8/18.
 * 活动限时秒杀
 */
@RestController
public class SeckillController {
    @Autowired
    SeckillService seckillService;

    @Autowired
    SeckillMapper seckillMapper;
    @Autowired
    SeckillRepository seckillRepository;

    @PostMapping("/saveSeckill")
    public void saveSeckill(Seckill seckill){
        seckillService.saveSeckill(seckill);
    }

    @PostMapping("/addGoodsToSeckill")
    public void addGoodsToSeckill(SeckillPlayer seckillPlayer){
        seckillService.addGoodsToSeckill(seckillPlayer );
    }


}
