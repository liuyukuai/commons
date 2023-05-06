/*
 *
 */
package com.cutefool.commons.core;

import java.io.Serializable;

/**
 * @author 271007729@qq.com
 * @date 8/31/21 6:01 PM
 */
public interface GroupId extends Serializable {

    /**
     * 获取数据hash值
     *
     * @param num 需要分片的数量
     * @return 具体的值
     */
    long group(Long num);
}
