package com.genesis.microservice.integration.authserver.configration;

import com.genesis.microservice.integration.authserver.domain.User;
import com.genesis.microservice.integration.authserver.security.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.sql.DataSource;
import java.security.KeyPair;
import java.security.Principal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aizhanglin on 2017/10/16.
 */
@Configuration
@AutoConfigureAfter(DataSourceConfigration.class)
@EnableResourceServer
@SessionAttributes("authorizationRequest")
public class AuthServerConfigration {
    @RestController
    public class LogController {
        @Autowired
        UserDetailService userDetailService;
//        private final String insertUserSql="insert users(username,tel,email,password,enabled,account_non_expired,credentials_non_expired,account_non_locked) values (?,?,?,?,?,?,?,?)";
        @RequestMapping("/user")
        @ResponseBody
        public Principal user(Principal user) {
            return user;
        }

        @RequestMapping("/regist")
        @ResponseBody
        public String user(User user) {
            userDetailService.registUser(user);
            return "success";
        }
    }

    @Configuration
    @EnableAuthorizationServer
    public class OAuth2Config extends AuthorizationServerConfigurerAdapter {
        public class CustomTokenEnhancer implements TokenEnhancer {
            @Override
            public OAuth2AccessToken enhance(
                    OAuth2AccessToken accessToken,
                    OAuth2Authentication authentication) {
                //用户点击同意访问的时候触发，在json中添加自定义信息后加密成token
                Map<String, Object> additionalInfo = new HashMap<>();
                additionalInfo.put("organization", authentication.getName());
                ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
                return accessToken;
            }
        }
        @Autowired
        private AuthenticationManager authenticationManager;

        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
            TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
            tokenEnhancerChain.setTokenEnhancers(
                    Arrays.asList(tokenEnhancer(), jwtAccessTokenConverter()));

            endpoints.tokenStore(tokenStore())
                    .tokenEnhancer(tokenEnhancerChain)
                    .authenticationManager(authenticationManager);
        }

        @Bean
        public TokenEnhancer tokenEnhancer() {
            return new CustomTokenEnhancer();
        }

        @Override
        public void configure(AuthorizationServerSecurityConfigurer oauthServer)
                throws Exception {
            oauthServer
                    .tokenKeyAccess("permitAll()")
                    .checkTokenAccess("isAuthenticated()");
        }

        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//            clients.inMemory()
//                    .withClient("ui1")
//                    .secret("ui1-secret")
//                    .authorities("ROLE_STEVE")
//                    .authorizedGrantTypes("authorization_code", "refresh_token")
//                    .scopes("ui1.read")
//                    .autoApprove(false);
            clients.jdbc(dataSource)
                    .withClient("ui1")
                    .authorizedGrantTypes("implicit")
                    .scopes("read")
                    .autoApprove(true)
                    .and()
                    .withClient("ui2")
                    .secret("secret")
                    .authorizedGrantTypes("password", "authorization_code", "refresh_token")
                    .scopes("read", "write")
                    .autoApprove(true);
        }

        @Autowired
        private DataSource dataSource;

        //        @Bean
//        public JdbcTokenStore tokenStore() {
//            return new JdbcTokenStore(dataSource);
//        }
        @Bean
        public TokenStore tokenStore() {
            return new JwtTokenStore(jwtAccessTokenConverter());
        }


        @Bean
        public JwtAccessTokenConverter jwtAccessTokenConverter() {
            JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
            KeyPair keyPair = new KeyStoreKeyFactory(
                    new ClassPathResource("keystore.jks"), "keystore".toCharArray())
                    .getKeyPair("security","security".toCharArray());
            converter.setKeyPair(keyPair);
            return converter;
        }

        @Bean
        @Primary
        public DefaultTokenServices tokenServices() {
            DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
            defaultTokenServices.setTokenStore(tokenStore());
            defaultTokenServices.setSupportRefreshToken(true);
            return defaultTokenServices;
        }
    }

    @Configuration
    @EnableWebSecurity
    @Order(-20)
    public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
        @Autowired
        private DataSource dataSource;

        @Bean
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }

        @Override
        public void configure(WebSecurity web) throws Exception {
            //设置忽略验证的资源
            web.ignoring().antMatchers("/regist");
        }

        @Override
        public void configure(AuthenticationManagerBuilder auth) throws Exception {
//            auth.inMemoryAuthentication()
//                    .withUser("steve").password("steve").roles("STEVE")
//                    .and()
//                    .withUser("admin").password("admin").roles("ADMIN");
            auth.authenticationProvider(authenticationProvider());

        }

        @Bean
        public org.springframework.security.crypto.password.PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Bean
        public AuthenticationProvider authenticationProvider() {
            DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
            authenticationProvider.setUserDetailsService(userDetailsService());
            authenticationProvider.setPasswordEncoder(passwordEncoder());
//            authenticationProvider.setSaltSource(saltSource());
//            authenticationProvider.setPreAuthenticationChecks();
            return authenticationProvider;
        }

//        @Bean
//        public SaltSource saltSource(){
//            ReflectionSaltSource reflectionSaltSource = new ReflectionSaltSource();
//            reflectionSaltSource.setUserPropertyToUse("salt");
//            return reflectionSaltSource;
//        }

        @Bean
        public UserDetailsService userDetailsService() {
            UserDetailService userDetailService = new UserDetailService();
            userDetailService.setEnableGroups(true);
            userDetailService.setRolePrefix("ROLE_");
            userDetailService.setDataSource(dataSource);
            return userDetailService;
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.formLogin().permitAll()
                    .and()
                    .requestMatchers().antMatchers("/login", "/oauth/authorize", "/oauth/confirm_access")
                    .and()
                    .authorizeRequests().anyRequest().authenticated()
                    .and()
                    .exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler());

        }
    }
}
