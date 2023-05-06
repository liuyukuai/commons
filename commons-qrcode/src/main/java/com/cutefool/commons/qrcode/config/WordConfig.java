/*
 *
 */
package com.cutefool.commons.qrcode.config;

import com.cutefool.commons.qrcode.common.QrCodeConstants;
import lombok.Data;
import com.cutefool.commons.core.util.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 添加字体配置类
 *
 * @author 271007729@qq.com
 * @date 4/23/21 10:07 AM
 */
@Data
@SuppressWarnings("unused")
public class WordConfig {

    /**
     * 图片底部文字
     */
    private List<String> words;

    /**
     * 字体大小
     */
    private int fontSize;

    /**
     * 图片后缀，默认png
     */
    private String suffix;

    /**
     * 底部增加高度
     */
    private int bottomHeight;


    private WordConfig(String word, String suffix, int fontSize, int bottomHeight) {
        if (StringUtils.isNotBlank(word)) {
            this.words = Lists.newArrayList(word);
        }
        this.suffix = suffix;
        this.bottomHeight = bottomHeight;
        this.fontSize = fontSize;
    }

    private WordConfig(List<String> words, String suffix, int fontSize, int bottomHeight) {
        this.words = words;
        this.suffix = suffix;
        this.bottomHeight = bottomHeight;
        this.fontSize = fontSize <= 0 ? QrCodeConstants.DEFAULT_FONT_SIZE : fontSize;
    }

    /**
     * 生成配置
     */
    public static WordConfig create(String word) {
        return create(word, QrCodeConstants.DEFAULT_FONT_SIZE);
    }

    public static WordConfig create(List<String> words) {
        return create(words, QrCodeConstants.DEFAULT_FONT_SIZE);
    }

    public static WordConfig create(String word, int fontSize) {
        return new WordConfig(word, QrCodeConstants.DEFAULT_SUFFIX, fontSize, QrCodeConstants.BOTTOM_HEIGHT);
    }

    public static WordConfig create(List<String> words, int fontSize) {
        return new WordConfig(words, QrCodeConstants.DEFAULT_SUFFIX, fontSize, QrCodeConstants.BOTTOM_HEIGHT);
    }

    public WordConfig append(String word) {
        if (StringUtils.isNotBlank(word)) {
            this.words.add(word);
        }
        return this;

    }

}