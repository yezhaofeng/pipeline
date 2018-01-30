package com.jlu.common.swagger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.jlu.common.utils.PipelineReadConfig;
import com.mangofactory.swagger.configuration.SpringSwaggerConfig;
import com.mangofactory.swagger.models.dto.ApiInfo;
import com.mangofactory.swagger.plugin.EnableSwagger;
import com.mangofactory.swagger.plugin.SwaggerSpringMvcPlugin;

import java.io.UnsupportedEncodingException;

/**
 * Created by xiaohui on 2016/1/14.
 */
@Configuration
@EnableWebMvc
@EnableSwagger
@ComponentScan(basePackages = {"com.jlu"})
public class SwaggerConfig {

    @Autowired
    private SpringSwaggerConfig springSwaggerConfig;

    @Bean
    public SwaggerSpringMvcPlugin customImplementation() throws UnsupportedEncodingException {
        return new SwaggerSpringMvcPlugin(this.springSwaggerConfig)
                .apiInfo(apiInfo())
                .includePatterns(".*?");
    }

    private ApiInfo apiInfo() throws UnsupportedEncodingException {
        ApiInfo apiInfo = new ApiInfo(
                "Pipeline流水线API列表",
                new String(PipelineReadConfig.getConfigValueByKey("swagger.description").getBytes("ISO-8859-1"), "UTF-8"),
                PipelineReadConfig.getConfigValueByKey("pipeline.home"),
                "576506402@qq.com",
                "Apache2.0",
                "https://github.com/z521598/pipeline_v2");
        return apiInfo;
    }
}
