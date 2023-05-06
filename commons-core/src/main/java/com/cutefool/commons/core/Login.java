/*
 *
 */
package com.cutefool.commons.core;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.List;

/**
 * 用户信息基础类
 *
 * @author 271007729@qq.com
 * @date 2/23/21 10:14 AM
 */
@Data
@SuppressWarnings("unused")
public class Login {

    /**
     * 用户ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 用户登录名
     */
    private String loginName;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户移动电话
     */
    private String mobile;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 用户类型
     */
    private Byte userType;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 用户角色
     */
    private List<String> roles;

    /**
     * 登录方式
     */
    private String authType;

    /**
     * 登录设备
     */
    private Byte device;

    /**
     * 登录时间
     */
    private Long loginTime;

    /**
     * 是否拒绝登录
     */
    private Boolean refuse;

}
