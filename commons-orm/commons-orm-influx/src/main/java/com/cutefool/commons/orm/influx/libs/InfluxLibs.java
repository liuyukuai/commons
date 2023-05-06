/*
 *
 */
package com.cutefool.commons.orm.influx.libs;

import com.influxdb.annotations.Column;
import lombok.Data;

import java.time.Instant;

/**
 * libs
 *
 * @author 271007729@qq.com
 * @date 11/1/21 5:43 PM
 */
@Data
public class InfluxLibs {


    /**
     * 数据ID
     */
    private Long id;

    /**
     * 创建时间
     */
    @Column(timestamp = true)
    private Instant time;
}
