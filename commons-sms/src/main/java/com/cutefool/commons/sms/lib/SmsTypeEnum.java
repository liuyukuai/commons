/*
 *  
 */
package com.cutefool.commons.sms.lib;

import lombok.Getter;

/**
 * 联通短信 错误码 枚举
 *
 * @author 271007729@qq.com
 * @date 2019/10/29 9:19
 */
@Getter
public enum SmsTypeEnum {

    /**
     * 验证码
     */
    VERIFICATION_CODE,

    /**
     * 密码 初始化/变更 通知
     */
    PASSWORD_NOTIFICATION,

    /**
     * 应用上线通知
     */
    APP_ON_LINE,
    /**
     * 应用掉线通知
     */
    APP_OFF_LINE,

    /**
     * 采集器上线通知
     */
    COLLECTOR_ON_LINE,
    /**
     * 采集器掉线通知
     */
    COLLECTOR_OFF_LINE,

    /**
     * 传感器触发报警
     */
    COLLECTOR_TRIGGER_ALARM,
    /**
     * 监测到报警
     */
    DETECTION_ALARM,

    /**
     * 空模板，内容自定义【前提是供应商支持】
     */
    EMPTY,


}

