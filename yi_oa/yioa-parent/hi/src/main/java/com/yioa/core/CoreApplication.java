package com.yioa.core;

//import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by tao on 2017-05-06.
 */
@EnableScheduling
@EnableAutoConfiguration
@SpringBootApplication
@MapperScan({"com.yioa.*.*.mapper","com.yioa.*.mapper"})
@ComponentScan({"com.xseed","com.yioa"})
@EnableAsync
public class CoreApplication {

    private final static Logger logger = LoggerFactory.getLogger(CoreApplication.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx =  SpringApplication.run(CoreApplication.class, args);
        logger.debug(ctx.toString());
//        System.out.println("Let's inspect the beans provided by Spring Boot:");
//        String[] arr = ctx.getBeanDefinitionNames();
//        System.out.println(StringUtils.arrayToCommaDelimitedString(arr));

        logger.info("###############################################################################################################");
        logger.info("####  ");
        logger.info("####  ");
        logger.info("####  ");
        logger.info("#### yi_oa start sucess!!!!!!!!");
        logger.info("####  ");
        logger.info("####  ");
        logger.info("#### ");
        logger.info("###############################################################################################################");
    }
}
