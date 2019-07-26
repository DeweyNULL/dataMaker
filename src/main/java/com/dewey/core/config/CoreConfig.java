package com.dewey.core.config;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 * @program dataMaker
 * @description: 一些组件
 * @author: xielinzhi
 * @create: 2019/07/26 14:39
 */

@Configuration
public class CoreConfig {

    @Value("${insertServiceThreadSize:10}")
    int threadSize;

    @Bean(destroyMethod = "shutdown")
    public ThreadPoolExecutor insertThreadPool() {
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat("insert-service-thread")
                .build();
        return new ThreadPoolExecutor(threadSize,threadSize,
                0L, TimeUnit.MILLISECONDS,new LinkedBlockingQueue<Runnable>(),threadFactory) ;
    }
}
