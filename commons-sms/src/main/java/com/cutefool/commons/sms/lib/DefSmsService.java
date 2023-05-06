/*
 *
 */
package com.cutefool.commons.sms.lib;

import com.cutefool.commons.core.page.Response;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

/**
 * 短信 接口实现【部分】
 *
 * @author 271007729@qq.com
 * @date 2020/12/3 15:41
 */
public abstract class DefSmsService implements SmsCallable {

    @Resource
    protected RedisTemplate<String, String> redisTemplate;


    @Override
    public Response<String> sendSystem(String phone, SmsParam smsParam) {
        throw new IllegalArgumentException(" please override this method");
    }


    /**
     * 查询验证码
     *
     * @param phone 手机号
     * @return 验证码
     */
    @Override
    public String getCode(String phone) {
        if (StringUtils.isNotBlank(phone)) {
            return this.redisTemplate.opsForValue().get(phone);
        }
        return "";
    }

    /**
     * 发送验证码
     *
     * @param phone  手机号
     * @param code   验证码
     * @param msg    消息
     * @return 发送结果
     */
    @Override
    @Deprecated
    public Response<String> sendCode(String phone, String msg, int code) {
        throw new IllegalArgumentException(" please override this method");
    }


}
