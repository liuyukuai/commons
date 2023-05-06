/*
 *
 */
package com.cutefool.commons.sms.cl253;

import com.cutefool.commons.sms.lib.*;
import com.cutefool.commons.core.Constants;
import com.cutefool.commons.core.page.Response;
import com.cutefool.commons.core.util.JsonUtils;
import com.cutefool.commons.sms.lib.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Duration;

/**
 * 联通 短信
 *
 * @author 271007729@qq.com
 * @date 2020/12/4 14:44
 */
@SuppressWarnings("unused")
@Component(value = SmsConstants.SMS_PREFIX + SmsConstants.SMS_CL_253)
public class ClSmsService extends DefSmsService {


    @Resource
    private SmsConfig smsConfig;

    @Resource
    private ClSmsConfig clSmsConfig;

    @Resource
    private SmsValidate smsValidate;


    @Override
    public Response<String> sendSystem(String phone, SmsParam smsParam) {
        return this.sendUser(Constants.SYSTEM, phone, smsParam);
    }

    @Override
    public Response<String> sendUser(String sender, String phone, SmsParam smsParam) {
        Response<String> response = smsValidate.valid(sender);
        if (response.isSuccess()) {
            String msg = smsParam.getMsg();
            String sign = "【" + clSmsConfig.getSign() + "】";
            if (!msg.startsWith(sign)) {
                msg = sign + msg;
            }
            return this.send(sender, phone, msg);
        }
        return response;
    }


    private Response<String> send(String sender, String phone, String msg) {
        Response<String> response = smsValidate.valid(sender);
        if (response.isSuccess()) {
            ClSmsRequest request = new ClSmsRequest();
            request.setAccount(clSmsConfig.getAccount());
            request.setPassword(clSmsConfig.getPassword());
            request.setMsg(msg);
            request.setPhone(phone);
            ClSmsResponse clSmsResponse = ClSmsUtil.send(clSmsConfig.getAddress(), JsonUtils.toJson(request));
            if (clSmsResponse.isSuccess()) {
                smsValidate.setSendMessageCount(sender);
            }
            return clSmsResponse.isSuccess() ? Response.ok("") : Response.failure(clSmsResponse.getErrorMsg());
        }
        return response;
    }


    @Override
    public Response<String> sendCode(String phone, String msg, int code) {
        if (StringUtils.isBlank(phone)) {
            return Response.failure("手机号为空");
        }
        // 判断手机号是否已经发送过验证码
        String s = this.redisTemplate.opsForValue().get(phone);
        if (StringUtils.isNotBlank(s)) {
            return Response.ok(s);
        }

        if (smsConfig.isEnabled()) {
            Response<String> response = this.send(phone, phone, msg);
            // 存储验证码
            if (response.isSuccess()) {
                this.redisTemplate.opsForValue().set(phone, String.valueOf(code), Duration.ofMinutes(10));
            }
            return response;
        }

        this.redisTemplate.opsForValue().set(phone, String.valueOf(code), Duration.ofMinutes(10));
        return Response.ok("commons sms is disabled.");
    }


}
