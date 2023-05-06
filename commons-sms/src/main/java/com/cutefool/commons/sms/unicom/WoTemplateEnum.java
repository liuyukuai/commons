/*
 *  
 */
package com.cutefool.commons.sms.unicom;

import lombok.Getter;
import com.cutefool.commons.sms.lib.SmsTypeEnum;

/**
 * 联通短信 模板
 *
 * @author 271007729@qq.com
 * @date 2019/10/29 9:19
 */
@Getter
public enum WoTemplateEnum {

    /**
     * 验证码模板ID 还没有 TODO
     */
    VERIFICATION_CODE(SmsTypeEnum.VERIFICATION_CODE, "??"),

    /**
     * 应用上线通知:%s
     */
    APP_ON_LINE(SmsTypeEnum.APP_ON_LINE, "117698"),
    /**
     * 应用掉线通知:%s
     */
    APP_OFF_LINE(SmsTypeEnum.APP_OFF_LINE, "117696"),

    /**
     * 采集器上线通知:%s
     */
    COLLECTOR_ON_LINE(SmsTypeEnum.COLLECTOR_ON_LINE, "117811"),
    /**
     * 采集器掉线通知:%s
     */
    COLLECTOR_OFF_LINE(SmsTypeEnum.COLLECTOR_OFF_LINE, "117695"),

    /**
     * 尊敬的客户，%s位号%s传感器触发%s报警，请在%s分钟内通过%s进行反馈。
     */
    COLLECTOR_TRIGGER_ALARM(SmsTypeEnum.COLLECTOR_TRIGGER_ALARM, "159267"),
    /**
     * 您好，%s监测到%s报警，已达到推送条件%s。请您在%s内进行处理。
     */
    DETECTION_ALARM(SmsTypeEnum.DETECTION_ALARM, "159268"),

    ;


    /**
     * 主键 / 编码
     */
    private final SmsTypeEnum key;

    /**
     * 名称 / 文案值
     */
    private final String value;


    WoTemplateEnum(SmsTypeEnum key, String value) {
        this.key = key;
        this.value = value;
    }


    /**
     * 获取 枚举
     *
     * @param key value
     * @return WoTemplateEnum or null
     */
    public static WoTemplateEnum forKey(SmsTypeEnum key) {
        for (WoTemplateEnum type : values()) {
            if (type.key == key) {
                return type;
            }
        }
        return null;
    }


}

