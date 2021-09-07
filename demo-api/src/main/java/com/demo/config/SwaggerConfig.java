package com.demo.config;

import com.demo.common.config.BaseSwaggerConfig;
import com.demo.common.domain.SwaggerProperties;
import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * swagger文档配置
 *
 * @author molong
 * @date 2021/9/6
 */
@Configuration
@EnableSwagger2
@EnableKnife4j
@Import(BeanValidatorPluginsConfiguration.class) //引入jsr303
public class SwaggerConfig extends BaseSwaggerConfig {

    @Value("${swagger.host}")
    private String host;

    @Override
    public SwaggerProperties swaggerProperties() {
        return SwaggerProperties.builder()
                .apiBasePackage("com.demo")
                .title("demo例子API")
                .description("demo例子API文档")
                .contactName("molong")
                .version("v1.0.0")
                .enableSecurity(true)
                .host(host)
                .groupName("v1.0.0")
                .build();
    }
}

