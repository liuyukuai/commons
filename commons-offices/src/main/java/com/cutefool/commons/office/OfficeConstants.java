/*
 *  
 */
package com.cutefool.commons.office;

import com.cutefool.commons.expression.Expressions;
import org.apache.commons.lang3.StringUtils;

/**
 * @author 271007729@qq.com
 * @date 8/2/21 1:17 PM
 */
public class OfficeConstants {

    /**
     *
     */
    public static final String TABLE_PREFIX = "[";

    /**
     *
     */
    public static final String TABLE_SUFFIX = "]";

    /**
     * 多行前缀
     */
    public static final String MULTIPLE_ROWS_PREFIX = "[rows*";

    /**
     * 多列前缀
     */
    public static final String MULTIPLE_COLS_PREFIX = "[cols*";


    /**
     * 多列前缀
     */
    public static final String INDEX_PREFIX = "_index";

    /**
     *
     */
    public static final String TABLE_IMAGE_PREFIX = "{@";

    /**
     *
     */
    public static final String TABLE_REMOVE_PREFIX = "<remove*";

    /**
     *
     */
    public static final String TABLE_IMAGE_LIST_PREFIX = "[@";


    public static boolean isPrefix(String text) {
        if (StringUtils.isBlank(text)) {
            return false;
        }
        return text.contains(TABLE_PREFIX)
                || text.contains(Expressions.PREFIX)
                || text.contains(MULTIPLE_ROWS_PREFIX)
                || text.contains(MULTIPLE_COLS_PREFIX)
                || text.contains(TABLE_IMAGE_PREFIX)
                || text.contains(TABLE_IMAGE_LIST_PREFIX)
                || text.contains(TABLE_REMOVE_PREFIX);
    }

    public static boolean isSuffix(String text) {
        if (StringUtils.isBlank(text)) {
            return false;
        }
        return text.contains(TABLE_SUFFIX)
                || text.contains(Expressions.SUFFIX);
    }
}
