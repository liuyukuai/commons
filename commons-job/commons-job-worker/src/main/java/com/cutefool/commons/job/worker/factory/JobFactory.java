/*
 *
 */
package com.cutefool.commons.job.worker.factory;

import com.cutefool.commons.job.libs.JobsExecutor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 271007729@qq.com
 * @date 9/6/21 3:35 PM
 */
public class JobFactory {

    private static Map<String, JobsExecutor> EXECUTORS = new ConcurrentHashMap<>();

    public static void put(String name, JobsExecutor jobsExecutor) {
        EXECUTORS.put(name, jobsExecutor);
    }

    public static void remove(String name) {
        EXECUTORS.remove(name);
    }

    public static JobsExecutor get(String name) {
        return EXECUTORS.get(name);
    }
}
