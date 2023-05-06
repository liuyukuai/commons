/*
 *
 */
package com.cutefool.commons.core;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 下拉列表对象
 *
 * @author 271007729@qq.com
 * @date 2019/10/25 12:37 PM
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("unused")
public class Select<T> {

    /**
     * 名称
     */
    private String label;

    /**
     * 值
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private T value;

    public Select(Map.Entry<T, String> entry) {
        this.value = entry.getKey();
        this.label = entry.getValue();
    }
}
