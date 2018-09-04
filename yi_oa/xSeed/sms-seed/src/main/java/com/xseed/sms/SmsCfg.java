package com.xseed.sms;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.util.concurrent.Executors;


/**
 * 主工程已经已经有必要的配置了，运行时这里不再需要
 * 但是对于sms单独启动的情况，还是有必要的
 * <p>
 * Created by tao on 2017-05-04.
 */
@Configuration
public class SmsCfg {


    @Bean
    public EventBus getEventBus()
            throws IOException {
        AsyncEventBus eventBus = new AsyncEventBus(Executors.newFixedThreadPool(10));
        return eventBus;
    }
}
