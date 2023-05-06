/*
 *
 */
package com.cutefool.commons.job.worker;

import lombok.Data;

/**
 * job应用
 *
 * @author 271007729@qq.com
 * @date 2021/9/16 12:40 AM
 */
@Data
public class JobBaseApplication {

    private String address;

    private String appId;

    public JobBaseApplication(String address, String appId) {
        this.address = address;
        this.appId = appId;
    }
}
