/*
 *  
 */
package com.cutefool.commons.core;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * between
 *
 * @author 271007729@qq.com
 * @date 9/12/21 2:35 AM
 */
@Data
@NoArgsConstructor
@SuppressWarnings("unused")
public class DurationLibs<T, E> {

    private T before;

    private T after;

    private E name;

    public DurationLibs(T before, T after) {
        this.before = before;
        this.after = after;
    }

    public DurationLibs(T before, T after, E name) {
        this.before = before;
        this.after = after;
        this.name = name;
    }
}
