/*
 *
 */
package com.cutefool.commons.sms.unicom;

import lombok.Data;
import com.cutefool.commons.core.page.Responsive;

import java.util.Objects;

/**
 * 联通 短信发送响应
 *
 * @author 271007729@qq.com
 * @date 2020/12/4 17:03
 */
@Data
public class WoSmsResponse implements Responsive {

    /**
     * 错误码，请参考下节《错误码说明》
     */
    private String resultcode;

    /**
     * 错误码说明，请参考下节《错误码说明》
     */
    private String resultmsg;

    /**
     * 内部订单号，匹配状态报告使用
     */
    private String taskid;


    @Override
    public boolean isSuccess() {
        return Objects.equals(resultcode, WoMsgEnum.SUCCESS.getStringValue());
    }

}
