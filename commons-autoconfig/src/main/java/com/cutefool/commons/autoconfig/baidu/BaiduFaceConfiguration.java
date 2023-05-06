package com.cutefool.commons.autoconfig.baidu;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@SuppressWarnings("all")
@ConfigurationProperties(prefix = "commons.baidu.face")
public class BaiduFaceConfiguration {

    /**
     * 百度云应用的AK
     */
    @Value("${commons.baidu.face.ak:kazddcHNM2NQc2O5UbxyLeA4}")
    private String ak;

    /**
     * 百度云应用的SK
     */
    @Value("${commons.baidu.face.sk:78HRSPcWHGl9f1sH2QpU5R312i27CjGm}")
    private String sk;

    /**
     * 百度云应用的appId
     */
    @Value("${commons.baidu.face.appId:30509524}")
    private String appId;

}
