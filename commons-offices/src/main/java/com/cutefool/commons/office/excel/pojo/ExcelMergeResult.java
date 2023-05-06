/*
 *
 */
package com.cutefool.commons.office.excel.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * excel 判断单元格合并结果
 *
 * @author 271007729@qq.com
 * @date 2021/12/21 10:23
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExcelMergeResult implements Serializable {

    /**
     * 是否是合并的单元格
     */
    private boolean flag;

    /**
     * 当前合并区域的开始行
     */
    private int rowBegin;

    /**
     * 当前合并区域的结束行
     */
    private int rowEnd;

    /**
     * 当前合并区域的开始列
     */
    private int columBegin;

    /**
     * 当前合并区域的结束列
     */
    private int columEnd;
}