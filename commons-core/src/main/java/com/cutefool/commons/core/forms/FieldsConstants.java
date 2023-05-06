/*
 *
 */
package com.cutefool.commons.core.forms;

import com.cutefool.commons.core.util.Maps;
import com.cutefool.commons.core.util.Strings;

import java.util.Map;

/**
 * @author 271007729@qq.com
 * @date 2022/7/20 4:51 PM
 */
public class FieldsConstants {

    /**
     * 表单code
     */
    public static final String KEY_FORM_ITEM_CODE = "code";

    /**
     * 表单类型KEY
     */
    public static final String KEY_FORM_ITEM_TYPE = "type";

    /**
     * 表单类型code
     */
    public static final String KEY_FORM_ITEM_NAME = "name";

    /**
     * 表单类型code
     */
    public static final String KEY_FORM_ITEM_VALUE = "value";

    public static String getOptionType(Map<String, Object> settings) {
        return Maps.empty(settings).getOrDefault(KEY_FORM_ITEM_TYPE, Strings.EMPTY).toString();
    }

}
