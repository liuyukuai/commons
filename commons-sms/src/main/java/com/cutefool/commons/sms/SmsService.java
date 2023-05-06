/*
 *
 */
package com.cutefool.commons.sms;

import com.cutefool.commons.sms.lib.SmsCallable;
import com.cutefool.commons.sms.lib.SmsConfig;
import com.cutefool.commons.sms.lib.SmsConstants;
import com.cutefool.commons.sms.lib.SmsParam;
import com.cutefool.commons.core.BizException;
import com.cutefool.commons.core.page.Response;
import com.cutefool.commons.sms.lib.*;
import com.cutefool.commons.spring.SpiSpringContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * 短信 接口实现
 *
 * @author 271007729@qq.com
 * @date 2020/12/3 15:41
 */
@Component
public class SmsService implements SmsCallable {

    @Resource
    private SmsConfig smsConfig;


    @Override
    public Response<String> sendSystem(String phone, SmsParam smsParam) {
        if (!smsConfig.isEnabled()) {
            return Response.ok("common sms is disabled.");
        }
        SmsCallable spi = SpiSpringContext.getSpi(SmsConstants.SMS_PREFIX + smsConfig.getType(), SmsCallable.class);
        if (Objects.nonNull(spi)) {
            return spi.sendSystem(phone, smsParam);
        }
        throw new BizException("please config commons.sms ...");
    }

    @Override
    public Response<String> sendUser(String sender, String phone, SmsParam smsParam) {
        if (!smsConfig.isEnabled()) {
            return Response.ok("common sms is disabled.");
        }
        SmsCallable spi = SpiSpringContext.getSpi(SmsConstants.SMS_PREFIX + smsConfig.getType(), SmsCallable.class);
        if (Objects.nonNull(spi)) {
            return spi.sendUser(sender, phone, smsParam);
        }
        throw new BizException("please config commons.sms ...");
    }


    @Override
    public String getCode(String phone) {
        SmsCallable spi = SpiSpringContext.getSpi(SmsConstants.SMS_PREFIX + smsConfig.getType(), SmsCallable.class);
        if (Objects.nonNull(spi)) {
            return spi.getCode(phone);
        }
        throw new BizException("please config commons.sms ...");
    }


    @Override
    public Response<String> sendCode(String phone, String msg, int code) {
        SmsCallable spi = SpiSpringContext.getSpi(SmsConstants.SMS_PREFIX + smsConfig.getType(), SmsCallable.class);
        if (Objects.nonNull(spi)) {
            return spi.sendCode(phone, msg, code);
        }
        throw new BizException("please config commons.sms ...");
    }

}
