package com.cutefool.commons.jwt;

import lombok.Data;

import java.io.Serializable;
import java.time.Instant;

/**
 * jwt token 类
 *
 * @author 271007729@qq.com
 * @date 2019-07-04 12:56
 */
@Data
@SuppressWarnings({"unused", "WeakerAccess"})
public class JwtToken implements Serializable {

    static final long serialVersionUID = 6739574768237935126L;

    /**
     * token具体的值
     */
    private String token;

    /**
     * token 过期时间（默认有效时间为两个小时）
     */
    private Long expireTime;

    /**
     * token 刷新时间
     */

    private Long refreshTime;

    /**
     * token的类型
     */
    private int type;

    public JwtToken() {

    }

    public JwtToken(String token, Long expireTime) {
        this(token, expireTime, Instant.now().toEpochMilli());
    }

    public JwtToken(String token, Long expireTime, Long refreshTime) {
        this.expireTime = expireTime;
        this.token = token;
        this.refreshTime = refreshTime;
    }

    public JwtToken(String token, Long expireTime, Long refreshTime, int type) {
        this.expireTime = expireTime;
        this.token = token;
        this.refreshTime = refreshTime;
        this.type = type;
    }

    /**
     * @author 271007729@qq.com
     */
    public enum Operation {
        /**
         * 创建
         */
        create(0),
        /**
         * 刷新
         */
        refresh(1),
        /**
         * 销毁
         */
        destroy(-1);

        private int value;

        Operation(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
