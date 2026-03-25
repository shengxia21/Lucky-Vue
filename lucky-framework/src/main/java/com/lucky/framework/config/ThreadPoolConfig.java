package com.lucky.framework.config;

import com.lucky.common.utils.Threads;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池配置
 *
 * @author lucky
 **/
@Configuration
public class ThreadPoolConfig {

    // 核心线程池大小(初始化为CPU核心数)
    private final int corePoolSize = Runtime.getRuntime().availableProcessors();

    // 最大可创建的线程数
    private final int maxPoolSize = 50;

    // 队列最大长度
    private final int queueCapacity = 100;

    // 线程池维护线程所允许的空闲时间
    private final int keepAliveSeconds = 200;

    // 线程池关闭等待任务时间
    private final int awaitTerminationSeconds = 120;

    @Bean(name = "threadPoolTaskExecutor")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // CPU 密集型，可设置为 CPU 核心数 + 1
        // IO 密集型，可设置为 CPU 核心数 × 2
        executor.setCorePoolSize(corePoolSize * 2);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setKeepAliveSeconds(keepAliveSeconds);
        executor.setThreadNamePrefix("thread-pool-");
        // 线程池关闭时等待所有任务完成
        executor.setWaitForTasksToCompleteOnShutdown(true);
        // 线程池关闭时，还存在任务正在执行，等待该时间后强制关闭
        executor.setAwaitTerminationSeconds(awaitTerminationSeconds);
        // 拒绝策略：任务队列已满且线程数已达到最大线程数时,直接由调用者运行策略，不再交由线程池处理
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 初始化线程池(创建核心线程,真正可被调用)
        executor.initialize();
        return executor;
    }

    /**
     * 执行周期性或定时任务
     */
    @Bean(name = "scheduledExecutorService")
    protected ScheduledExecutorService scheduledExecutorService() {
        // 拒绝策略：调用者运行策略，不再交由线程池处理
        return new ScheduledThreadPoolExecutor(corePoolSize * 2,
                new BasicThreadFactory.Builder().namingPattern("schedule-pool-%d").daemon(true).build(),
                new ThreadPoolExecutor.CallerRunsPolicy()) {
            @Override
            protected void afterExecute(Runnable r, Throwable t) {
                super.afterExecute(r, t);
                Threads.printException(r, t);
            }
        };
    }

}
