/*
 *  
 */
package com.cutefool.commons.wx.libs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author 271007729@qq.com
 * @date 10/17/21 8:13 PM
 */
@Data
public class WxResponse {

    @JsonProperty("errcode")
    private int code;

    @JsonProperty("errmsg")
    private String msg;
}
