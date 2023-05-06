/*
 *
 */
package com.cutefool.commons.office.excel.pojo;

import lombok.Data;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;
import java.util.Map;

/**
 * Excel 解析中 依赖参数【需要开发者自己维护依赖的参数】
 *
 * @author 271007729@qq.com
 * @date 2022/3/21 10:24
 */
@Data
public class ExcelRelyRead<T> {

    /**
     * Excel 工作薄
     */
    private Workbook workbook;

    /**
     * Excel 动态表头 ==> Map<列号, 表头列名称>
     */
    private Map<Integer, String> mapTitle;

    /**
     * 校验错误信息 Map<Excel行下标, List<String>> 0表示第1行
     */
    private Map<Integer, List<String>> mapMsg;

    /**
     * 业务/数据校验【按需-依赖的参数】
     */
    private T param;


}
