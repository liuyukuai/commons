/*
 *
 */
package com.cutefool.commons.core;

import com.cutefool.commons.core.util.Lists;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 权限判断类
 *
 * @author 271007729@qq.com
 * @date 2019-09-09 11:20
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public final class Authorities {

    private static final Long INVALID = 0L;


    public static boolean isHave(long authority, Long value) {
        return (authority & value) == value;
    }

    /**
     * 通过选中的值计算权限值
     *
     * @param values 选中的值
     * @return 计算值
     */
    public static long calculate(List<Long> values) {
        return Optional.ofNullable(values)
                .filter(Lists::iterable)
                .map(e -> e.stream().reduce(INVALID, (x1, x2) -> x1 | x2))
                .orElse(INVALID);
    }

    /**
     * 查询有权限的值
     *
     * @param authority 当前权限
     * @param values    所有值
     * @return 有权限的值
     */
    public static List<Long> list(long authority, List<Long> values) {
        if (!Lists.iterable(values)) {
            return Collections.emptyList();
        }
        return values.stream().filter(v -> isHave(authority, v)).collect(Collectors.toList());
    }

    /**
     * 查询有权限的值
     *
     * @param authority 当前权限
     * @param values    所有值
     * @return 有权限的值
     */
    public static List<Long> or(long authority, List<Long> values) {
        if (!Lists.iterable(values)) {
            return Collections.emptyList();
        }
        // 最大值
        long sum = values.stream().mapToLong(e -> e).sum();


        List<Long> privileges = list(authority, values);

        if (!Lists.iterable(privileges)) {
            return Collections.emptyList();
        }
        List<Long> orList = new ArrayList<>();

        privileges.forEach(e -> {
            for (long i = 1; i <= sum; i++) {
                boolean have = isHave(i, e);
                if (have) {
                    orList.add(i);
                }
            }
        });
        return orList;
    }

    /**
     * 删除部分有权限的值
     *
     * @param authority 当前权限
     * @param values    所有值
     * @param removes   需要删除的值
     * @return 有权限的值
     */
    public static Long remove(long authority, List<Long> removes, List<Long> values) {
        // 如果删除的值为空
        if (!Lists.iterable(removes)) {
            return authority;
        }
        List<Long> privileges = list(authority, values);

        if (!Lists.iterable(privileges)) {
            return authority;
        }

        privileges.removeAll(removes);
        return calculate(privileges);
    }

    /**
     * 判断有效
     *
     * @param authority 需要判断的地址
     * @return true | false
     */
    public static boolean isValid(long authority) {
        return !isInvalid(authority);
    }

    /**
     * 判断无效
     *
     * @param authority 需要判断的地址
     * @return true | false
     */
    public static boolean isInvalid(long authority) {
        return Objects.equals(INVALID, authority);
    }
}
