/*
 *
 */
package com.cutefool.commons.sms.cl253;

import lombok.Data;
import com.cutefool.commons.core.page.Responsive;

import java.util.Objects;

/**
 * 短信发送响应
 *
 * @author 271007729@qq.com
 * @date 2020/3/10 4:13 PM
 */
@Data
public class ClSmsResponse implements Responsive {

    /**
     * 响应时间
     */
    private String time;

    /**
     * 消息id
     */
    private String msgId;

    /**
     * 状态码说明（成功返回空）
     */
    private String errorMsg;

    /**
     * 状态码（详细参考提交响应状态码）
     */
    private String code;


    @Override
    public boolean isSuccess() {
        return Objects.equals("0", code);
    }

}
