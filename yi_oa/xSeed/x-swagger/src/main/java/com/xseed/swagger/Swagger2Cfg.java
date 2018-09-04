package com.xseed.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by tao on 2017-05-17.
 */
@Configuration
@EnableSwagger2
public class Swagger2Cfg {

    @Bean
    public Docket xseedRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(xseedApiInfo())
                .groupName("2 xseed")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.xseed"))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName("1 yioa")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.yioa"))
//                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo xseedApiInfo() {
        return new ApiInfoBuilder()
                .title("xseed rest apis")
                .description("show xseed rest apis")
                .termsOfServiceUrl("")
                .contact("whiletrue yioa")
                .version("1.0")
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("rest apis")
                .description("show rest apis")
                .termsOfServiceUrl("")
                .contact("whiletrue yioa")
                .version("1.0")
                .build();
    }
}
