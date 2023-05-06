/*
 *
 */
package com.cutefool.commons.core.util;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Safelist;

/**
 * html2text 工具类
 *
 * @author 271007729@qq.com
 * @date 1/27/21 3:36 PM
 */
@SuppressWarnings("unused")
public final class Texts {

    public static String getText(String html) {
        if (StringUtils.isBlank(html)) {
            return "";
        }
        Jsoup.clean(html, "", Safelist.none(), new Document.OutputSettings().prettyPrint(false));
        return Jsoup.parse(html).text();
    }
}