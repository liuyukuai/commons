package com.cutefool.commons.orm.utils;

import com.cutefool.commons.core.util.Strings;
import org.apache.commons.text.StringEscapeUtils;

/**
 * orm工具类
 */
@SuppressWarnings("unused")
public class SqlUtils {

    /**
     * 组装like查询语句
     *
     * @param v v
     * @return 组装后的语句
     */
    public static String like(Object v) {
        return "%" + StringEscapeUtils.escapeXSI(Strings.empty(v)) + "%";
    }


    /**
     * 组装like查询语句
     *
     * @param v v
     * @return 组装后的语句
     */
    public static String leftLike(Object v) {
        return "%" + StringEscapeUtils.escapeXSI(Strings.empty(v));
    }

    /**
     * 组装like查询语句
     *
     * @param v v
     * @return 组装后的语句
     */
    public static String rightLike(Object v) {
        return StringEscapeUtils.escapeXSI(Strings.empty(v)) + "%";
    }
}
