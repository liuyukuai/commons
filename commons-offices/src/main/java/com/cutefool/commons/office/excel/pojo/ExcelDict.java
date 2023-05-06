/*
 *  
 */
package com.cutefool.commons.office.excel.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * 枚举动态字典-数据
 *
 * @author 271007729@qq.com
 * @date 2021/12/22 11:45
 */
@Data
@AllArgsConstructor
public class ExcelDict {

    /**
     * 数据填充-列下标【0表示第1列】
     */
    private Integer column;

    /**
     * 数据值
     */
    private List<String> values;

}
