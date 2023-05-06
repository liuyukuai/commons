/*
 *
 */
package com.cutefool.commons.app;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Objects;

/**
 * @author 271007729@qq.com
 * @date 2020/4/23 12:48 PM
 */
@Data
public class AppNotice {
    /**
     * 设备唯一标识
     */
    private String device;
    /**
     * 通知栏提示文字
     */
    private String ticker;

    /**
     * 通知标题
     */
    private String title;

    /**
     * 通知文字描述
     */
    private String text;

    /**
     * 未读消息总数
     */
    private Long badge;

    /**
     * 扩展字段
     */
    private Map<String, String> params;

    /**
     * 去掉a标签
     */
    public void clean() {
        if (StringUtils.isNotBlank(this.text)) {
            this.text = this.text.replaceAll("<a href[^>]*>", "");
            this.text = this.text.replaceAll("</a>", "");
        }
        if (Objects.isNull(badge) || badge < 0) {
            this.badge = 0L;
        }

        if (this.badge > 99) {
            this.badge = 99L;
        }
    }
}
