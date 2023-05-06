package com.cutefool.commons.core;

/**
 * 常用类
 *
 * @author 271007729@qq.com
 * @date 2019-07-10 08:17
 */

@SuppressWarnings({"unused"})
public class Constants {

    /**
     * 年份格式
     */
    public static final String YEAR_PATTERN = "yyyy";

    /**
     * 月份格式
     */
    public static final String MONTH_PATTERN = "yyyy-MM";

    /**
     * 月份格式
     */
    public static final String MONTH_PATTERN_ZH = "yyyy年MM月";

    /**
     * 日期格式
     */
    public static final String DATE_PATTERN = "yyyy-MM-dd";

    /**
     * 小时
     */
    public static final String DATE_HOUR_PATTERN = "yyyy-MM-dd HH";

    /**
     * 小时（带：00）
     */
    public static final String DATE_HOUR_ZERO_PATTERN = "yyyy-MM-dd HH:00";

    /**
     * 分钟
     */
    public static final String DATE_MINUTES_PATTERN = "yyyy-MM-dd HH:mm";

    /**
     * 时间格式24小时
     */
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 时间格式（不带日期）
     */
    public static final String TIME_PATTERN = "HH:mm:ss";

    /**
     * 时间格式24小时
     */
    public static final String DATE_PATTERN_NONE = "yyyyMMdd";

    /**
     * 时间格式24小时
     */
    public static final String DATE_MINUTES_PATTERN_NONE = "yyyyMMddHHmm";

    /**
     * 时间格式24小时
     */
    public static final String DATE_TIME_PATTERN_NONE = "yyyyMMddHHmmss";

    /**
     * 一天小时数
     */
    public static final int HOURS_OF_DAY = 24;

    /**
     * 一天分钟数
     */
    public static final int MINUTES_OF_DAY = 24 * 60;

    /**
     * 一天秒数
     */
    public static final int SECONDS_OF_DAY = 24 * 60 * 60;

    /**
     * 一小时分钟数
     */
    public static final int MINUTES_OF_HOUR = 60;

    /**
     * 一小时秒数
     */
    public static final int SECONDS_OF_HOUR = 60 * 60;

    /**
     * 分钟秒数
     */
    public static final int SECONDS_OF_MINUTES = 60;

    /**
     * 半年月份数
     */
    public static final int MONTH_OF_HALF_YEAR = 6;

    /**
     * 系统
     */
    public static final String SYSTEM = "system";

    /**
     * 请求头(x-token)
     */
    public static final String HEADER_X_TOKEN = "x-token";

    /**
     * 请求头(x-user)
     */
    public static final String HEADER_X_USER = "x-user";

    /**
     * tempDir
     */
    public static final String TEMP_DIR = System.getProperty("java.io.tmpdir");

    /**
     * 用户家目录
     */
    public static final String USER_HOME = System.getProperty("user.home");

    /**
     * 中国邮政编码,6位数字
     */
    public static final String REGEXP_POSTAL_CODE = "^[0-9]{6}$";

    /**
     * 传真格式 简单正则表达式
     */
    public static final String REGEXP_FAX = "^(\\d{3,4})?(-)?\\d{7,8}$";

    /**
     * 默认父ID
     */
    public static final Long DEFAULT_PARENT = 0L;

    /**
     * 应用名称
     */
    public static final String SPRING_APPLICATION_NAME = "spring.application.name";

}
