/*
 *
 */
package com.cutefool.commons.api;

import com.cutefool.commons.core.page.Response;

/**
 * API操作接口
 *
 * @author 271007729@qq.com
 * @date 2019/9/26 2:16 PM
 */
@SuppressWarnings("unused")
public interface ApiOperation {

    /**
     * 创建api
     *
     * @param apiSave apiSave
     * @return response
     */
    Response<String> create(ApiSave apiSave);
}
