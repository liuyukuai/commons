package com.cutefool.commons.mybatis;

import com.cutefool.commons.core.util.Lists;
import com.cutefool.commons.orm.BasicsDomain;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 数据接口类
 *
 * @author liuyk
 */

@SuppressWarnings("unused")
public interface IdWorkerService<DTO, E extends BasicsDomain, ID extends Long, Mapper extends IBaseMapper<E, ID, Example>, Example extends IExample<ID>, Query> extends IBaseSpecificationService<DTO, E, ID, Mapper, Example, Query> {

    /**
     * 通过id获取某个属性的值
     *
     * @param ids             ids
     * @param valueClassifier 属性函数
     * @param <V>             值类型
     * @return 值
     */
    default <V> Map<Long, V> fieldBy(List<ID> ids, Function<E, V> valueClassifier) {
        if (Lists.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        Objects.requireNonNull(valueClassifier);
        return Lists.empty(this.getById(ids)).stream().collect(Collectors.toMap(BasicsDomain::getId, valueClassifier));
    }

    /**
     * newExample
     *
     * @param id   id
     * @param name 参数
     * @return example
     */
    default Example newExample(Long id, String name) {
        throw new IllegalArgumentException(" please override this method. ");
    }

}
