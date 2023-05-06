/*
 *
 */
package com.cutefool.commons.core.forms;

import lombok.Data;

/**
 * 属性对象
 *
 * @author 271007729@qq.com
 * @date 2020-09-17 00:18
 */
@Data
public class Fields {
    private Long id;
    private String label;
    private String type;
    private String typeName;
    private String code;
    private Options options;
    private String key;
    private String name;
    private String component;
    private String renderCom;
    private Object value;
    private String description;
    private Object children;
}
