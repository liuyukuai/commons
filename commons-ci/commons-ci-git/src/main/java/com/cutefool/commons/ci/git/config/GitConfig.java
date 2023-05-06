/*
 *
 */
package com.cutefool.commons.ci.git.config;

import lombok.Data;
import com.cutefool.commons.ci.core.CiConfig;

/**
 * git配置类
 *
 * @author 271007729@qq.com
 * @date 5/26/21 9:44 AM
 */


@Data
public class GitConfig implements CiConfig {

    /**
     * 本地git库目录、默认为user.home
     */
    private String dir;

    /**
     * 用户名
     */
    private String username;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 密码
     */
    private String password;

    /**
     * ssh目录
     */
    private String sshHome;

}
