package com.xseed.boot.common.cfg;



import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;

/**
 * Created by tao on 2017-05-22.
 */
@Configuration
public class CommonPropertyCfg {
    @Bean
    public PropertyPlaceholderConfigurer getPropertyPlaceholderConfigurer()
            throws IOException {
        PropertyPlaceholderConfigurer ppc = new PropertyPlaceholderConfigurer();
        ppc.setLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:/config/*.properties"));
        return ppc;
    }
}
