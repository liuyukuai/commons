package com.cutefool.commons.core.thread;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池
 *
 * @author 271007729@qq.com
 * @date 2019-07-16 04:33
 */
@SuppressWarnings("unused")
public class ThreadPoolFactory {

    private static ExecutorService executorService;

    public static ExecutorService newInstance(String name) {
        if (executorService == null) {
            synchronized (ThreadPoolFactory.class) {
                if (executorService == null) {
                    BasicThreadFactory factory = new BasicThreadFactory.Builder()
                            .namingPattern(name + "-%d")
                            .build();

                    return executorService = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), 200, 100, TimeUnit.MINUTES, new LinkedBlockingQueue<>(), factory);
                }
            }
        }
        return executorService;
    }
}
