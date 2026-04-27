package com.lucky.common.web.config;

import com.lucky.common.core.utils.spring.SpringUtils;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.VirtualThreadTaskExecutor;

import java.util.concurrent.*;

/**
 * 线程池配置
 *
 * @author lucky
 **/
@Slf4j
@AutoConfiguration
public class ThreadPoolConfig {

    // CPU 密集型，可设置为 CPU 核心数 + 1
    // IO 密集型，可设置为 CPU 核心数 × 2
    // 核心线程池大小(初始化为CPU核心数)
    private final int corePoolSize = Runtime.getRuntime().availableProcessors() + 1;

    private ScheduledExecutorService scheduledExecutorService;

    /**
     * 执行周期性或定时任务
     */
    @Bean(name = "scheduledExecutorService")
    protected ScheduledExecutorService scheduledExecutorService() {
        BasicThreadFactory.Builder builder = new BasicThreadFactory.Builder().daemon(true);
        if (SpringUtils.isVirtual()) {
            builder.namingPattern("virtual-schedule-pool-%d").wrappedFactory(new VirtualThreadTaskExecutor().getVirtualThreadFactory());
        } else {
            builder.namingPattern("schedule-pool-%d");
        }
        // 拒绝策略：调用者运行策略，不再交由线程池处理
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(corePoolSize,
                builder.build(),
                new ThreadPoolExecutor.CallerRunsPolicy()) {
            @Override
            protected void afterExecute(Runnable r, Throwable t) {
                super.afterExecute(r, t);
                printException(r, t);
            }
        };
        this.scheduledExecutorService = scheduledThreadPoolExecutor;
        return scheduledThreadPoolExecutor;
    }

    /**
     * 销毁时关闭线程池
     */
    @PreDestroy
    public void destroy() {
        try {
            log.info("====关闭后台任务任务线程池====");
            ScheduledExecutorService pool = scheduledExecutorService;
            if (pool != null && !pool.isShutdown()) {
                pool.shutdown();
                try {
                    if (!pool.awaitTermination(120, TimeUnit.SECONDS)) {
                        pool.shutdownNow();
                        if (!pool.awaitTermination(120, TimeUnit.SECONDS)) {
                            log.info("Pool did not terminate");
                        }
                    }
                } catch (InterruptedException ie) {
                    pool.shutdownNow();
                    Thread.currentThread().interrupt();
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 打印线程异常信息
     */
    public static void printException(Runnable r, Throwable t) {
        if (t == null && r instanceof Future<?>) {
            try {
                Future<?> future = (Future<?>) r;
                if (future.isDone()) {
                    future.get();
                }
            } catch (CancellationException ce) {
                t = ce;
            } catch (ExecutionException ee) {
                t = ee.getCause();
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }
        if (t != null) {
            log.error(t.getMessage(), t);
        }
    }

}
