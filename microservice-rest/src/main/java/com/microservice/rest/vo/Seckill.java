package com.microservice.rest.vo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by Aizhanglin on 2017/8/18.
 */
@Getter
@Setter
@Entity
@RedisHash
public class Seckill{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Date startTime;
    private Date endTime;
    private Integer moq;//最低起订数量
}
