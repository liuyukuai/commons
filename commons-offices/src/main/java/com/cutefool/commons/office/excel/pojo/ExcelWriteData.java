/*
 *
 */
package com.cutefool.commons.office.excel.pojo;

import lombok.Data;

/**
 * Excel填充单元格的数据
 *
 * @author 271007729@qq.com
 * @date 2022/3/21 10:17
 */
@Data
public class ExcelWriteData {

    /**
     * 数据填充 所在行 开始行
     */
    private Integer rowBegin;

    /**
     * 数据填充 所在行 结束行【为空或等于小于 rowBegin 表示不合并单元格】
     */
    private Integer rowEnd;

    /**
     * 数据填充 所在列【不支持合并列】
     */
    private Integer column;

    /**
     * 数据填充 值
     */
    private String cellValue;

    /**
     * 单元格 自定义样式
     */
    private ExcelCellType type;


    /**
     * 构造方法
     */
    public ExcelWriteData() {
    }


    /**
     * 构造方法
     *
     * @param type 单元格 自定义样式
     */
    public ExcelWriteData(ExcelCellType type) {
        this.type = type;
    }

}
