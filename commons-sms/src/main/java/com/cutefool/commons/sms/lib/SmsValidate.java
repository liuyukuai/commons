/*
 *
 */
package com.cutefool.commons.sms.lib;

import lombok.extern.slf4j.Slf4j;
import com.cutefool.commons.core.Constants;
import com.cutefool.commons.core.page.Response;
import com.cutefool.commons.core.util.NumUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 短信验证器
 *
 * @author 271007729@qq.com
 * @date 2020/3/11 10:42 AM
 */
@Slf4j
@Component
@SuppressWarnings("WeakerAccess")
public class SmsValidate {

    /**
     * 每分钟最大限制次数
     */
    private static final Integer MINUTE_COUNT = 1;
    /**
     * 每小时最大限制次数
     */
    private static final Integer HOUR_COUNT = 10;
    /**
     * 每天最大限制次数
     */
    private static final Integer DAY_COUNT = 30;

    /**
     * 每小时最大限制次数（先设置200）
     */
    private static final Integer SYSTEM_HOUR_COUNT = 500;

    /**
     * 每分钟的秒数
     */
    private static final Integer MINUTE_SECOND = 60;
    /**
     * 每小时的秒数
     */
    private static final Integer HOUR_SECOND = 3600;
    /**
     * 每天的秒数
     */
    private static final Integer DAY_SECOND = 86400;

    /**
     * 前缀
     */
    private static final String PREFIX = "PHONE";

    /**
     * 分隔符
     */
    private static final String SEPARATOR = ":";

    /**
     * 发送短信时，一分钟内发送短信次数缓存名称前缀
     */
    private static String minuteCountCacheName = "MinuteOfSenderCount";

    /**
     * 发送短信时，一小时内发送短信次数缓存名称前缀
     */
    private static String hourCountCacheName = "HourOfSenderCount";

    /**
     * 发送短信时，一天内发送短信次数缓存名称前缀
     */
    private static String dayCountCacheName = "DayOfSenderCount";


    @Resource
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 验证发送人是否可以继续发送短信
     *
     * @param sender 发信人
     */
    public Response<String> valid(String sender) {
        if (StringUtils.isBlank(sender)) {
            return Response.failure("发送人信息为空");
        }

        Long hourCount = getHourCount(sender);

        // 如果是系统发送
        if (Objects.equals(sender, Constants.SYSTEM)) {
            // 先不限制
            if (hourCount >= SYSTEM_HOUR_COUNT) {
                // 发送预警
                return Response.failure("用户" + sender + "系统每分钟发送短信的次数超过" + SYSTEM_HOUR_COUNT);
            }
            return Response.ok("");
        }
        Long minuteCount = getMinuteCount(sender);

        Long dayCount = getDayCount(sender);
        if (minuteCount >= MINUTE_COUNT) {
            log.debug("用户{}每分钟发送短信的次数超过最大限制", sender);
            return Response.failure("用户" + sender + "每分钟发送短信的次数超过最大限制");
        }
        if (hourCount >= HOUR_COUNT) {
            log.debug("用户{}每小时发送短信的次数超过最大限制", sender);
            return Response.failure("用户" + sender + "每小时发送短信的次数超过最大限制");
        }
        if (dayCount >= DAY_COUNT) {
            log.debug("用户{}每天发送短信的次数超过最大限制", sender);
            return Response.failure("用户" + sender + "每天发送短信的次数超过最大限制");
        }
        return Response.ok("");
    }

    /**
     * 记录发信人单位时间内发出的信息条数
     *
     * @param sender
     */
    public boolean setSendMessageCount(String sender) {
        log.debug("add sendSystem short message count for sender");
        this.increment(PREFIX + SEPARATOR + sender + SEPARATOR + minuteCountCacheName, MINUTE_SECOND);
        this.increment(PREFIX + SEPARATOR + sender + SEPARATOR + hourCountCacheName, HOUR_SECOND);
        this.increment(PREFIX + SEPARATOR + sender + SEPARATOR + dayCountCacheName, DAY_SECOND);
        return true;
    }


    /**
     * @param key
     * @param liveTime
     */
    public Long increment(String key, long liveTime) {
        RedisAtomicLong entityIdCounter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
        Long increment = entityIdCounter.getAndIncrement();
        if (increment == 0 && liveTime > 0) {
            entityIdCounter.expire(liveTime, TimeUnit.SECONDS);
        }
        return increment;
    }


    /**
     * 获取发送人一分钟内发出的信息条数
     *
     * @param sender
     */
    private Long getMinuteCount(String sender) {
        String s = this.redisTemplate.opsForValue().get(PREFIX + SEPARATOR + sender + SEPARATOR + minuteCountCacheName);
        return StringUtils.isNotBlank(s) ? NumUtils.longVal(s, 0L) : 0L;
    }

    /**
     * 获取发送人一小时发出的信息条数
     *
     * @param sender
     */
    private Long getHourCount(String sender) {
        String s = this.redisTemplate.opsForValue().get(PREFIX + SEPARATOR + sender + SEPARATOR + hourCountCacheName);
        return StringUtils.isNotBlank(s) ? NumUtils.longVal(s, 0L) : 0L;
    }

    /**
     * 获取发送人一天发出的信息条数
     *
     * @param sender
     */
    private Long getDayCount(String sender) {
        String s = this.redisTemplate.opsForValue().get(PREFIX + SEPARATOR + sender + SEPARATOR + dayCountCacheName);
        return StringUtils.isNotBlank(s) ? NumUtils.longVal(s, 0L) : 0L;
    }

}
