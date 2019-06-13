package com.jlu.common.swagger2;

import java.io.UnsupportedEncodingException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.jlu.common.utils.PipelineConfigReader;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableWebMvc
@EnableSwagger2
public class Swagger2Config {

    public static final String DESCRIPTION = "使用方法：\n\n" +
            "在登录状态下：直接访问。\n\n" +
            "在非登录状态下：每次请求需要带上请求头\"pipeline-token\":\"token在首页右上角处[查看token]\"。\n" +
            "\n" +
            "注意：请妥善保管您的token，是您身份的唯一标识。\n\n" +
            "若您的token已经泄露，请在首页重置您的token，重置token接口仅允许在登录状态下访问，不允许通过API调用。";
    public static final String JSON_URL = "/v2/api-docs";
    public static final String UI_URL = "/swagger-resources";

    @Bean
    public Docket buildDocket() throws UnsupportedEncodingException {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(buildApiInf())
                .select().apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo buildApiInf() throws UnsupportedEncodingException {
        return new ApiInfoBuilder()
                .title("持续交付平台API列表")
                .termsOfServiceUrl("https://github.com/z521598/pipeline")
                .description(DESCRIPTION)
                .contact(new Contact("yezhaofeng", PipelineConfigReader.getConfigValueByKey("pipeline.home"),
                        "576506402@qq.com"))
                .build();

    }
}
