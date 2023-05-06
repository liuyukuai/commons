/*
 *
 */
package com.cutefool.commons.office.excel.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * excel 模板sheet需要的字典数据
 *
 * @author 271007729@qq.com
 * @date 2021/12/21 10:23
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExcelSheet {

    /**
     * 填充的sheet页名称【优先级高于sheetIndex】
     */
    private String sheetName;

    /**
     * 填充的sheet页下标【0表示第1个sheet，优先级低于sheetName】
     */
    private Integer sheetIndex;

    /**
     * 数据填充-开始行下标【0表示第1行】
     */
    private Integer row;

    /**
     * 填充的数据-动态字典
     */
    private List<ExcelDict> dictList;

    /**
     * 填充的数据-级联字典
     */
    private List<ExcelCascade> dictCascade;


}