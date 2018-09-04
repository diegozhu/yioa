package com.xseed.xoauth.service;

import com.xseed.xoauth.domain.AccessTokenResponse;
import com.xseed.xoauth.util.OauthConst;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;

/**
 * Created by tao on 2017-05-22.
 */
@Service
public class OauthService {


    @Value("${xseed.oauth.yixin.openid_path}")
    private  String tokenPath;


    @Autowired
    private RestTemplate restTemplate;

    public AccessTokenResponse accessYiXin(String code){
        //获取accessToken
        String tagUrl = StringUtils.replaceAll(tokenPath,"CODE",code);
        AccessTokenResponse accessTokenResponse = restTemplate.getForObject(tagUrl, AccessTokenResponse.class);


        return accessTokenResponse;




    }

}
