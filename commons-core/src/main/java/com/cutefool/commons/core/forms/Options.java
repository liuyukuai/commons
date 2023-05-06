/*
 *
 */
package com.cutefool.commons.core.forms;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @author 271007729@qq.com
 * @date 2020-09-17 14:55
 */
@Data
public class Options {
    private String value;
    private boolean required;
    private boolean disabled;
    private boolean readonly;
    private boolean clearable;
    private String dataType;
    private String placeholder;
    private List<String> options;
    private String regEx;
    private int maxlength;
    private boolean asFields;
    private boolean asQueryTerms;
    private boolean addition;
    @JsonProperty(value = "isCustomOptions")
    private boolean customOptions;
    @JsonProperty(value = "isMultiple")
    private boolean multiple;
    private String source;
    private int initNum;

    private Object tree;
    private boolean hasUnit;
    private String unit;
    private String decimalCount;
    @JsonProperty(value = "isRange")
    private boolean isRange;
    private String primaryKey;
    private boolean intermediate;
    private boolean product;
    private boolean material;
    private boolean feedstock;
}
