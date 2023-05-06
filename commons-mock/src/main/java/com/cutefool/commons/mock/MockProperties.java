/*
 *
 */
package com.cutefool.commons.mock;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * mock 配置项类
 *
 * @author 271007729@qq.com
 * @date 2019-08-01 02:40
 */
@Data
@Component
@ConfigurationProperties(prefix = "commons.request.mock")
public class MockProperties implements Serializable {

    private static final long serialVersionUID = 3434861454239444273L;
    /**
     * 是否启用mock，默认为false，不启用
     */
    @Value("${commons.request.mock.enabled:false}")
    private Boolean enabled;


    /**
     * 是否启用mock，默认为false，不启用
     */
    @Value("${commons.request.mock.orgCode:-1}")
    private String orgCode;


    /**
     * 是否启用mock，默认为false，不启用
     */
    @Value("${commons.request.mock.userId:1001}")
    private Long userId;

    /**
     * mockServer的地址
     */
    @Value("${commons.request.mock.address:}")
    private String address;

    /**
     * department
     */
    @Value("${commons.request.mock.department:}")
    private String department;

    /**
     * positions
     */
    @Value("${commons.request.mock.positions:}")
    private String positions;


    /**
     * 用户类型【0:普通用户，1:超级管理员，2:子系统管理员】
     */
    @Value("${commons.request.mock.userType:1}")
    private Byte userType;

    /**
     * 租户
     */
    @Value("${commons.request.mock.tenantId:0}")
    private Long tenantId;


}
