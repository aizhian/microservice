package com.microservice.rest.vo;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by Aizhanglin on 2017/8/22.
 *
 */
@Data
@Entity
public class Menu {
    @Id
    private Long id;
    private String position;
    private String name;
    private Integer level;
    private String link;
    private Integer type;
}
