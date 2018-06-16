package com.genesis.microservice.integration.mybatis.configration;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tk.mybatis.spring.mapper.MapperScannerConfigurer;

import java.util.Properties;

/**
 * Created by Aizhanglin on 2017/9/15.
 */
@Configuration
@AutoConfigureAfter(MybatisAutoConfigration.class)
public class TkMybatisMapperScanConfigration{
        @Bean
        public MapperScannerConfigurer mapperScannerConfigurer() {
            MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
            mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
            mapperScannerConfigurer.setBasePackage("com.genesis");
            mapperScannerConfigurer.setAnnotationClass(Mapper.class);
            Properties properties = new Properties();//设置通用mapper的属性
            properties.setProperty("notEmpty", "false");
            properties.setProperty("IDENTITY", "MYSQL");//主键生成策略
            properties.setProperty("ORDER", "AFTER");
            mapperScannerConfigurer.setProperties(properties);
            return mapperScannerConfigurer;
        }
}
