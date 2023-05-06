/*
 *
 */
package com.cutefool.commons.api;

import lombok.Data;

/**
 * @author 271007729@qq.com
 * @date 2019/9/25 5:16 PM
 */
@Data
@SuppressWarnings("unused")
public class ApiQuery {
    private String name;
    private String type;
    private String example;
    private String desc;
    private String required;

    public ApiQuery() {
    }

    public ApiQuery(String name, String example, String desc, String required) {
        this.name = name;
        this.example = example;
        this.desc = desc;
        this.required = required;
    }

    public ApiQuery(String name, String type, String example, String desc, String required) {
        this.name = name;
        this.type = type;
        this.example = example;
        this.desc = desc;
        this.required = required;
    }
}
