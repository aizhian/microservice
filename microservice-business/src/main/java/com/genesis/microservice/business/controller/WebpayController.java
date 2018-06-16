package com.genesis.microservice.business.controller;

import com.genesis.microservice.sdk.webpay.WebpaySdk;
import com.microservice.rest.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Aizhanglin on 2017/5/4.
 */
@RestController
@RequestMapping("webpay")
public class WebpayController {
    @Autowired
    WebpaySdk webpaySdk;
    @RequestMapping("weixinPay")
    public Result weixinPay(String orderCode){
        Result result = webpaySdk.weixinPay(orderCode);
        return result;
    }
}
