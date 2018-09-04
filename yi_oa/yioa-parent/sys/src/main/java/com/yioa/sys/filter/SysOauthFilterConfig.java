package com.yioa.sys.filter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * oauth的认证，初略看了下易信和微信都是oauth2.0的标准，实现上差不多，细节暂时不比较了。
 *
 * Created by tao on 2017-04-29.
 */
@Configuration
public class SysOauthFilterConfig {

    @Value("${yioa.sys.bindUserUrl}")
    private String bindUserUrl ;

    @Value("${yioa.sys.whiteListUrl}")
    private String whiteListUrl ;

    @Bean
    public FilterRegistrationBean getSysOauthFilter() {

        //init
        SysOauthFilter sysOauthFilter = new SysOauthFilter();
        sysOauthFilter.setBindUserUrl(this.bindUserUrl);
        String[] whiteListUrlArr = StringUtils.split(whiteListUrl,";");
        sysOauthFilter.setWhiteListUrlArr(whiteListUrlArr);

        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setName("sysOauthFilter");
        registrationBean.setFilter(sysOauthFilter);

        List<String> urlPatterns = new ArrayList<String>();
        urlPatterns.add("/api/*");//拦截路径，可以添加多个
        urlPatterns.add("/car/*");
        urlPatterns.add("/oa/*");
        urlPatterns.add("/sys/*");

        registrationBean.setUrlPatterns(urlPatterns);
        registrationBean.setOrder(3);
        return registrationBean;
    }


}
