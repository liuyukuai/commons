/*
 *  
 */
package com.cutefool.commons.qrcode.config;

import com.cutefool.commons.qrcode.common.QrCodeConstants;
import lombok.Data;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;


/**
 * @author 271007729@qq.com
 * @date 4/22/21 1:26 PM
 */
@Data
@SuppressWarnings({"unused", "WeakerAccess"})
public class LogoConfig {

    private int width;

    private int height;

    private InputStream is;

    private boolean compress;

    private LogoConfig(int width, int height, InputStream file) {
        this.width = width;
        this.height = height;
        this.is = file;
        this.compress = true;
    }

    private LogoConfig(int width, int height, String base64) {
        this.width = width;
        this.height = height;
        byte[] decode = Base64.getDecoder().decode(base64);
        this.is = new ByteArrayInputStream(decode);
        this.compress = true;
    }

    /**
     * 生成配置
     */
    public static LogoConfig create(InputStream file) {
        return new LogoConfig(QrCodeConstants.DEFAULT_LOGO_WIDTH, QrCodeConstants.DEFAULT_LOGO_HEIGHT, file);
    }

    public static LogoConfig create(String base64) {
        return new LogoConfig(QrCodeConstants.DEFAULT_LOGO_WIDTH, QrCodeConstants.DEFAULT_LOGO_HEIGHT, base64);
    }
}