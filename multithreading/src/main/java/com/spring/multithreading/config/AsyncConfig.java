package com.spring.multithreading.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2); //initilaize two threads
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("userThread-");
        executor.initialize();
        return executor;
    }
}
/*
ThreadPoolTaskExecutor Spring-specific implementation of Java's Executor framework.
It provides features like setting core pool size, max pool size, queue capacity, etc.,
for managing a pool of threads.
 */

/*
setCorePoolSize(int corePoolSize) sets the core number of threads in the thread pool.
 Core threads are always kept alive unless the thread pool is shut down.
 In this case, it initializes the thread pool with 2 threads.
 */

/*

setMaxPoolSize(int maxPoolSize) sets the maximum allowed number of threads
in the thread pool.
 This configuration ensures that the thread pool will not exceed 2 threads,
  even if more tasks are submitted.
 */

/*
setQueueCapacity(int queueCapacity) sets the capacity for the queue that holds tasks
 before they are executed.
 If there are more tasks than the core pool size,
 they are queued up to this capacity before additional threads are created up
 to maxPoolSize
 */

/*
setThreadNamePrefix(String threadNamePrefix) sets the prefix
to use for the names of newly created threads.
This can be useful for identifying threads belonging to specific pools or tasks
 */

/*
initialize() initializes the thread pool.
This method must be called to actually start using the configured thread pool settings.
 */

/*
return executor:
Finally, the method returns the configured ThreadPoolTaskExecutor instance (executor).
 This allows Spring to manage this bean and inject it where it's needed,
  typically for concurrent task execution within a Spring application context.
 */