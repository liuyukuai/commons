/*
 *
 */
package com.cutefool.commons.core.forms;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 271007729@qq.com
 * @date 2022/7/22 9:53 AM
 */
@Data
@NoArgsConstructor
@SuppressWarnings("unused")
public class FieldsLibs {
    private String code;
    private String name;
    private String type;

    public FieldsLibs(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public FieldsLibs(String code, String name, String type) {
        this.code = code;
        this.name = name;
        this.type = type;
    }
}
