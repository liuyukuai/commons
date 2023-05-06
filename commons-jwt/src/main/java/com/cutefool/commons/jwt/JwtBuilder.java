package com.cutefool.commons.jwt;

import com.cutefool.commons.core.BizException;
import com.cutefool.commons.core.Login;
import com.cutefool.commons.core.UserDevices;
import com.cutefool.commons.core.page.ResponseCode;
import com.cutefool.commons.core.util.JsonUtils;
import com.cutefool.commons.core.util.Times;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import com.cutefool.commons.jwt.listener.TokenListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * jwt构造
 *
 * @author 271007729@qq.com
 * @date 2019-07-04 12:56
 */

@Slf4j
@SuppressWarnings({"unused", "WeakerAccess"})
public class JwtBuilder implements Serializable {

    private static final long serialVersionUID = -3301605591108950415L;

    /**
     * 创建时间
     */
    private static final String CLAIM_KEY_CREATED = "created";

    /**
     * ip
     */
    private static final String CLAIM_KEY_IP = "ip";

    @Resource
    private JwtProperties jwtProperties;

    @Resource
    private TokenListener tokenListener;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 重token中获取token创建时间
     *
     * @param token 当前token
     * @return token创建时间
     */
    public Instant createdTime(String token) {
        final Claims claims = claims(token);
        return Instant.ofEpochMilli((Long) claims.get(CLAIM_KEY_CREATED));
    }

    /**
     * 重token中获取token过期时间
     *
     * @param token 当前token
     * @return token创建时间
     */
    public Date expirationDate(String token) {
        final Claims claims = claims(token);
        return claims.getExpiration();
    }

    /**
     * 重token中获取token过期时间
     *
     * @param token 当前token
     * @param ip    ip
     * @return token创建时间
     */
    public boolean validateIp(String token, String ip) {
        final Claims claims = claims(token);
        // 通知校验事件
        tokenListener.onValidate(claims, LocalDateTime.now());
        //  去掉校验IP
        return true;
//        return Objects.equals(claims.get(CLAIM_KEY_IP, String.class), ip) || Objects.equals(claims.get(CLAIM_KEY_IP, String.class), "127.0.0.1");
    }

    /**
     * 解析token
     *
     * @param token 当前token
     * @return 解析后的值
     */
    public Claims claims(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(jwtProperties.getSecret())
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.error("get claims from jwt token error : {}", e.getMessage());
            // 通知过期事件
            tokenListener.onExpired(e.getClaims(), LocalDateTime.now());
            throw new BizException(ResponseCode.USER_TOKEN_EXPIRED.getCode(), ResponseCode.USER_TOKEN_EXPIRED.getMessage());
        } catch (Exception e) {
            log.error("get claims from jwt token error : {}", e.getMessage());
            throw new BizException(ResponseCode.USER_TOKEN_INVALID.getCode(), ResponseCode.USER_TOKEN_INVALID.getMessage());
        }
        return claims;
    }


    /**
     * 生成token过期时间
     *
     * @return token过期时间
     */
    private Date generateExpirationDate(Byte device) {
        device = Objects.nonNull(device) ? device : UserDevices.PC.getId();
        if (Objects.equals(device, UserDevices.PC.getId())) {
            return new Date(System.currentTimeMillis() + jwtProperties.getExpiration() * 1000);
        }
        return new Date(System.currentTimeMillis() + jwtProperties.getAppExpiration() * 1000);
    }

    /**
     * 通过用户信息创建token
     *
     * @param t  需要encode的对象
     * @param ip ip地址
     * @return token
     */
    public <T extends Login> JwtToken encode(T t, String ip) {
        return encode(t, ip, UserDevices.PC.getId());
    }


    /**
     * 通过用户信息创建token
     *
     * @param t      需要encode的对象
     * @param ip     ip地址
     * @param device 用户登录设备
     * @return token
     */
    public <T extends Login> JwtToken encode(T t, String ip, Byte device) {
        Date expireTime = generateExpirationDate(device);
        if (jwtProperties.isMulti()) {
            // TODO WE
            t.setLoginTime(Times.getTime());
        }
        Map<String, Object> claims = JsonUtils.toMap(t).orElseThrow(() -> new RuntimeException("parse jwt object error."));
        claims.put(CLAIM_KEY_IP, ip);
        // 通过创建事件
        LocalDateTime now = LocalDateTime.now();
        tokenListener.onCreated(claims, now);
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        JwtToken jwtToken = new JwtToken(encode(claims, expireTime), expireTime.getTime());
        valueOperations.set(jwtProperties.getTokenKey() + jwtToken.getToken(), "1", jwtProperties.getExpiration(), TimeUnit.SECONDS);
        return jwtToken;
    }


    /**
     * 通过map创建token对象
     *
     * @param claims map
     * @return token
     */
    private String encode(Map<String, Object> claims, Date expireTime) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(expireTime)
                .signWith(SignatureAlgorithm.HS512, jwtProperties.getSecret())
                .compact();
    }

    /**
     * 刷新token的值
     *
     * @param token 原来的token值
     * @return 刷新后的token值
     */
    public JwtToken refresh(String token) {
        return refresh(token, UserDevices.PC.getId());
    }


    /**
     * 刷新token的值
     *
     * @param token  原来的token值
     * @param device 用户登录设备
     * @return 刷新后的token值
     */
    public JwtToken refresh(String token, Byte device) {
        String refreshedToken;
        Date expireTime = generateExpirationDate(device);
        final Claims claims = claims(token);
        claims.put(CLAIM_KEY_CREATED, expireTime.getTime());
        refreshedToken = encode(claims, expireTime);
        // 通知刷新事件
        tokenListener.onRefresh(claims, LocalDateTime.now());
        JwtToken jwtToken = new JwtToken(refreshedToken, expireTime.getTime(), expireTime.getTime());
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        redisTemplate.delete(jwtProperties.getTokenKey() + token);
        valueOperations.set(jwtProperties.getTokenKey() + jwtToken.getToken(), "1", jwtProperties.getExpiration(), TimeUnit.SECONDS);
        return jwtToken;
    }


    /**
     * 判断token是否过期
     *
     * @param token token
     * @return 是否过期
     */
    public Boolean isExpired(String token) {
        final Date expiration = expirationDate(token);
        return Optional.ofNullable(expiration).map(e -> e.before(new Date())).orElse(false);
    }


    /**
     * 获取token中的对象
     *
     * @param token token的值
     * @return obj
     */
    public <T> Optional<T> decode(String token, Class<T> clz) {
        Claims claims = this.claims(token);
        return JsonUtils.toBean(claims, clz);
    }
}

