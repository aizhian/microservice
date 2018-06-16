package com.genesis.microservice.business.service;

import com.microservice.rest.vo.Seckill;
import com.microservice.rest.vo.SeckillPlayer;

/**
 * Created by Aizhanglin on 2017/8/18.
 */
public interface SeckillService {
    void saveSeckill(Seckill seckill);

    Seckill querySeckillById(Long seckillId);

    void addGoodsToSeckill(SeckillPlayer seckillPlayer);
}
