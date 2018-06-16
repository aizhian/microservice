package com.genesis.microservice.business.service.impl;

import com.genesis.microservice.business.service.AuthenticationService;
import com.genesis.microservice.business.service.UserService;
import com.genesis.microservice.integration.wechat.Decript;
import com.genesis.microservice.integration.wechat.api.AuthorizeApi;
import com.genesis.microservice.integration.wechat.api.user.entity.AccessTokenRes;
import com.genesis.microservice.integration.wechat.api.user.entity.WechatUserInfo;
import com.genesis.microservice.integration.wechat.configration.WeChatProperties;
import com.genesis.microservice.business.repository.AccessTokenRepository;
import com.microservice.rest.vo.UserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.UUID;

/**
 * Created by Aizhanglin on 2018/6/3.
 */
@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    @Autowired
    WeChatProperties weChatProperties;
    @Autowired
    AuthorizeApi authorizeApi;
    @Autowired
    UserService userService;
    @Autowired
    AccessTokenRepository accessTokenRepository;

    @Override
    public String validateWebsite(String signature, String echostr, String timestamp, String nonce) {

        String[] str = {weChatProperties.getToken(), timestamp, nonce};
        Arrays.sort(str); // 字典序排序
        String bigStr = str[0] + str[1] + str[2];
        //加密
        String sha1Signature = Decript.SHA1(bigStr);
        //校验签名
        if (StringUtils.hasText(sha1Signature) && sha1Signature.equals(signature)) {
            //签名校验通过
            return echostr;
        } else {
            //签名校验失败
            return null;
        }
    }

    @Override
    public void authorize() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = servletRequestAttributes.getResponse();
        StringBuilder url = new StringBuilder("https://open.weixin.qq.com/connect/oauth2/authorize");
        url.append("?appid=").append(weChatProperties.getAppid());
        String encodeBackUrl = null;
        try {
            encodeBackUrl = URLEncoder.encode(weChatProperties.getAuthorizeBackUrl(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        url.append("&redirect_uri=").append(encodeBackUrl);
        url.append("&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect");
        try {
            response.sendRedirect(url.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public AccessTokenRes accessToken(String code) {
        AccessTokenRes accessTokenRes = authorizeApi.getAccessToken(weChatProperties.getAppid(), weChatProperties.getSecret(), code);
        accessTokenRepository.save(accessTokenRes);
        return accessTokenRes;
    }

    @Override
    public void login(String accessToken, String openid) {
        UserVo userVo=userService.findUserByOpenid(openid);
        if (userVo==null){
            userVo=new UserVo();
            WechatUserInfo userInfo = authorizeApi.getUserInfo(accessToken, openid);
            BeanUtils.copyProperties(userInfo,userVo);
            String userToken = UUID.randomUUID().toString();
            userVo.setToken(userToken);
            userService.saveUser(userVo);
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletResponse response = servletRequestAttributes.getResponse();
            try {
                response.sendRedirect("/user/fixUserInfo.html?token="+userToken);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
