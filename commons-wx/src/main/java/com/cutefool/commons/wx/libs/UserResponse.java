/*
 *  
 */
package com.cutefool.commons.wx.libs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * token
 *
 * @author 271007729@qq.com
 * @date 10/17/21 8:29 PM
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class UserResponse extends WxResponse {

    @JsonProperty("userid")
    private String userId;
    private String name;
    private String position;
    private String mobile;
    private String gender;
    private String email;
    private String avatar;
    private String telephone;
    private String alias;
    private String address;
    private int status;
    private List<Integer> department;
    private List<Integer> order;
}
