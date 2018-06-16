package com.genesis.microservice.integration.security.configration;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.ManagementServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.authserver.AuthorizationServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.authserver.OAuth2AuthorizationServerConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.KeyPair;
import java.security.Principal;

/**
 * Created by Aizhanglin on 2017/9/28.
 */
@Configuration
@EnableDiscoveryClient
public class OAuthServerConfigration extends WebMvcConfigurerAdapter {
    @RestController
    public class UserController {
        @GetMapping("/domain")
        public Principal user(Principal user){
            return user;
        }
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("admin").password("admin").roles("USER");
    }


    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/oauth/confirm_access").setViewName("authorize");
    }

    @Configuration
    @Order(ManagementServerProperties.ACCESS_OVERRIDE_ORDER)
    protected static class LoginConfig extends WebSecurityConfigurerAdapter {
        @Autowired
        private AuthenticationManager authenticationManager;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests()
                    .anyRequest().authenticated()
                    .and()
                    .csrf().csrfTokenRepository(csrfTokenRepository()).and()
                    .addFilterAfter(csrfHeaderFilter(), CsrfFilter.class)
                    .formLogin()
                    .loginPage("/login")
                    .failureUrl("/login?error")
                    .permitAll() //5
                    .and()
                    .logout().permitAll();
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.parentAuthenticationManager(authenticationManager);
        }

        private Filter csrfHeaderFilter() {
            return new OncePerRequestFilter() {
                @Override
                protected void doFilterInternal(HttpServletRequest request,
                                                HttpServletResponse response, FilterChain filterChain)
                        throws ServletException, IOException {
                    CsrfToken csrf = (CsrfToken) request
                            .getAttribute(CsrfToken.class.getName());
                    if (csrf != null) {
                        Cookie cookie = new Cookie("XSRF-TOKEN",
                                csrf.getToken());
                        cookie.setPath("/");
                        response.addCookie(cookie);
                    }
                    filterChain.doFilter(request, response);
                }
            };
        }

        private CsrfTokenRepository csrfTokenRepository() {
            HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
            repository.setHeaderName("X-XSRF-TOKEN");
            return repository;
        }
    }

    @Configuration
    @EnableAuthorizationServer
    @EnableConfigurationProperties(OAuth2TokenPropertyConfigration.class)
    protected static class OAuth2Config extends OAuth2AuthorizationServerConfiguration {
        @Autowired
        OAuth2TokenPropertyConfigration oAuth2TokenPropertyConfigration;
        @Autowired
        private AuthenticationManager authenticationManager;
        private final BaseClientDetails details;
        private final TokenStore tokenStore;
        public OAuth2Config(BaseClientDetails details, AuthenticationManager authenticationManager, ObjectProvider<TokenStore> tokenStoreProvider, AuthorizationServerProperties properties) {
            super(details, authenticationManager, tokenStoreProvider, properties);
            this.details=details;
            this.tokenStore = tokenStoreProvider.getIfAvailable();
        }

        private JwtAccessTokenConverter jwtAccessTokenConverter() {
            JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
            KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(
                    new ClassPathResource(oAuth2TokenPropertyConfigration.getKeystoreClassPath()), oAuth2TokenPropertyConfigration.getKeystorePassword().toCharArray());
            KeyPair keyPair;
            String alias = oAuth2TokenPropertyConfigration.getAlias();
            if (StringUtils.isEmpty(oAuth2TokenPropertyConfigration.getAliasPassword())){
                keyPair=keyStoreKeyFactory.getKeyPair(alias);
            }else {
                keyPair=keyStoreKeyFactory.getKeyPair(alias,oAuth2TokenPropertyConfigration.getAliasPassword().toCharArray());
            }
            converter.setKeyPair(keyPair);
            return converter;
        }

        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints)
                throws Exception {
            if(this.tokenStore != null) {
                endpoints.tokenStore(this.tokenStore);
            }

            if(this.details.getAuthorizedGrantTypes().contains("password")) {
                endpoints.authenticationManager(authenticationManager).accessTokenConverter(
                        jwtAccessTokenConverter());
            }

        }

    }
}
