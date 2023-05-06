package com.cutefool.commons.core.digest;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DigestLibs {

    /**
     * 时间
     */
    private Long time;

    /**
     * 值
     */
    private String value;
}
