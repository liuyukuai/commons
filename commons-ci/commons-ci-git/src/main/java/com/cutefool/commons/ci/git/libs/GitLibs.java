/*
 *
 */
package com.cutefool.commons.ci.git.libs;

import lombok.Data;

/**
 * Git 传输对象
 *
 * @author 271007729@qq.com
 * @date 5/26/21 10:00 AM
 */
@Data
public class GitLibs {
    /**
     * 名称
     */
    private String name;

    /**
     * 完整地址
     */
    private String address;
}
