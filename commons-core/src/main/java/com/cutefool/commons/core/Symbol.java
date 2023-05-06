/*
 *
 */
package com.cutefool.commons.core;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * 符号
 *
 * @author 271007729@qq.com
 * @date 2022/7/21 1:00 AM
 */
@SuppressWarnings("unused")
public enum Symbol {

    /**
     * 加法
     */
    ADD("+", "加法"), SUB("-", "减法"), MULTI("*", "乘法"), DIVIDE("/", "除法"), PREVIOUS("pre[", "前值");

    private final String key;

    private final String name;

    Symbol(java.lang.String key, java.lang.String name) {
        this.key = key;
        this.name = name;
    }

    public static boolean isSymbol(String symbol) {
        if (StringUtils.isBlank(symbol)) {
            return false;
        }
        boolean isSymbol = Stream.of(Symbol.values()).anyMatch(e -> Objects.equals(symbol, e.key));
        if (isSymbol) {
            return true;
        }
        return symbol.startsWith(PREVIOUS.key);
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }
}
