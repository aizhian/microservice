package com.genesis.microservice.integration.authresource.interceptor;


import io.swagger.annotations.ApiOperation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.Assert;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.security.InvalidParameterException;
import java.util.List;

/**
 * Created by Aizhanglin on 2017/11/29.
 */
public class SignInterceptor implements HandlerInterceptor {
    private final String clientSecretQuerySql="select client_secret from oauth_client_details where client_id=?";
    private final long timeInterval=6*60*60*1000;

    private JdbcTemplate jdbcTemplate;
    public SignInterceptor(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate=jdbcTemplate;
    }
    @Override
    public boolean preHandle(javax.servlet.http.HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {
        String accessToken=httpServletRequest.getParameter("access_token") ;
        String clientId = httpServletRequest.getParameter("client_id");
        String method = httpServletRequest.getParameter("method");
        String timestamp = httpServletRequest.getParameter("timestamp");
        String v = httpServletRequest.getParameter("v");
        String sign = httpServletRequest.getParameter("sign");
        Assert.hasText(accessToken, "access_token request parameter must not be empty or null");
        Assert.hasText(clientId, "client_id request parameter must not be empty or null");
        Assert.hasText(method, "method request parameter must not be empty or null");
        Assert.hasText(timestamp, "timestamp request parameter must not be empty or null");
        Assert.hasText(v, "v request parameter must not be empty or null");
        Assert.hasText(sign, "sign request parameter must not be empty or null");
        validateSign(sign,accessToken,clientId,method,timestamp,v,handler);
        return true;
    }

    /**
     * 自定义资源访问请求参数，加强请求url的验证并且将token访问限制到method级别
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(javax.servlet.http.HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {


    }

    private void validateSign(String sign,String accessToken,String clientId,String method,String timestamp,String v,Object handler){

        List<String> query = jdbcTemplate.query(clientSecretQuerySql, new Object[]{clientId}, (RowMapper) (rs, rowNum) -> {
            String clientSecret = rs.getString(1);
            return clientSecret;
        });

        if (query==null||query.size()==0){
            throw new InvalidParameterException("Invalid client_id: " + clientId);
        }
        String secret=query.get(0);
        StringBuilder sb=new StringBuilder();
        sb.append("access_token").append(accessToken)
                .append("client_id").append(clientId)
                .append("client_secret").append(secret)
                .append("method").append(method)
                .append("timestamp").append(timestamp)
                .append("v").append(v);
        String  md5sign= DigestUtils.md5DigestAsHex(sb.toString().getBytes());
        if (!md5sign.equals(sign)){
            throw new InvalidParameterException("Invalid sign request parameter: " + sign);
        }
        HandlerMethod handlerMethod= (HandlerMethod) handler;
        if (handlerMethod.hasMethodAnnotation(ApiOperation.class)){
            ApiOperation apiOperation = handlerMethod.getMethodAnnotation(ApiOperation.class);
            String methodName = apiOperation.value();
            if (!method.equals(methodName)){
                throw new InvalidParameterException("Permission denied,can not access method: " + method);
            }
        }else {
            throw new InvalidParameterException("Permission denied,has no method: " + method);
        }
        long signTimestamp;
        try {
            signTimestamp = Long.parseLong(timestamp);
        }catch (Exception e){
            throw new InvalidParameterException("Invalid timestamp request parameter:"+timestamp);
        }
        if (System.currentTimeMillis()-signTimestamp>timeInterval){
            throw new InvalidParameterException("sign request parameter is time out");
        }

    }

    @Override
    public void afterCompletion(javax.servlet.http.HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
