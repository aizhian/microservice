package com.genesis.microservice.business.redis;

import com.microservice.rest.vo.Seckill;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Aizhanglin on 2017/9/11.
 *
 */
@Repository
public interface SeckillRepository extends CrudRepository<Seckill,Long>{
}
