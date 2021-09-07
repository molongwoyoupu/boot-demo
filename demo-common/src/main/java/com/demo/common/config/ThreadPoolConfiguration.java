package com.demo.common.config;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池配置
 *
 * @author molong
 * @date 2021/9/6
 */
@Configuration
public class ThreadPoolConfiguration {

    @Bean(name = "executorService", destroyMethod = "shutdown")
    public ExecutorService executorService() {
        // 线程池维护线程所允许的空闲时间
        int keepAliveSeconds = 120;
        // 队列最大长度
        int queueCapacity = 200;
        // 最大可创建的线程数
        int maxPoolSize = 150;
        // 核心线程池大小
        int corePoolSize = 35;
        return new ThreadPoolExecutor(corePoolSize,
                maxPoolSize,
                keepAliveSeconds,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(queueCapacity),
                ThreadFactoryBuilder.create().setNamePrefix("executor-").setDaemon(true).build(),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }
}
