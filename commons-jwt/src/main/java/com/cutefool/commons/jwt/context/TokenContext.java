package com.cutefool.commons.jwt.context;

import com.cutefool.commons.core.BizException;
import com.cutefool.commons.core.UserDevices;
import com.cutefool.commons.core.page.ResponseCode;
import com.cutefool.commons.core.util.JsonUtils;
import com.cutefool.commons.core.util.Md5Utils;
import com.cutefool.commons.jwt.JwtBuilder;
import com.cutefool.commons.jwt.JwtToken;
import io.jsonwebtoken.Claims;
import com.cutefool.commons.jwt.JwtProperties;
import com.cutefool.commons.jwt.listener.TokenListener;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

/**
 * token上下文管理
 *
 * @author 271007729@qq.com
 * @date 2019-08-01 05:11
 */

@SuppressWarnings({"unused", "WeakerAccess"})
public class TokenContext {

    @Resource
    private JwtProperties jwtProperties;

    @Resource
    private JwtBuilder jwtBuilder;

    @Resource
    private TokenListener tokenListener;

    @Resource
    private ValueOperations<String, String> valueOperations;


    /**
     * 刷新token值
     *
     * @param token token值
     * @return 新token
     */
    public Optional<JwtToken> refresh(String token, String ip) {
        return refresh(token, ip, UserDevices.PC.getId());
    }

    /**
     * 刷新token值
     *
     * @param token token值
     * @return 新token
     */
    public Optional<JwtToken> refresh(String token, String ip, Byte device) {
        Optional<HashMap> decode = this.jwtBuilder.decode(token, HashMap.class);
        if (decode.isPresent()) {
            Boolean validate = this.validate(token, ip);
            if (validate) {
                String key = this.getKey(token);
                // 过期时间
                Date expirationDate = jwtBuilder.expirationDate(token);

                // 设置旧的token值到缓存
                Boolean ifAbsent = valueOperations.setIfAbsent(key, JsonUtils.toJson(new JwtToken(token, expirationDate.getTime(), Instant.now().toEpochMilli(), JwtToken.Operation.refresh.getValue())));

                if (!Objects.isNull(ifAbsent) && ifAbsent) {
                    //设置过期时间
                    valueOperations.getOperations().expireAt(key, expirationDate);
                    // 刷新token的值
                    return Optional.ofNullable(this.jwtBuilder.refresh(token, device));
                }
            }

        }
        return Optional.empty();
    }

    /**
     * 销毁token值
     *
     * @param token token
     * @return true: 成功，false: 失败
     */
    public Boolean destroy(String token) {
        // 判断是否过期
        if (jwtBuilder.isExpired(token)) {
            return true;
        }

        String key = this.getKey(token);
        Claims claims = jwtBuilder.claims(token);
        // 过期时间
        Date expirationDate = claims.getExpiration();
        // 存储到缓存
        valueOperations.set(key, JsonUtils.toJson(new JwtToken(token, expirationDate.getTime(), Instant.now().toEpochMilli(), JwtToken.Operation.destroy.getValue())));
        //设置过期时间
        valueOperations.getOperations().expireAt(key, expirationDate);
        // 通知销毁事件
        tokenListener.onDestroy(claims, LocalDateTime.now());

        return true;
    }

    /**
     * 校验token是否合法
     *
     * @param token token的值
     * @return true:合法，false：非法
     */
    public Boolean validate(String token, String ip) {
        if (StringUtils.isBlank(token)) {
            return false;
        }

        // 缓存的key值
        String key = this.getKey(token);

        try {
            // 判断缓存中是否有token值
            Optional<JwtToken> optional = JsonUtils.toBean(valueOperations.get(key), JwtToken.class);
            // 缓存中只存销毁的和刷新导致失效的token
            if (optional.isPresent()) {
                JwtToken jwtToken = optional.get();

                // 销毁状态
                if (JwtToken.Operation.destroy.getValue() == jwtToken.getType()) {
                    return false;
                }

                // 如果是刷新状态的token
                Long refreshTime = jwtToken.getRefreshTime();

                if (!Objects.isNull(refreshTime) && jwtToken.getType() == JwtToken.Operation.refresh.getValue()) {
                    // 刷新token二分钟内可以使用
                    return Instant.now().toEpochMilli() - refreshTime < 1000 * 60 * 2;
                }
            }
            // 如果缓存没有值，则判断token是否过期

            // 从token中获取用户信息
            Optional<HashMap> userOptional = jwtBuilder.decode(token, HashMap.class);

            // 判断用户是否为空
            return userOptional.map(e -> !jwtBuilder.isExpired(token) && jwtBuilder.validateIp(token, ip)).orElse(false);
        } catch (RedisConnectionFailureException e) {
            throw new BizException(e, ResponseCode.REDIS_SERVICE_IS_DOWN.getCode(), ResponseCode.REDIS_SERVICE_IS_DOWN.getMessage());
        }
    }

    /**
     * 获取token的key值
     *
     * @param token token
     * @return key
     */
    private String getKey(String token) {
        return jwtProperties.getPrefix() + Md5Utils.digestMd5(token);
    }

}
