/*
 *
 */
package com.cutefool.commons.concurrency;

import com.cutefool.commons.core.util.Lists;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

/**
 * @author 271007729@qq.com
 * @date 8/31/21 5:28 PM
 */
@Slf4j
@SuppressWarnings("ALL")
public class ConcurrentExecutors<T extends Serializable> extends Thread {

    private Long id;


    private final boolean repeat;

    private Map<String, Object> keyMaps;


    @Setter
    private ConsumerExecutor<T> consumerExecutor;

    private LinkedBlockingQueue<List<T>> queue;

    ConcurrentExecutors(Long id, boolean isRepeat) {
        super("consumer-" + id);
        this.id = id;
        this.repeat = isRepeat;
        this.keyMaps = new ConcurrentHashMap<>(1024);
        this.queue = new LinkedBlockingQueue<>();
    }

    private boolean retain(T e) {
        // 如果不需要去重
        if (!this.repeat || Objects.isNull(this.keyMaps)) {
            return true;
        }

        if (e instanceof Repeatable) {
            String keys = ((Repeatable) e).keys();
            Object values = ((Repeatable) e).values();
            Object o = keyMaps.get(keys);
            // 如果值相同，说明不需要保留该数据，已经消费过了
            if (Objects.equals(o, ((Repeatable) e).values())) {
                log.info("keys = {} values = {} isRepeat = {}", keys, values, true);
                return false;
            }
            log.info("keys = {} values = {} isRepeat = {}", keys, values, false);
            // 如果不相同，说明值有变化，需要保留该值
            keyMaps.put(keys, values);
            return true;
        }
        return true;
    }

    void put(List<T> serializable) {
        try {
            queue.add(serializable);
            log.info("id = {} size = {} message = {}", id, queue.size());
        } catch (Exception e) {
            log.error("id = {} size = {} message = {}", id, queue.size(), e.getMessage());
        }
    }

    @Override
    public void run() {
        for (; ; ) {
            try {
                List<T> poll = queue.take();
                List<T> entities = Lists.empty(poll).stream().filter(this::retain).collect(Collectors.toList());
                if (Lists.iterable(entities) && Objects.nonNull(consumerExecutor)) {
                    consumerExecutor.consumer(this.id, entities);
                }
            } catch (Throwable e) {
                log.error(e.getMessage(), e);
            }
        }
    }
}
