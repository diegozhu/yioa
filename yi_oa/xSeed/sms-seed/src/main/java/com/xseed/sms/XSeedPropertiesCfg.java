package com.xseed.sms;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;


/**
 * 主工程已经已经有必要的配置了，运行时这里不再需要
 * 但是对于sms单独启动的情况，还是有必要的
 *
 * Created by tao on 2017-05-04.
 */
@Configuration
public class XSeedPropertiesCfg {
    @Bean
    public PropertyPlaceholderConfigurer getPropertyPlaceholderConfigurer()
            throws IOException {
        PropertyPlaceholderConfigurer ppc = new PropertyPlaceholderConfigurer();
        ppc.setLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:/config/*.properties"));
        return ppc;
    }
}
