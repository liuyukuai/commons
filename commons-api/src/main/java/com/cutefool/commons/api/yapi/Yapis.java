/*
 *
 */
package com.cutefool.commons.api.yapi;

import lombok.extern.slf4j.Slf4j;
import com.cutefool.commons.api.ApiRequest;
import com.cutefool.commons.core.util.JsonUtils;
import com.cutefool.commons.core.util.Lists;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 工具类
 *
 * @author 271007729@qq.com
 * @date 2019/9/26 11:02 AM
 */
@Slf4j
@SuppressWarnings("WeakerAccess")
public final class Yapis {

    /**
     * 将请求转换为字符串
     *
     * @param request request
     * @return string
     */
    public static String parse(ApiRequest request) {
        if (Objects.isNull(request)) {
            return "";
        }
        Map<String, Object> requestBody = new HashMap<>(16);
        requestBody.put("type", request.getType());

        // 如果是数组
        if (Objects.equals("array", request.getType())) {
            Map<String, Object> itemMap = new HashMap<>(16);
            itemMap.put("type", "object");
            itemMap.put("description", request.getDescription());
            itemMap.put("properties", parse(request.getFields()));
            requestBody.put("items", itemMap);
        } else {
            // 全部必填的属性
            requestBody.put("required", requires(request.getFields()));
            requestBody.put("properties", parse(request.getFields()));
            requestBody.put("description", request.getDescription());
        }
        return JsonUtils.toJson(requestBody);
    }


    public static Map<String, Map<String, Object>> parse(List<ApiRequest.ApiField> fields) {
        return Lists.empty(fields)
                .stream()
                .collect(Collectors.toMap(ApiRequest.ApiField::getName, e -> {
                    Map<String, Object> parse = parse(e);
                    parse(parse, e.getFields());
                    parse(parse, e, e.getItems());
                    return parse;
                }));
    }


    public static void parse(Map<String, Object> fieldMap, List<ApiRequest.ApiField> fields) {

        if (!Lists.iterable(fields)) {
            return;
        }
        // 必填的属性
        fieldMap.put("required", requires(fields));
        Map<String, Map<String, Object>> map = parse(fields);
        fieldMap.put("properties", map);

    }

    public static void parse(Map<String, Object> fieldMap, ApiRequest.ApiField field, ApiRequest request) {

        if (Objects.isNull(request)) {
            return;
        }

        fieldMap.put("type", "array");
        fieldMap.put("description", field.getDescription());

        Map<String, Object> itemMap = new HashMap<>(16);
        itemMap.put("type", "object");
        itemMap.put("description", request.getDescription());

        // 必填的属性
        fieldMap.put("required", requires(request.getFields()));
        Map<String, Map<String, Object>> map = parse(request.getFields());
        itemMap.put("properties", map);
        fieldMap.put("items", itemMap);
    }


    public static Map<String, Object> parse(ApiRequest.ApiField field) {
        Map<String, Object> fieldMap = new HashMap<>(16);
        fieldMap.put("type", field.getType());
        fieldMap.put("description", field.getDescription());
        return fieldMap;

    }

    private static List<String> requires(List<ApiRequest.ApiField> fields) {
        // 全部必填的属性
        return Lists.empty(fields)
                .stream()
                .filter(ApiRequest.ApiField::isRequired)
                .map(ApiRequest.ApiField::getName)
                .collect(Collectors.toList());
    }
}
