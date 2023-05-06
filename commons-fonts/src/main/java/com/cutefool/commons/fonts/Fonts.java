/*
 *  
 */
package com.cutefool.commons.fonts;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;

import java.awt.*;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 字体加载类
 *
 * @author 271007729@qq.com
 * @date 8/7/21 9:44 AM
 */

@Slf4j
@SuppressWarnings({"unused", "WeakerAccess"})
public class Fonts {


    /**
     * 字体映射关系
     */
    private static final Map<String, String> FONTS_MAP = new HashMap<>();

    static {
        FONTS_MAP.put("宋体", "fonts/simsun.ttc");
        FONTS_MAP.put("微软雅黑", "fonts/msyh.ttc");
    }


    public static Font load(String name, int size) {

        return load(name, Font.TRUETYPE_FONT, size);
    }


    public static Font load(String name, int fontFormat, int size) {

        String fontType = FONTS_MAP.get(name);
        // 如果为空，说明是默认字体
        if (StringUtils.isBlank(fontType)) {
            return new Font(name, Font.PLAIN, size);
        }

        ClassPathResource resource = new ClassPathResource(fontType);
        try (InputStream is = resource.getInputStream();) {
            Font dynamicFont = Font.createFont(fontFormat, is);
            return dynamicFont.deriveFont(Font.PLAIN, size);
        } catch (Exception e) {
            log.warn(" load {} not success message = {} ", fontType, e.getMessage());
            return new Font(name, Font.PLAIN, size);
        }
    }

}
