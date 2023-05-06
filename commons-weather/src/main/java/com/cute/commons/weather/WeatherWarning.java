/*
 *
 */
package com.cute.commons.weather;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @author 271007729@qq.com
 * @date 2021/6/1 下午2:58
 */
@Data
public class WeatherWarning {

    /**
     * 本条预警的唯一标识，可判断本条预警是否已经存在
     */
    @JsonProperty("id")
    private String warningId;
    /**
     * 预警发布单位，可能为空
     */
    private String sender;
    /**
     * 预警发布时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mmXXX")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mmXXX",timezone = "GMT+8")
    private LocalDateTime pubTime;
    /**
     * 预警信息标题
     */
    private String title;
    /**
     * 预警开始时间，可能为空
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mmXXX")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mmXXX",timezone = "GMT+8")
    private LocalDateTime startTime;
    /**
     * 预警结束时间，可能为空
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mmXXX")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mmXXX",timezone = "GMT+8")
    private LocalDateTime endTime;
    /**
     * 预警状态，可能为空
     * active 预警中或首次预警
     * update 预警信息更新
     * cancel 取消预警
     */
    private String status;
    /**
     * 预警等级
     */
    private String level;
    /**
     * 预警类型ID
     */
    private String type;
    /**
     * 预警类型名称
     */
    private String typeName;
    /**
     * 预警详细文字描述
     */
    private String text;
    /**
     * 与本条预警相关联的预警ID，当预警状态为cancel或update时返回。可能为空
     */
    private String related;

}
