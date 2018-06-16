package com.microservice.rest.vo;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

/**
 * Created by Aizhanglin on 2017/8/22.
 *
 */
@Data
@Entity
public class Goods {
    @Id
    private Long id;
    private String name;
    private Menu menu;
    private BigDecimal price;

}
