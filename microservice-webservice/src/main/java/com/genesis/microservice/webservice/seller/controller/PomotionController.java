package com.genesis.microservice.webservice.seller.controller;

import com.microservice.rest.vo.Pomotion;
import com.microservice.rest.vo.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Created by Aizhanglin on 2017/12/6.
 *
 */
@RestController
public class PomotionController {
    @GetMapping("/pomotion/addPomotion")
    public Result addPomotion(){
        Pomotion pomotion=new Pomotion();
        return Result.success(pomotion);
    }
}
