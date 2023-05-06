/*
 *
 */
package com.cutefool.commons.jwt.listener;

import io.jsonwebtoken.Claims;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * token监听类
 *
 * @author 271007729@qq.com
 * @date 2020-12-18 14:14
 */
public interface TokenListener {
    /**
     * 创建
     *
     * @param claims     claims
     * @param createTime createTime
     */
    void onCreated(Map<String, Object> claims, LocalDateTime createTime);

    /**
     * 刷新
     *
     * @param claims     claims
     * @param createTime createTime
     */
    void onRefresh(Claims claims, LocalDateTime createTime);


    /**
     * 访问
     *
     * @param claims     claims
     * @param createTime createTime
     */
    void onValidate(Claims claims, LocalDateTime createTime);

    /**
     * 销毁
     *
     * @param claims     claims
     * @param createTime createTime
     */
    void onDestroy(Claims claims, LocalDateTime createTime);

    /**
     * 过期
     *
     * @param claims     claims
     * @param createTime createTime
     */
    void onExpired(Claims claims, LocalDateTime createTime);
}
