/*
 *  
 */
package com.cutefool.commons.office.excel.pojo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Excel 数据导出，递归一对多数据结构
 *
 * @author 271007729@qq.com
 * @date 2022/3/21 10:17
 */
@Data
@SuppressWarnings("unused")
public class ExcelRecursionData {

    /**
     * 开始填充的列下标【0表示第1列】
     */
    private Integer beginColumn;

    /**
     * 数据占用列数【必须大于0，默认值1】
     */
    private Integer columnNum;

    /**
     * 按照 columnNum 顺序填充的数据，数据为空的填充null或""
     */
    private List<String> values;

    /**
     * 子集
     */
    private List<ExcelRecursionData> children;

    /**
     * 单元格 自定义样式
     */
    private ExcelCellType type;

    /**
     * 级联所在级别【开发无需关心，该字段设值无效】
     */
    private int level = 1;


    /**
     * 构造方法
     *
     * @param beginColumn 开始填充的列下标【0表示第1列】
     */
    public ExcelRecursionData(int beginColumn) {
        // 默认值，默认占用1列
        this.columnNum = 1;
        this.beginColumn = beginColumn;
        this.values = new ArrayList<>();
        this.children = new ArrayList<>();
    }


    /**
     * 构造方法
     *
     * @param beginColumn 开始填充的列下标【0表示第1列】
     * @param columnNum   数据占用列数【必须大于0，默认值1】
     */
    public ExcelRecursionData(int beginColumn, int columnNum) {
        this.columnNum = columnNum;
        this.beginColumn = beginColumn;
        this.values = new ArrayList<>();
        this.children = new ArrayList<>();
    }


    /**
     * 构造方法
     *
     * @param beginColumn 开始填充的列下标【0表示第1列】
     * @param columnNum   数据占用列数【必须大于0，默认值1】
     * @param type        单元格 自定义样式
     */
    public ExcelRecursionData(int beginColumn, int columnNum, ExcelCellType type) {
        this.type = type;
        this.columnNum = columnNum;
        this.beginColumn = beginColumn;
        this.values = new ArrayList<>();
        this.children = new ArrayList<>();
    }

}
