/*
 *
 */
package com.cutefool.commons.core.forms;

import java.util.Arrays;
import java.util.List;

/**
 * 自定义表单
 *
 * @author 271007729@qq.com
 * @date 2022/06/06 11:04
 */
public enum OptionsEnum {

    /**
     * 文本输入
     */
    INPUT("input", "文本输入"),

    /**
     * 文本域
     */
    TEXTAREA("textarea", "文本域"),

    /**
     * 富文本
     */
    CKEDITOR("ckeditor", "富文本"),

    /**
     * 数字输入
     */
    NUMBER("number", "数字输入"),

    /**
     * 单选框
     */
    RADIO("radio", "单选框"),

    /**
     * 复选框
     */
    CHECKBOX("checkbox", "复选框"),

    /**
     * 下拉选择
     */
    SELECT("select", "下拉选择"),

    /**
     * 日期选择
     */
    DATE("date", "日期选择"),

    /**
     * 时间选择
     */
    TIME("time", "时间选择"),

    /**
     * 日期时间
     */
    DATETIME("dateTime", "日期时间"),

    /**
     * 手机号
     */
    PHONE("phone", "手机号"),

    /**
     * 附件上传
     */
    DOC_UPLOAD("docUpload", "附件上传"),

    /**
     * 级联选择
     */
    CASCADE("cascade", "级联选择"),

    /**
     * 简单表格
     */
    GROUP("group", "简单表格"),

    /**
     * 数据控件-选择人员
     */
    USER("user", "选择人员"),

    /**
     * 选择岗位
     */
    POSITION("position", "选择岗位"),

    /**
     * 选择部门
     */
    DEPARTMENT("department", "选择部门"),

    /**
     * 选择设施
     */
    FACILITY("facility", "选择设施"),

    /**
     * 选择JSA
     */
    JSA("jsa", "选择JSA"),

    /**
     * 选择传感器
     */
    INDICATOR("indicator", "选择传感器"),

    /**
     * 选择物料
     */
    MATERIAL("material", "选择物料"),

    /**
     * 选择产品
     */
    PRODUCT("product", "选择产品"),

    /**
     * 选择产品明细
     */
    PRODUCT_DETAIL("productDetail", "选择产品明细"),

    /**
     * 选择中间品
     */
    INTERMEDIATE("intermediate", "选择中间品"),

    /**
     * 选择原料
     */
    FEEDSTOCK("feedstock", "选择原料"),

    /**
     * 小节标题
     */
    TITLE("title", "小节标题"),

    /**
     * 分割线
     */
    LINE("line", "分割线"),

    /**
     * 内容占位
     */
    AREA("area", "内容占位");

    private final String type;
    private final String name;

    OptionsEnum(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public static List<String> others() {
        return Arrays.asList(AREA.getType(), LINE.getType(), TITLE.getType());
    }

    public static List<String> materials() {
        return Arrays.asList(MATERIAL.getType(), PRODUCT.getType(), PRODUCT_DETAIL.getType(), INTERMEDIATE.getType());
    }

}
