package com.microservice.rest.vo;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by Aizhanglin on 2017/8/23.
 */
@Data
@Entity
public class SeckillPlayer {
    @Id
    private Long id;
    private Long seckillId;
    private Long goodsId;
    private Integer num;
    private Date joinTime;
}
