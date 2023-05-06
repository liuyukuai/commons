package com.cutefool.commons.cache.libs;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExpireLibs {

    /**
     * 过期时间（秒）
     */
    private long expire;
}
