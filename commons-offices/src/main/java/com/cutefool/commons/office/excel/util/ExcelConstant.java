package com.cutefool.commons.office.excel.util;

/**
 * excel 导入适用常量
 *
 * @author 271007729@qq.com
 * @date 2020/2/14 11:04
 */
public class ExcelConstant {

    /**
     * Excel 不同版本 后缀名
     */
    public static String EXCEL_03_SUFFIX_REGEX = "^.+\\.(?i)(xls)$";
    public static String EXCEL_07_SUFFIX_REGEX = "^.+\\.(?i)(xlsx)$";


    /**
     * 小数点
     */
    public static final String SPLIT_POINT_EN = ".";

    /**
     * 字符串  0
     */
    public static final String STR_ZERO = "0";


    /**
     * 时间格式
     */
    public static final String FORMAT_00 = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_01 = "yyyy/MM/dd HH:mm:ss";
    public static final String FORMAT_02 = "yyyy年MM月dd日 HH:mm:ss";
    public static final String FORMAT_03 = "yyyy年MM月dd日 HH时mm分ss秒";
    public static final String FORMAT_04 = "yyyyMMddHHmmss";
    public static final String FORMAT_05 = "yyyy-MM-dd KK:mm:ss A";
    public static final String FORMAT_06 = "yyyy-MM-dd HH:mm:ss.SSS";

    public static final String FORMAT_10 = "yyyy-MM-dd HH:mm";
    public static final String FORMAT_11 = "yyyy/MM/dd HH:mm";
    public static final String FORMAT_12 = "yyyy年MM月dd日 HH:mm";
    public static final String FORMAT_13 = "yyyy年MM月dd日 HH时mm分";
    public static final String FORMAT_14 = "yyyyMMddHHmm";

    public static final String FORMAT_20 = "yyyy-MM-dd HH";
    public static final String FORMAT_21 = "yyyy/MM/dd HH";
    public static final String FORMAT_22 = "yyyy年MM月dd日 HH";
    public static final String FORMAT_23 = "yyyy年MM月dd日 HH时";
    public static final String FORMAT_24 = "yyyyMMddHH";

    public static final String FORMAT_30 = "yyyy-MM-dd";
    public static final String FORMAT_31 = "yyyy/MM/dd";
    public static final String FORMAT_32 = "yyyy年MM月dd日";
    public static final String FORMAT_33 = "yyyyMMdd";
    public static final String FORMAT_34 = "HH:mm:ss";
    public static final String FORMAT_35 = "HH时mm分ss秒";
    public static final String FORMAT_36 = "KK:mm:ss A";
    public static final String FORMAT_37 = "HH:mm:ss.SSS";
    public static final String FORMAT_38 = "yyyy.MM.dd";
    public static final String FORMAT_39 = "MMddHHmm";

    /**
     * Number of milliseconds in A standard second.
     */
    public static final long MILLIS_PER_SECOND = 1000;
    /**
     * Number of milliseconds in A standard minute.
     */
    public static final long MILLIS_PER_MINUTE = 60 * MILLIS_PER_SECOND;
    /**
     * Number of milliseconds in A standard hour.
     */
    public static final long MILLIS_PER_HOUR = 60 * MILLIS_PER_MINUTE;
    /**
     * Number of milliseconds in A standard day.
     */
    public static final long MILLIS_PER_DAY = 24 * MILLIS_PER_HOUR;
    /**
     * Number of milliseconds in A standard week.
     */
    public static final long MILLIS_PER_WEEK = 7 * MILLIS_PER_DAY;


}
