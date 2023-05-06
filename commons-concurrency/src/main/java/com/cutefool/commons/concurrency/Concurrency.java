/*
 *
 */
package com.cutefool.commons.concurrency;

import com.cutefool.commons.core.GroupId;
import com.cutefool.commons.core.util.Lists;
import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

/**
 * @author 271007729@qq.com
 * @date 8/31/21 4:46 PM
 */
@Slf4j
@SuppressWarnings({"WeakerAccess", "unused"})
public class Concurrency<T extends Serializable> implements Closeable {

    private final Long num;


    private final CountDownLatch countDownLatch;

    private final Map<Long, ConcurrentExecutors<T>> executorMap = new ConcurrentHashMap<>();


    public Concurrency(Long num, boolean isRepeat) {
        if (Objects.isNull(num) || num <= 0) {
            num = 1L;
        }
        if (num >= Long.MAX_VALUE) {
            num = Long.MAX_VALUE;
        }

        this.num = num;
        this.countDownLatch = new CountDownLatch(num.intValue());

        for (long i = 0; i < num; i++) {
            // 创建线程
            ConcurrentExecutors<T> concurrentExecutors = new ConcurrentExecutors<>(i, isRepeat);
            executorMap.put(i, concurrentExecutors);
        }
    }

    /**
     * 启动线程
     *
     * @param consumerExecutor consumerExecutor
     */
    public void startup(ConsumerExecutor<T> consumerExecutor) {
        executorMap.forEach((k, v) -> {
            v.setConsumerExecutor(consumerExecutor);
            v.start();
        });
    }

    public void put(T serializable) {

        // 如果数据为空，忽略
        if (Objects.isNull(serializable)) {
            return;
        }

        if (executorMap.isEmpty()) {
            throw new RuntimeException("concurrency is closed.");
        }
        // 如果不重复
        long idx = this.idx(serializable);
        ConcurrentExecutors<T> concurrentExecutors = executorMap.get(idx);
        if (Objects.nonNull(concurrentExecutors)) {
            concurrentExecutors.put(Collections.singletonList(serializable));
        }
    }


    public void put(List<T> serializable) {
        // 如果数据为空，忽略
        if (Lists.isEmpty(serializable)) {
            return;
        }

        // 分组
        serializable.parallelStream()
                .collect(Collectors.groupingBy(this::idx))
                .forEach((k, v) -> {
                    ConcurrentExecutors<T> concurrentExecutors = executorMap.get(k);
                    if (Objects.nonNull(concurrentExecutors)) {
                        concurrentExecutors.put(v);
                    }
                });
    }

    private long idx(T serializable) {
        // 如果支持分组
        if (serializable instanceof GroupId) {
            GroupId groupId = (GroupId) serializable;
            return groupId.group(this.num);
        }
        return Math.abs(serializable.hashCode() % this.num);
    }

    /**
     * 判断任务是否全部结束
     *
     * @return true or false
     */
    public boolean isDown() {
        long count = this.countDownLatch.getCount();
        return count == 0;
    }

    /**
     * 判断任务是否全部结束
     */
    public void isDown(ConcurrencyInterface concurrencyInterface) {
        try {
            this.countDownLatch.await();
            this.close();
            concurrencyInterface.finished();
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }


    @Override
    public void close() {
        // 停止所有线程
        executorMap.forEach((key, value) -> value.interrupt());
        executorMap.clear();
    }
}
