/*
 *
 */
package com.cutefool.commons.jwt;

import com.cutefool.commons.core.UserDevices;
import com.cutefool.commons.jwt.context.TokenContext;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Optional;

/**
 * token刷新类
 *
 * @author 271007729@qq.com
 * @date 1/29/21 5:40 PM
 */
@SuppressWarnings("unused")
public class TokenRefresh {

    @Resource
    private JwtProperties jwtProperties;

    @Resource
    private JwtBuilder jwtBuilder;

    @Resource
    private TokenContext tokenContext;


    public Optional<JwtToken> refresh(String token, String ip) {
        return refresh(token, ip, UserDevices.PC.getId());
    }


    public Optional<JwtToken> refresh(String token, String ip, Byte device) {
        Date date = jwtBuilder.expirationDate(token);
        long time = date.getTime();
        long now = System.currentTimeMillis();
        long refresh = (time - now) / 1000;
        if (refresh <= jwtProperties.getRefresh()) {
            return tokenContext.refresh(token, ip, device);
        }
        return Optional.of(new JwtToken(token, null));
    }
}
