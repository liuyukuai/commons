/*
 *
 */
package com.cutefool.commons.orm.interfaces;

import com.cutefool.commons.orm.BasicsDomain;
import com.cutefool.commons.core.util.Lists;
import com.cutefool.commons.expression.Conditioning;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author 271007729@qq.com
 * @date 11/10/21 5:38 PM
 */
@SuppressWarnings({"unused", "unchecked"})
public interface IdWorkerOperations<DTO, E extends BasicsDomain, ID extends Long, R, Q extends Conditioning> extends IOperations<DTO, E, ID, R>, ISpecificationExecutor<E, Q> {

    /**
     * 通过id获取某个属性的值
     *
     * @param ids             ids
     * @param valueClassifier 属性函数
     * @param <V>             值类型
     * @return 值
     */
    default <V> Map<ID, V> fieldBy(List<ID> ids, Function<E, V> valueClassifier) {
        if (Lists.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        Objects.requireNonNull(valueClassifier);
        return Lists.empty(this.getById(ids)).stream().collect(Collectors.toMap(e -> (ID) e.getId(), valueClassifier));
    }
}
