/*
 *
 */
package com.cutefool.commons.app.um.lib;

import lombok.Data;

/**
 * @author 271007729@qq.com
 * @date 2022/7/19 10:43 AM
 */
@Data
public class UmResp {

    private String ret;
    private DataBean data;

    @Data
    public static class DataBean {
        private String msg_id;
    }
}
