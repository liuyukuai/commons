package com.cutefool.commons.autoconfig.face;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@SuppressWarnings("all")
@ConfigurationProperties(prefix = "commons.face")
public class FaceConfiguration {

    /**
     * 人脸识别类型
     */
    @Value("${commons.face.type:baidu}")
    private String type;

    /**
     * 人脸识别配置百分百
     */
    @Value("${commons.face.score.min:80}")
    private String scoreMin;

}