/*
 *
 */
package com.cutefool.commons.jwt.listener;

import io.jsonwebtoken.Claims;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 默认token监听类
 *
 * @author 271007729@qq.com
 * @date 2020-12-18 14:14
 */
public class DefaultTokenListener implements TokenListener {

    @Override
    public void onCreated(Map<String, Object> claims, LocalDateTime createTime) {

    }

    @Override
    public void onRefresh(Claims claims, LocalDateTime createTime) {

    }

    @Override
    public void onValidate(Claims claims, LocalDateTime createTime) {

    }

    @Override
    public void onDestroy(Claims claims, LocalDateTime createTime) {

    }

    @Override
    public void onExpired(Claims claims, LocalDateTime createTime) {

    }
}
