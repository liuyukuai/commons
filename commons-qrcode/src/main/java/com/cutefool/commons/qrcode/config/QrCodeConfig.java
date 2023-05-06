/*
 *  
 */
package com.cutefool.commons.qrcode.config;

import com.cutefool.commons.qrcode.common.QrCodeConstants;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import com.cutefool.commons.core.util.Lists;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

/**
 * 二维码配置
 *
 * @author 271007729@qq.com
 * @date 4/22/21 10:42 AM
 */
@Data
@Slf4j
@SuppressWarnings({"unused", "WeakerAccess"})
public class QrCodeConfig {


    /**
     * 二维码宽度，默认500
     */
    private int width;

    /**
     * 二维码高度，默认500
     */
    private int height;

    /**
     * 图片后缀，默认png
     */
    private String suffix;

    /**
     * 二维码内容
     */
    private String content;

    /**
     * 中间logo
     */
    private LogoConfig logoConfig;

    /**
     * 图片底部文字
     */
    private WordConfig wordConfig;

    /**
     * top
     */
    private int borderTop;

    /**
     * left
     */

    private int borderLeft;
    /**
     * right
     */

    private int borderRight;

    /**
     * bottom
     */
    private int borderBottom;

    /**
     * 底部高度
     */
    private int bottomHeight;

    private QrCodeConfig(int width, int height, String suffix, String content) {
        this.borderLeft = 30;
        this.borderRight = 30;
        this.borderTop = 30;
        this.borderBottom = 30;
        this.width = width;
        this.height = height;
        this.suffix = suffix;
        this.content = content;
        this.bottomHeight = QrCodeConstants.BOTTOM_HEIGHT;
    }


    /**
     * 生成配置
     *
     * @param content content
     */
    public static QrCodeConfig create(String content) {
        return new QrCodeConfig(QrCodeConstants.DEFAULT_WIDTH, QrCodeConstants.DEFAULT_HEIGHT, QrCodeConstants.DEFAULT_SUFFIX, content);
    }

    /**
     * 生成配置
     *
     * @param content content
     */
    public static QrCodeConfig create(String content, List<String> words) {
        return create(content, (FileInputStream) null, words, QrCodeConstants.DEFAULT_FONT_SIZE);
    }

    public static QrCodeConfig create(String content, List<String> words, int fontSize) {
        return create(content, (FileInputStream) null, words, fontSize);
    }

    /**
     * 生成配置
     *
     * @param content content
     * @param logo    logo
     */
    public static QrCodeConfig create(String content, File logo) {
        return create(content, logo, Lists.newArrayList());
    }

    /**
     * 生成配置
     *
     * @param content content
     * @param logo    logo
     */
    public static QrCodeConfig create(String content, InputStream logo) {
        return create(content, logo, Lists.newArrayList());
    }


    /**
     * 生成配置
     *
     * @param content content
     * @param logo    logo
     * @param words   word
     */
    public static QrCodeConfig create(String content, File logo, List<String> words) {
        return create(content, logo, words, QrCodeConstants.DEFAULT_FONT_SIZE);
    }

    /**
     * 生成配置
     *
     * @param content content
     * @param logo    logo
     * @param words   word
     */
    public static QrCodeConfig create(String content, File logo, List<String> words, int fontSize) {
        try {
            return create(content, new FileInputStream(logo), words, fontSize);
        } catch (FileNotFoundException e) {
            log.warn("can not find logo = {}", logo);
            return create(content, words);
        }
    }


    /**
     * 生成配置
     *
     * @param content    content
     * @param logoBase64 logoBase64
     * @param words      word
     */
    public static QrCodeConfig create(String content, String logoBase64, List<String> words) {
        return create(content, logoBase64, words, QrCodeConstants.DEFAULT_FONT_SIZE);
    }

    /**
     * 生成配置
     *
     * @param content    content
     * @param logoBase64 logoBase64
     * @param words      word
     */
    public static QrCodeConfig create(String content, String logoBase64, List<String> words, int fontSize) {
        QrCodeConfig qrCodeConfig = create(content);
        if (Objects.nonNull(logoBase64)) {
            qrCodeConfig.setLogoConfig(LogoConfig.create(logoBase64));
        }

        if (Lists.iterable(words)) {
            qrCodeConfig.setWordConfig(WordConfig.create(words, fontSize));
        }
        return qrCodeConfig;
    }


    /**
     * 生成配置
     *
     * @param content content
     * @param logo    logo
     */
    public static QrCodeConfig create(String content, InputStream logo, List<String> words) {
        return create(content, logo, words, QrCodeConstants.DEFAULT_FONT_SIZE);
    }

    public static QrCodeConfig create(String content, InputStream logo, List<String> words, int fontSize) {
        QrCodeConfig qrCodeConfig = create(content);
        if (Objects.nonNull(logo)) {
            qrCodeConfig.setLogoConfig(LogoConfig.create(logo));
        }

        if (Lists.iterable(words)) {
            qrCodeConfig.setWordConfig(WordConfig.create(words, fontSize));
        }
        return qrCodeConfig;
    }

    public boolean isDraw() {
        return Objects.nonNull(logoConfig) || Objects.nonNull(wordConfig);
    }

}