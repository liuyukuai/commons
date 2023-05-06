/*
 *
 */
package com.cutefool.commons.sms.lib;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 短信参数【参数都是非必填，应对不同的供应商需要组合必填】
 *
 * @author 271007729@qq.com
 * @date 2020/12/8 10:50
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsParam {

    /**
     * 短信类型
     */
    SmsTypeEnum smsType;


    /**
     * 短信模板
     * <p>
     * eg：您的验证码是:%s。5分钟内有效。
     * 说明：params.put("1", "123456"); map的key任意，保证value值有序即可
     * <p>
     * eg：您的验证码是:{name}。5分钟内有效。
     * 参数配置说明：params.put("{name}", "123456"); map的key是模板中的占位符
     * <p>
     * eg：您的验证码是:123456。5分钟内有效。
     * 参数配置说明：模板无占位符，无需配置参数
     */
    String template;


    /**
     * 模板参数
     */
    LinkedHashMap<String, String> params;


    /**
     * 获取短信内容
     *
     * @return String
     */
    public String getMsg() {
        if (StringUtils.isBlank(this.template)) {
            return "";
        }
        if (CollectionUtils.isEmpty(this.params)) {
            return this.template;
        }
        String msg = this.template;

        // 匹配 %s 占位符
        if (msg.contains(SmsConstants.SPLIT_COMMA_STR)) {
            return String.format(msg, params.values().toArray());
        }

        // 匹配 自定义 占位符
        if (!CollectionUtils.isEmpty(params)) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                msg = msg.replace(entry.getKey(), entry.getValue());
            }
        }
        return msg;
    }


}
