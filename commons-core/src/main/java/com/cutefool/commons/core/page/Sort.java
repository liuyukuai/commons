package com.cutefool.commons.core.page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 271007729@qq.com
 * @date 2019-07-03 09:51
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("unused")
public class Sort {

    public static final String DESC = "desc";

    public static final String ASC = "asc";
    /**
     * name
     */
    private String name;
    /**
     * direction
     */
    private String direction;

    /**
     * null值排序到最后
     */
    private boolean nullLast;


    public static Sort desc(String name, boolean nullLast) {
        return new Sort(name, DESC, nullLast);
    }

    public static Sort desc(String name) {
        return desc(name, false);
    }

    public static Sort asc(String name, boolean nullLast) {
        return new Sort(name, ASC, nullLast);
    }

    public static Sort asc(String name) {
        return asc(name, false);
    }
}
