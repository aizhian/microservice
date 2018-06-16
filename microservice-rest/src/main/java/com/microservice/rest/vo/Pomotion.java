package com.microservice.rest.vo;

import lombok.Data;

import java.util.Date;

/**
 * Created by Aizhanglin on 2017/12/6.
 */
@Data
public class Pomotion {
    private Long id;
    private String name;
    private Date beginTime;
    private Date endTime;
    private Integer bound;
    private Integer userLevel;
}
