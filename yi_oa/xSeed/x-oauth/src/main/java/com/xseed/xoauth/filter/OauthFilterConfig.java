package com.xseed.xoauth.filter;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * oauth的认证，初略看了下易信和微信都是oauth2.0的标准，实现上差不多，细节暂时不比较了。
 *
 * Created by tao on 2017-04-29.
 */
@Configuration
public class OauthFilterConfig {

//    @Value("${xseed.sms.serverUrl}")
//    private String oauthloginUrl;
//
//    @Value("${xseed.sms.serverUrl}")
//    private String serverUrl;

    @Value("${xseed.oauth.sys.sys_token_path}")
    private String sysTokenpath ;

    @Value("${xseed.oauth.yixin.code_path}")
    private String codePath ;

    @Value("${xseed.oauth.sys.whiteListUrl}")
    private String whiteListUrl ;

    @Bean
    public FilterRegistrationBean getOauthFilter() {

        //init
        OauthFilter oauthFilter = new OauthFilter();
        oauthFilter.setSysTokenPath(sysTokenpath);
        oauthFilter.setCodePath(codePath);
        String[] whiteListUrlArr = StringUtils.split(whiteListUrl,";");
        oauthFilter.setWhiteListUrlArr(whiteListUrlArr);


        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setName("oauthfilter");
        registrationBean.setFilter(oauthFilter);

        List<String> urlPatterns = new ArrayList<String>();
        urlPatterns.add("/api/*");//拦截路径，可以添加多个
        urlPatterns.add("/car/*");
        urlPatterns.add("/oa/*");
        urlPatterns.add("/sys/*");


        //拦截路径，可以添加多个
//        List<String> urlPatterns = ImmutableList.of("/api/*", "/car/*", "/oa/*");

        //debug
//        List<String> urlPatterns = ImmutableList.of("/apix/*");
        registrationBean.setUrlPatterns(urlPatterns);
        registrationBean.setOrder(2);
        return registrationBean;
    }


}
