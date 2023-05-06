package com.cutefool.commons.mybatis.dsl;

import com.cutefool.commons.core.util.Maps;
import com.cutefool.commons.core.util.Strings;
import com.cutefool.commons.orm.Extensible;
import com.cutefool.commons.orm.interfaces.IOperations;
import org.mybatis.dynamic.sql.insert.InsertDSL;
import org.mybatis.dynamic.sql.insert.MultiRowInsertDSL;
import org.mybatis.dynamic.sql.update.UpdateDSL;
import org.mybatis.dynamic.sql.update.UpdateModel;

import java.util.Map;

public interface MybatisExtensible<D, E, I, R extends MybatisMapper<E, I>> extends IOperations<D, E, I, R> {

    /**
     * 处理额外的动态属性
     *
     * @param insertDsl     dsl
     * @param extensibleMap 属性值集合
     */
    default void doExtensible(InsertDSL<E> insertDsl, Map<String, String> extensibleMap) {
        if (Maps.iterable(extensibleMap)) {
            extensibleMap.forEach((k, v) -> insertDsl.map(this.getRepository().sqlTable().column(v)).toProperty(Extensible.FIELD_EXTENSIBLE + "." + k));
        }
    }


    /**
     * 处理额外的动态属性
     *
     * @param insertDsl     dsl
     * @param extensibleMap 属性值集合
     */
    default void doExtensible(MultiRowInsertDSL<E> insertDsl, Map<String, String> extensibleMap) {
        if (Maps.iterable(extensibleMap)) {
            extensibleMap.forEach((k, v) -> insertDsl.map(this.getRepository().sqlTable().column(v)).toProperty(Extensible.FIELD_EXTENSIBLE + "." + k));
        }
    }

    /**
     * 处理额外的动态属性
     *
     * @param insertDsl     dsl
     * @param extensibleMap 属性值集合
     */
    default void doExtensiblePresent(InsertDSL<E> insertDsl, Map<String, String> extensibleMap) {
        if (Maps.iterable(extensibleMap)) {
            extensibleMap.forEach((k, v) -> {
                if (Strings.isNotNull(v)) {
                    insertDsl.map(this.getRepository().sqlTable().column(v)).toProperty(Extensible.FIELD_EXTENSIBLE + "." + k);
                }
            });
        }
    }


    /**
     * 处理额外的动态属性
     *
     * @param insertDsl     dsl
     * @param extensibleMap 属性值集合
     */
    default void doExtensiblePresent(MultiRowInsertDSL<E> insertDsl, Map<String, String> extensibleMap) {
        if (Maps.iterable(extensibleMap)) {
            extensibleMap.forEach((k, v) -> {
                if (Strings.isNotNull(v)) {
                    insertDsl.map(this.getRepository().sqlTable().column(v)).toProperty(Extensible.FIELD_EXTENSIBLE + "." + k);
                }
            });
        }
    }

    /**
     * 处理额外的动态属性
     *
     * @param e             e
     * @param dsl           dsl
     * @param extensibleMap 属性值集合
     */
    default void  doExtensible(E e, UpdateDSL<UpdateModel> dsl, Map<String, String> extensibleMap) {
        if (Maps.iterable(extensibleMap)) {
            if (e instanceof Extensible) {
                Map<String, Object> extensibleValue = ((Extensible) e).getExtensible();
                if (Maps.iterable(extensibleValue)) {
                    extensibleMap.forEach((k, v) -> dsl.set(this.getRepository().sqlTable().column(v)).equalTo(extensibleValue.get(k)));
                }
            }
        }
    }

    /**
     * 处理额外的动态属性
     *
     * @param e             e
     * @param dsl           dsl
     * @param extensibleMap 属性值集合
     */
    default void doExtensiblePresent(E e, UpdateDSL<UpdateModel> dsl, Map<String, String> extensibleMap) {
        if (Maps.iterable(extensibleMap)) {
            if (e instanceof Extensible) {
                Map<String, Object> extensibleValue = ((Extensible) e).getExtensible();
                if (Maps.iterable(extensibleValue)) {
                    extensibleMap.forEach((k, v) -> dsl.set(this.getRepository().sqlTable().column(v)).equalToWhenPresent(extensibleValue.get(k)));
                }
            }
        }
    }

}
