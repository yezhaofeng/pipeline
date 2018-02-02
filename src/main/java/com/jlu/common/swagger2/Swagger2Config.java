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
                .title("Pipeline流水线API列表")
                .termsOfServiceUrl("https://github.com/z521598/pipeline_v2")
                .description(
                        new String(PipelineConfigReader.getConfigValueByKey("swagger.description").getBytes("ISO-8859-1"), "UTF-8"))
                .contact(new Contact("langshiquan", PipelineConfigReader.getConfigValueByKey("pipeline.home"),
                        "576506402@qq.com"))
                .build();

    }
}
