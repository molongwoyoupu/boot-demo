package com.demo.config;

import com.demo.common.config.BaseRedisConfig;
import com.demo.common.service.RedisService;
import com.demo.common.service.impl.RedisServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自定义redis配置
 *
 * @author molong
 * @date 2021/9/6
 */
@Configuration
public class RedisCustomConfig extends BaseRedisConfig {

    @Bean
    public RedisService redisService(){
        return new RedisServiceImpl();
    }

}

