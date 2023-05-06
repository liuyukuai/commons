package com.cutefool.commons.core.util;

import lombok.SneakyThrows;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;

/**
 * URL ENCODER
 */
@SuppressWarnings("unused")
public class URLUtils {

    public static String join(String uri, String params) {
        String empty = Strings.empty(uri);
        if (empty.contains("?")) {
            return empty + "&" + params;
        }
        return empty + "?" + params;
    }

    public static String encode(String uri) {
        return encode(uri, Charset.defaultCharset());
    }

    public static String encode(String uri, String charsetName) {
        return encode(uri, Charset.forName(charsetName));
    }

    @SneakyThrows
    public static String encode(String uri, Charset charset) {
        try {
            return URLEncoder.encode(uri, charset.name());
        } catch (UnsupportedEncodingException e) {
            return URLEncoder.encode(uri, Charset.defaultCharset().name());
        }
    }
}
