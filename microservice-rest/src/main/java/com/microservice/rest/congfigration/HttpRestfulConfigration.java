package com.microservice.rest.congfigration;

import ch.qos.logback.classic.pattern.MessageConverter;
import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Aizhanglin on 2018/6/2.
 */
@Configuration
@EnableConfigurationProperties(HttpConnPoolProperties.class)
public class HttpRestfulConfigration {
    HttpConnPoolProperties httpConnPoolProperties;
    @Bean
    public HttpClient httpClient(){
        HttpClientBuilder httpClientBuilder=HttpClientBuilder.create();
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(httpConnPoolProperties.getMaxTotal());
        connectionManager.setDefaultMaxPerRoute(httpConnPoolProperties.getDefaultMaxPerRoute());
//        通过路由控制微信api访问量
//        HttpHost weChatApi=new HttpHost("api.wechat.qq.com",80);
//        connectionManager.setMaxPerRoute(new HttpRoute(weChatApi),150);

        httpClientBuilder.setConnectionManager(connectionManager);

        DefaultHttpRequestRetryHandler retryHandler = new DefaultHttpRequestRetryHandler(2, true);
        httpClientBuilder.setRetryHandler(retryHandler);

        List<BasicHeader> basicHeaders=new ArrayList<>();
        basicHeaders.add(new BasicHeader("Content-Type","text/html;charset=UTF-8"));
        basicHeaders.add(new BasicHeader("Accept-Encoding","gzip,deflate"));
        basicHeaders.add(new BasicHeader("Accept-Language","zh-CN"));
        httpClientBuilder.setDefaultHeaders(basicHeaders);
        return httpClientBuilder.build();
    }
    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory(HttpClient httpClient){
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        httpRequestFactory.setConnectTimeout(httpConnPoolProperties.getConnectTimeout());
        httpRequestFactory.setReadTimeout(httpConnPoolProperties.getReadTimeout());
        return httpRequestFactory;
    }

    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory clientHttpRequestFactory){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        restTemplate.setRequestFactory(clientHttpRequestFactory);
        return restTemplate;
    }

}
