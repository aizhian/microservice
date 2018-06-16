package com.genesis.microservice.integration.authresource.annotation;

import com.genesis.microservice.integration.authresource.configration.ResourceServerConfigration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Created by Aizhanglin on 2017/12/5.
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({ResourceServerConfigration.class})
public @interface EnableNormalResourceServer {

}
