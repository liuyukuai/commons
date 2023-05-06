package com.cutefool.commons.core.digest;

import com.cutefool.commons.core.BizException;
import com.cutefool.commons.core.page.ResponseCode;
import com.cutefool.commons.core.util.Times;
import lombok.extern.slf4j.Slf4j;

import java.util.Base64;

/**
 * 签名执行器
 */
@Slf4j
public class DigestsTimeInvoker implements DigestsInvoker {

    @Override
    public boolean validate(String s, long expirationTimes) {
        return validate(load(s), expirationTimes);
    }

    public boolean validate(DigestLibs libs, long expirationTimes) {
        long time = Times.getTime();
        return time - libs.getTime() < expirationTimes;
    }

    @Override
    public String getValue(String s, long expirationTimes) {
        DigestLibs libs = load(s);
        boolean validate = this.validate(libs, expirationTimes);
        if (validate) {
            return libs.getValue();
        }
        throw new BizException(ResponseCode.DIGESTS_ERROR);
    }

    public  DigestLibs load(String s) {
        String v = new String(Base64.getDecoder().decode(s));
        log.debug("decode s = {} v = {}", s, v);
        // 当前时间
        long now = Times.getTime();
        int length = String.valueOf(now).length();
        String times = v.substring(0, length);
        String value = v.substring(length);
        return new DigestLibs(Long.parseLong(times), value);
    }
}
