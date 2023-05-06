/*
 *
 */
package com.cutefool.commons.office.excel.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;
import java.util.Map;

/**
 * Excel 校验结果
 *
 * @author 271007729@qq.com
 * @date 2021/11/24 16:39
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExcelCheckResult {

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 工作薄
     */
    private Workbook workbook;

    /**
     * 读取的sheet下标【0表示第1个sheet】
     */
    private int index = 0;

    /**
     * 放入时间
     */
    private Long createTime;

    /**
     * 行 校验错误信息 Map<行下标, List<提示信息>> 0表示第1行
     */
    private Map<Integer, List<String>> mapErrorMsg;

    /**
     * 行+列 校验错误信息 Map<行下标, Map<列下标, List<提示信息>>>  0表示第1行或第1列
     */
    private Map<Integer, Map<Integer, List<String>>> rowColumnErrorMsg;

    /**
     * 多个sheet导入时封装错误提示信息
     * 工作簿+行+列表 校验错误信息 Map<工作簿下标, Map<行下标, Map<列下标, List<提示信息>>>>  0表示第1工作簿或第1行或第1列
     */
    private Map<Integer, Map<Integer, Map<Integer, List<String>>>> sheetColumnErrorMsg;
}
