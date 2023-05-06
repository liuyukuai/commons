/*
 *  
 */
package com.cutefool.commons.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;

import javax.annotation.Resource;
import java.util.concurrent.*;

/**
 * @author 271007729@qq.com
 * @date 2020/7/23 4:04 PM
 */
@Slf4j
@SuppressWarnings("unused")
public class KafkaService {

    private static ExecutorService executorService;

    static {
        int poolSize = Runtime.getRuntime().availableProcessors() * 2;
        executorService = new ThreadPoolExecutor(poolSize, poolSize,
                0, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(512),
                new ThreadPoolExecutor.DiscardPolicy());
    }

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    public void send(String topic, String message) {
        try {
            executorService.submit(() -> kafkaTemplate.send(topic, message).isDone());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
