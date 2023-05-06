/*
 *
 */
package com.cutefool.commons.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * api 分类
 *
 * @author 271007729@qq.com
 * @date 2019/9/26 2:42 PM
 */
@Data
@SuppressWarnings("unused")
public class ApiCat {

    @JsonProperty("_id")
    private Integer id;

    private String name;

    private String desc;

    private String token;

    @JsonProperty("project_id")
    private String projectId;


    public ApiCat() {
    }

    public ApiCat(String token, String projectId) {
        this.token = token;
        this.projectId = projectId;
    }

    public ApiCat(String name, String token, String projectId) {
        this.name = name;
        this.token = token;
        this.projectId = projectId;
    }
}
