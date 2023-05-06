/*
 *  
 */
package com.cutefool.commons.api;

import lombok.Data;

import java.util.List;

/**
 * @author 271007729@qq.com
 * @date 2019/9/26 10:50 AM
 */
@Data
public class ApiRequest {

    /**
     * 响应实体类型
     */
    private String type;

    /**
     * 项目分类
     */
    private String projectId;

    /**
     * 对象描述
     */
    private String description;

    /**
     * 属性集合
     */
    private List<ApiField> fields;


    @Data
    public static class ApiField {

        /**
         * 属性名称
         */
        private String name;

        /**
         * 响应实体类型
         */
        private String type;

        /**
         * 对象描述
         */
        private String description;

        /**
         * 属性集合
         */
        private List<ApiField> fields;


        /**
         * 如果是数组
         */
        private ApiRequest items;

        /**
         * 是否必填
         */
        private boolean required;

    }
}
