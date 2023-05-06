/*
 *  
 */
package com.cutefool.commons.sms.unicom;

import com.cutefool.commons.core.Constants;
import com.cutefool.commons.core.page.Response;
import com.cutefool.commons.core.util.JsonUtils;
import com.cutefool.commons.sms.lib.DefSmsService;
import com.cutefool.commons.sms.lib.SmsConstants;
import com.cutefool.commons.sms.lib.SmsParam;
import com.cutefool.commons.sms.lib.SmsTypeEnum;
import com.cutefool.commons.sms.lib.SmsValidate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.Resource;
import java.nio.charset.Charset;
import java.time.Duration;
import java.util.Optional;

/**
 * 联通 短信
 *
 * @author 271007729@qq.com
 * @date 2020/12/4 14:44
 */
@SuppressWarnings("unused")
@Component(value = SmsConstants.SMS_PREFIX + SmsConstants.SMS_WO)
public class WoSmsService extends DefSmsService {

    @Resource
    private SmsValidate smsValidate;
    @Resource
    private WoSmsConfig woSmsConfig;


    @Override
    public Response<String> sendSystem(String phone, SmsParam smsParam) {
        return this.sendUser(Constants.SYSTEM, phone, smsParam);
    }

    @Override
    public Response<String> sendUser(String sender, String phone, SmsParam smsParam) {
        Response<String> response = smsValidate.valid(sender);
        if (response.isSuccess()) {
            WoSmsRequest request = new WoSmsRequest();
            request.setCpcode(woSmsConfig.getAccount());
            request.setExcode(woSmsConfig.getExtendCode());
            SmsTypeEnum smsType = smsParam.getSmsType();
            WoTemplateEnum woTemplateEnum = WoTemplateEnum.forKey(smsType);
            String templateId = Optional.ofNullable(WoTemplateEnum.forKey(smsType))
                    .map(WoTemplateEnum::getValue)
                    .orElseThrow(() -> new IllegalArgumentException("短信模板ID不能为空"));
            String paramStr = Optional.ofNullable(smsParam.getParams()).map(e -> String.join(SmsConstants.SPLIT_COMMA_EN, e.values())).orElse("");

            request.setTempletid(templateId);
            request.setMsg(paramStr);
            request.setMobiles(phone);
            request.setSign(sign(phone, paramStr, templateId));

            WoSmsResponse send = send(woSmsConfig.getAddress(), JsonUtils.toJson(request));
            if (send.isSuccess()) {
                smsValidate.setSendMessageCount(sender);
            }
            return send.isSuccess() ? Response.ok("") : Response.failure(send.getResultmsg());
        }
        return response;
    }


    /**
     * 获取 sign
     *
     * @param mobiles    手机号
     * @param msg        消息体
     * @param templateId 模板ID
     * @return String
     */
    private String sign(String mobiles, String msg, String templateId) {
        return WoSmsUtil.makeMd5(woSmsConfig.getAccount() + msg + mobiles + woSmsConfig.getExtendCode() + templateId + woSmsConfig.getPassword());

    }


    private static WoSmsResponse send(String path, String postContent) {
        return WebClient.create(path)
                .post()
                .syncBody(postContent)
                .header("Charset", "UTF-8")
                .header("Content-Type", "Application/json")
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(Charset.defaultCharset())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<WoSmsResponse>() {
                })
                .blockOptional(Duration.ofSeconds(10000))
                .orElse(new WoSmsResponse());
    }


}


/*
 {
    "cpcode": "558",
    "msg": "183001,2,10010",
    "mobiles": "18607714703,18648807841",
    "excode": "00000",
    "templetid": "2",
    "sign": "20e90defeea6ea29e643bdd8a80b18e0"
}

*/
