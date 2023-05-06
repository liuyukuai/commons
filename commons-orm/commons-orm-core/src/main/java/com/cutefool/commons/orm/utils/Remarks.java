package com.cutefool.commons.orm.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.stream.Stream;

/**
 * 读取remarks
 */
public class Remarks {

    public static String remarks(String remarks) {
        if (StringUtils.isBlank(remarks)) {
            return remarks;
        }
        // 前端注释|后端注释
        // 读取前端注释
        remarks = StringUtils.isBlank(remarks) ? "未知" : remarks;
        String[] split = remarks.split("\\|");
        return Stream.of(split).findFirst().orElse(remarks);
    }

}
