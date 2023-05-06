/*
 *
 */
package com.cutefool.commons.ci.git.libs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;

/**
 * GitContext
 *
 * @author 271007729@qq.com
 * @date 5/26/21 10:00 AM
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GitContext {
    /**
     * 名称
     */
    private String name;

    /**
     * 完整地址
     */
    private String address;

    /**
     * 本地路径
     */
    private File localPath;
}
