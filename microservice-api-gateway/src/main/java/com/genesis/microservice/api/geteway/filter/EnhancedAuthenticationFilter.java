package com.genesis.microservice.api.geteway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 使sso单点登录能够通过增强resourceServer后的sign验证
 * 从header(Authorization)中取到token值放入request parameter参数中
 * Created by Aizhanglin on 2017/12/5.
 */
public class EnhancedAuthenticationFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        //定在OAuth2TokenRelayFilter之后
        return 11;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        //OAuth2TokenRelayFilter通过header成功认证并获取token后
//        String headerAccessToken = (String) ctx.get("ACCESS_TOKEN");
//        if (StringUtils.hasText(headerAccessToken)){
//            Map<String,List<String>> qp = ctx.getRequestQueryParams();
//            if (qp==null){
//                qp=new HashMap<>();
//            }
//            qp.put("access_token",Arrays.asList(headerAccessToken));
//            ctx.setRequestQueryParams(qp);
//            return null;
//        }
        //header中并没获取到有关token，尝试从参数中获取
        String accessToken = ctx.getRequest().getParameter("access_token");
        if (StringUtils.hasText(accessToken)){
            ctx.addZuulRequestHeader("authorization", ctx.get("TOKEN_TYPE") + " " + accessToken);
        }
        return null;
    }


}
