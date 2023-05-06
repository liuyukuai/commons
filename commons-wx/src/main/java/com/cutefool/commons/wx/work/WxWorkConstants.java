/*
 *
 */
package com.cutefool.commons.wx.work;

/**
 * 企业微信相关常量
 *
 * @author 271007729@qq.com
 * @date 10/15/21 4:01 PM
 */
@SuppressWarnings("WeakerAccess")
public class WxWorkConstants {

    /**
     * 获取企业微信token地址
     */
    public static final String GET_TOKEN_URL = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=%s&corpsecret=%s";

    /**
     * 获取企业微信用户id地址
     */
    public static final String GET_USER_ID_URL = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token=%s&code=%s";

    /**
     * 获取企业微信用户地址
     */
    public static final String GET_USER_INFO_URL = "https://qyapi.weixin.qq.com/cgi-bin/user/get?access_token=%s&userid=%s";
}
