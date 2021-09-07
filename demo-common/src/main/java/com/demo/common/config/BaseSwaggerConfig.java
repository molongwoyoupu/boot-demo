package com.demo.common.config;

import com.demo.common.domain.SwaggerProperties;
import com.demo.common.enums.ResultCode;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * swagger文档基础配置
 *
 * @author molong
 * @date 2021/9/6
 */
public abstract class BaseSwaggerConfig {

    /**
     * 返回值描述
     * @return 返回值描述字符串
     */
    private String resultCodeDescription(){
        ResultCode[] values = ResultCode.values();
        StringBuilder builder = new StringBuilder();
        builder.append("<table>");
        builder.append("<tr>");
        builder.append("<td style='border: 2px inset; width:100px;'>值</td>");
        builder.append("<td style='border: 2px inset'>说明</td>");
        builder.append("</tr>");
        for (ResultCode value : values) {
            builder.append("<tr><td style='border: 2px inset'>");
            builder.append(value.getCode());
            builder.append("</td><td style='border: 2px inset'>");
            builder.append(value.getMessage());
            builder.append("</td></tr>");
        }
        builder.append("</table>");
        return builder.toString();
    }

    @Bean
    public Docket createRestApi() {
        SwaggerProperties swaggerProperties = swaggerProperties();
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                //在swagger 返回的model类型中，将model中LocalTime 替换为 String 进行显示
                .directModelSubstitute(LocalTime.class, String.class)
                .host(swaggerProperties.getHost())
                .apiInfo(apiInfo(swaggerProperties))
                //自定义参数类型
//                .ignoredParameterTypes(Pageable.class)
                .select()
                .apis(RequestHandlerSelectors.basePackage(swaggerProperties.getApiBasePackage()))
                .paths(PathSelectors.any())
                .build();
        if (swaggerProperties.isEnableSecurity()) {
            docket.securitySchemes(securitySchemes()).securityContexts(securityContexts());
        }
        if(StringUtils.hasText(swaggerProperties.getGroupName())){
            docket.groupName(swaggerProperties.getGroupName());
        }

        return docket;
    }

    /**
     * api文档实体
     * @param swaggerProperties 参数
     * @return api文档实体
     */
    private ApiInfo apiInfo(SwaggerProperties swaggerProperties) {
        //扩展属性
        String s = resultCodeDescription();

        return new ApiInfoBuilder()
                .title(swaggerProperties.getTitle())
                .description(swaggerProperties.getDescription() + "<br><h3>全局返回值说明<h3>" + s)
                .contact(new Contact(swaggerProperties.getContactName(), swaggerProperties.getContactUrl(), swaggerProperties.getContactEmail()))
                .version(swaggerProperties.getVersion())
                .build();
    }

    protected List<ApiKey> securitySchemes() {
        //设置请求头信息
        List<ApiKey> result = new ArrayList<>();
        ApiKey apiKey = new ApiKey("Authorization", "Authorization", "header");
        result.add(apiKey);
        return result;
    }

    /**
     * 安全上下文
     * @return 安全上下文集合
     */
    private List<SecurityContext> securityContexts() {
        //设置需要登录认证的路径
        List<SecurityContext> result = new ArrayList<>();
        //路径正则表达式
        String pathRegex="/*/.*";
        result.add(getContextByPath(pathRegex));
        return result;
    }

    /**
     * 获取安全上下文
     * @param pathRegex 路径正则表达式
     * @return 安全上下文
     */
    private SecurityContext getContextByPath(String pathRegex) {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex(pathRegex))
                .build();
    }

    /**
     * 默认身份验证
     * @return 安全验证
     */
    private List<SecurityReference> defaultAuth() {
        List<SecurityReference> result = new ArrayList<>();
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        result.add(new SecurityReference("Authorization", authorizationScopes));
        return result;
    }

    /**
     * 自定义Swagger配置
     * @return 返回swagger 配置对象
     */
    public abstract SwaggerProperties swaggerProperties();
}

