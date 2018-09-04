package com.xseed.xoauth;


import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.util.StringUtils;

/**
 * Created by tao on 2017-05-09.
 */
//@EnableAutoConfiguration
@SpringBootApplication
@ComponentScan({"com.xseed", "com.yioa"})
public class XOauthApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(XOauthApplication.class, args);
        System.out.println("Let's inspect the beans provided by Spring Boot:");

        String[] arr = ctx.getBeanDefinitionNames();
        System.out.println(StringUtils.arrayToCommaDelimitedString(arr));
    }

}
