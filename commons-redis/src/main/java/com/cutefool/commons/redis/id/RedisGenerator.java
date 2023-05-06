/*
 *
 */
package com.cutefool.commons.redis.id;

import com.cutefool.commons.core.BizException;
import com.cutefool.commons.core.page.ResponseCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.Objects;
import java.util.Optional;

/**
 * id生成器
 *
 * @author 271007729@qq.com
 * @date 2019/11/6 11:53 AM
 */

@SuppressWarnings({"WeakerAccess", "unused"})
public class RedisGenerator {

    @Resource
    private ValueOperations<String, String> valueOperations;

    /**
     * 初始化值
     */
    private static final String INT_VALUE = "1";


    /**
     * 默认id长度
     */
    private static final int DEFAULT_ID_LEN = 3;


    /**
     * 主键生成器
     *
     * @param prefix 主键前缀
     * @param key    redis key
     * @return id
     */
    public String generate(String prefix, final String key, int keyLen, Duration timeout) {
        final String finalKey = Optional.ofNullable(key).filter(StringUtils::isNotBlank).orElseThrow(() -> new NullPointerException("key is null."));
        StringBuilder sb = new StringBuilder(prefix);
        try {
            // 设置redis值
            Boolean success = Optional.ofNullable(timeout)
                    .map(e -> valueOperations.setIfAbsent(finalKey, INT_VALUE, e))
                    .orElseGet(() -> valueOperations.setIfAbsent(key, INT_VALUE));
            // 成功第一个设置值
            if (Objects.nonNull(success) && success) {
                sb.append(String.format("%0" + keyLen + "d", Long.valueOf(INT_VALUE)));
                return sb.toString();
            }
            Long increment = valueOperations.increment(key);
            sb.append(String.format("%0" + keyLen + "d", increment));
            return sb.toString();
        } catch (Exception e) {
            throw new BizException(e, ResponseCode.REDIS_SERVICE_IS_DOWN.getCode(), ResponseCode.REDIS_SERVICE_IS_DOWN.getMessage());
        }
    }

    /**
     * 主键生成器
     *
     * @param prefix 主键前缀
     * @param key    redis key
     * @return id
     */
    public String generate(String prefix, String key, int keyLen) {
        return this.generate(prefix, key, keyLen, null);
    }

    /**
     * 主键生成器
     *
     * @param prefix 主键前缀
     * @param key    redis key
     * @return id
     */
    public String generate(String prefix, String key) {
        return this.generate(prefix, key, DEFAULT_ID_LEN);
    }
}


