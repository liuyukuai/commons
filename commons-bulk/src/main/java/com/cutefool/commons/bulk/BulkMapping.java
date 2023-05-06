package com.cutefool.commons.bulk;

/**
 * 填充属性字段映射
 *
 * @author 271007729@qq.com
 * @date 2019-07-03 09:30
 */
@SuppressWarnings("unused")
public @interface BulkMapping {
    /**
     * 源
     *
     * @return s
     */
    String s();

    /**
     * 目标
     *
     * @return d
     */
    String d();


    /**
     * 属性说明
     *
     * @return 属性说明
     */
    String remarks();
}
