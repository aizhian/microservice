package com.genesis.microservice.webpay.controller;

import com.microservice.rest.vo.ErrorCode;
import com.microservice.rest.vo.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Aizhanglin on 2017/3/30.
 * 微信支付
 */
@RestController
@RequestMapping("webpay")
@RefreshScope
public class WebpayController {
    @Value("${testValue}")
    private String testValue;
    @GetMapping("/weixinPay")
    public Result weixinPay(){
        System.out.println("微型支付");
        return new Result(ErrorCode.SUCCESS,null,testValue);
    }

}