package com.genesis.microservice.integration.authresource.configration;

import com.genesis.microservice.integration.authresource.interceptor.SignInterceptor;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.sql.DataSource;

/**
 * Created by Aizhanglin on 2017/10/17.
 *
 */
@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class IntensiveResourceServerConfigration extends GlobalMethodSecurityConfiguration {
        @Configuration
        public class MyWebAppConfigurer extends WebMvcConfigurerAdapter {

            @Bean(destroyMethod = "close")
            @ConfigurationProperties(prefix = "spring.datasource")
            public DataSource dataSource() {
                return DataSourceBuilder.create().build();
            }

            @Bean
            public JdbcTemplate jdbcTemplate(){
                return new JdbcTemplate(dataSource());
            }

            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                //增加token访问验证拦截器，加强请求url的验证并且将token访问限制到method级别
                registry.addInterceptor(new SignInterceptor(jdbcTemplate())).addPathPatterns("/**");
                super.addInterceptors(registry);
            }
        }

}
