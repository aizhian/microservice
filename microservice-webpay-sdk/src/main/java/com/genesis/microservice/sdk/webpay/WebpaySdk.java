package com.genesis.microservice.sdk.webpay;

import com.microservice.rest.vo.ErrorCode;
import com.microservice.rest.vo.Result;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by Aizhanglin on 2017/5/4.
 */
@FeignClient(name = "microservice-webpay",fallbackFactory = WebpaySdk.HystrixClientFallbackFactory.class)
public interface WebpaySdk {

    @RequestMapping("/webpay/weixinPay")
    Result weixinPay(@RequestParam("orderCode") String orderCode);

    @Slf4j
    class HystrixClientFallback implements WebpaySdk{

        @Override
        public Result weixinPay(String orderCode) {
            log.error("订单号："+orderCode+"异常");
            Result result=new Result(ErrorCode.FAILE,null,"订单号："+orderCode+"异常");
            return result;
        }
    }
    @Slf4j
    @Component
    class HystrixClientFallbackFactory implements FallbackFactory<WebpaySdk> {
        @Override
        public WebpaySdk create(Throwable cause) {
            log.error(cause.getMessage());
            return new HystrixClientFallback();
        }
    }
}
