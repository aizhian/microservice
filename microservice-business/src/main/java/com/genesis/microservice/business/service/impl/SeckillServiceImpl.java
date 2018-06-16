package com.genesis.microservice.business.service.impl;

import com.genesis.microservice.business.mapper.SeckillMapper;
import com.genesis.microservice.business.service.SeckillService;
import com.microservice.rest.vo.Seckill;
import com.microservice.rest.vo.SeckillPlayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

/**
 * Created by Aizhanglin on 2017/8/18.
 * 秒杀
 */
@Service
public class SeckillServiceImpl implements SeckillService {
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    SeckillMapper seckillMapper;

    /**
     * 发布秒杀活动
     * @param seckill
     */
    @Override
    public void saveSeckill(Seckill seckill){

        seckillMapper.insert(seckill);
    }

    @Override
    public Seckill querySeckillById(Long seckillId){
        return seckillMapper.selectByPrimaryKey(seckillId);
    }

    private String generateKey(Long id){
        return "seckill:"+id;
    }
    /**
     * 商品参与秒杀活动
     * @param seckillPlayer
     */
    @Override
    public void addGoodsToSeckill( SeckillPlayer seckillPlayer){
        Seckill seckill = querySeckillById(seckillPlayer.getSeckillId());
        Long id =seckill.getId();
        double score = generateScore(seckill, seckillPlayer);
        ZSetOperations zSetOperations = redisTemplate.opsForZSet();
        zSetOperations.add(generateKey(id),seckillPlayer,score);
    }

    /**
     * 生成商品评分
     * 评分因子：添加时间 数量
     * @param seckill
     * @param seckillPlayer
     * @return
     */
    private double generateScore(Seckill seckill, SeckillPlayer seckillPlayer){
        long factor = (seckill.getEndTime().getTime()-seckill.getStartTime().getTime())/seckill.getMoq();//活动时间除以参与活动起始数量得到因数（每个商品价值时间）
        Long score = seckillPlayer.getNum().longValue() * factor + seckillPlayer.getJoinTime().getTime();//商品数量乘以因数factor得到上架数量的价值时间，再加上上架时间得到最终评分
        return score.doubleValue();
    }
}
