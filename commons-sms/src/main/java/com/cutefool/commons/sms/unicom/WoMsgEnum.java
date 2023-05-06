/*
 *
 */
package com.cutefool.commons.sms.unicom;

import lombok.Getter;

/**
 * 联通短信 错误码 枚举
 *
 * @author 271007729@qq.com
 * @date 2019/10/29 9:19
 */
@Getter
public enum WoMsgEnum {

    /**
     * 错误码说明
     */
    SUCCESS(0, "成功"),
    MSG_301(301, "JSON的格式错误"),
    MSG_302(302, "非法商户信息"),
    MSG_304(304, "缺少cpid参数"),
    MSG_305(305, "商户状态异常"),
    MSG_306(306, "商户能力异常"),
    MSG_307(307, "模板参数值不足"),
    MSG_308(308, "缺少msg参数"),
    MSG_309(309, "msg长度非法"),
    MSG_310(310, "包含敏感词"),
    MSG_311(311, "缺少mobiles参数"),
    MSG_312(312, "mobiles长度非法"),
    MSG_313(313, "缺少excode参数"),
    MSG_314(314, "缺少templetid参数"),
    MSG_315(315, "缺少sign参数"),
    MSG_316(316, "签名校验不通过"),
    MSG_317(317, "程序异常"),


    /**
     * 空【防止空指针】
     */
    EMPTY(-1, "");


    /**
     * 主键 / 编码
     */
    private final int key;

    /**
     * 名称 / 文案值
     */
    private final String value;


    WoMsgEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }


    public String getStringValue() {
        return String.valueOf(this.key);
    }


    public static String getValue(int value) {
        WoMsgEnum forInt = forValue(value);
        return forInt == null ? "" : forInt.value;
    }


    /**
     * 获取 枚举
     *
     * @param value value
     * @return 【非null】WoMsgEnum
     */
    public static WoMsgEnum forValue(int value) {
        for (WoMsgEnum type : values()) {
            if (type.key == value) {
                return type;
            }
        }
        return EMPTY;
    }


}



/*
 错误码说明
0	成功
301	JSON的格式错误
302	非法商户信息
304	缺少cpid参数
305	商户状态异常
306	商户能力异常
307	模板参数值不足
308	缺少msg参数
309	msg长度非法
310	包含敏感词
311	缺少mobiles参数
312	mobiles长度非法
313	缺少excode参数
314	缺少templetid参数
315	缺少sign参数
316	签名校验不通过
317	程序异常
*/