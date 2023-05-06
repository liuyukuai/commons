/*
 *  
 */
package com.cutefool.commons.api;

import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 * @author 271007729@qq.com
 * @date 2019/9/25 4:44 PM
 */
@Data
@SuppressWarnings("ALL")
public class ApiSave {
    /**
     * id
     */
    private String id;

    /**
     * 项目编号
     */
    private String projectId;
    /**
     * token值
     */
    private String token;
    /**
     * 标题
     */
    private String title;
    /**
     * 请求路径
     */

    private String path;

    /**
     * 菜单名称
     */
    private String menu;
    /**
     * 响应体
     */
    private ApiRequest resBody;

    /**
     * 请求体
     */
    private ApiRequest reqBody;

    /**
     * 接口描述
     */
    private String desc;

    /**
     * 接口请求方式
     */
    private String method;

    /**
     * 请求参数
     */
    private List<ApiQuery> reqQuery;

    /**
     * 请求头信息
     */
    private List<ApiQuery> reqHeaders;
    /**
     * 请求body form
     */
    private List<ApiQuery> reqBodyForm;

    /**
     * 请求参数（路径）
     */
    private List<ApiQuery> reqParams;


    public ApiSave() {
        this.reqBodyForm = Collections.emptyList();
        this.reqHeaders = Collections.emptyList();
    }
}
