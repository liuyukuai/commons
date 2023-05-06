/*
 *
 */
package com.cutefool.commons.concurrency;

import java.io.Serializable;
import java.util.List;

/**
 * @author 271007729@qq.com
 * @date 8/31/21 5:28 PM
 */
@FunctionalInterface
public interface ConsumerExecutor<T extends Serializable> {

    /**
     * 处理数据
     *
     * @param id  id
     * @param t   t
     */
    void consumer(Long id, List<T> t);
}
