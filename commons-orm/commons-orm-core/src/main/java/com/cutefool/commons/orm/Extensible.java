package com.cutefool.commons.orm;

import lombok.Data;
import com.cutefool.commons.core.ExcelIgnore;

import java.util.Map;

/**
 * 可扩展的对象
 */
@Data
public class Extensible {

    /**
     * 扩展属性名称
     */
    public static final String FIELD_EXTENSIBLE = "extensible";

    /**
     * 可扩展参数
     */
    @ExcelIgnore
    private Map<String, Object> extensible;
}
