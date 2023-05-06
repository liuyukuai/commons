/*
 *
 */
package com.cutefool.commons.sms.lib;

import com.cutefool.commons.core.page.Response;

/**
 * 短信 接口定义
 *
 * @author 271007729@qq.com
 * @date 2020/12/3 14:59
 */
public interface SmsCallable {

    /**
     * 发送短信
     *
     * @param phone    收信人手机号，多个使用英文逗号隔开
     * @param smsParam 短信参数
     * @return 错误信息
     */
    Response<String> sendSystem(String phone, SmsParam smsParam);

    /**
     * 发送短信
     *
     * @param phone    收信人手机号，多个使用英文逗号隔开
     * @param sender   sender
     * @param smsParam 短信参数
     * @return 错误信息
     */
    Response<String> sendUser(String sender, String phone, SmsParam smsParam);


    /**
     * 查询验证码
     *
     * @param phone 手机号
     * @return 验证码
     */
    String getCode(String phone);

    /**
     * 发送验证码
     *
     * @param phone 手机号
     * @param code  验证码
     * @param msg   消息
     * @return 发送结果
     */
    Response<String> sendCode(String phone, String msg, int code);


}
