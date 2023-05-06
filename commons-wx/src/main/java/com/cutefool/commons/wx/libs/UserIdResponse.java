/*
 *
 */
package com.cutefool.commons.wx.libs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * token
 *
 * @author 271007729@qq.com
 * @date 10/17/21 8:29 PM
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class UserIdResponse extends WxResponse {

    @JsonProperty("UserId")
    private String userId;


}
